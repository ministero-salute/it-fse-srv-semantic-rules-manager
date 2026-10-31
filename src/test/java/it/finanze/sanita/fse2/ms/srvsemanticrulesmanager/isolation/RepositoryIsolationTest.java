package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.isolation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;
import com.mongodb.MongoException;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.repository.entity.SchematronETY;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.repository.mongo.impl.SchematronRepo;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.OperationException;

@Tag("ISOLATION")
@ExtendWith(MockitoExtension.class)
class RepositoryIsolationTest {
    @Mock MongoTemplate mongo;
    @InjectMocks SchematronRepo repository;

    @Test void insertAndSoftDeleteReturnDatabaseResultsAndSetAuditFields() throws Exception {
        SchematronETY e = ServiceIsolationTest.entity("1.0");
        when(mongo.insert(e)).thenReturn(e);
        assertSame(e, repository.insert(e));
        when(mongo.updateMulti(any(Query.class), any(Update.class), eq(SchematronETY.class)))
            .thenReturn(UpdateResult.acknowledged(3, 2L, null));
        long before = System.currentTimeMillis();
        assertEquals(2, repository.delete("root", "TS"));
        var query = ArgumentCaptor.forClass(Query.class); var update = ArgumentCaptor.forClass(Update.class);
        verify(mongo).updateMulti(query.capture(), update.capture(), eq(SchematronETY.class));
        assertEquals(new Document("template_id_root", "root").append("system", "TS")
            .append("deleted", new Document("$ne", true)), query.getValue().getQueryObject());
        Document fields = (Document) update.getValue().getUpdateObject().get("$set");
        assertEquals(true, fields.get("deleted"));
        assertTrue(((Date) fields.get("last_update_date")).getTime() >= before);
    }

    @Test void singleDocumentQueriesUseVersionObjectIdAndNewestInsertion() throws Exception {
        SchematronETY e = ServiceIsolationTest.entity("1.0");
        when(mongo.findOne(any(Query.class), eq(SchematronETY.class))).thenReturn(e);
        assertSame(e, repository.findByTemplateIdRootAndVersion("root", "1.0"));
        assertSame(e, repository.findById(e.getId()));
        assertSame(e, repository.findLatestByTemplateIdRoot("root", "TS"));
        var captor = ArgumentCaptor.forClass(Query.class);
        verify(mongo, times(3)).findOne(captor.capture(), eq(SchematronETY.class));
        var queries = captor.getAllValues();
        assertEquals("1.0", queries.get(0).getQueryObject().get("version"));
        assertEquals(new ObjectId(e.getId()), queries.get(1).getQueryObject().get("_id"));
        assertEquals("TS", queries.get(2).getQueryObject().get("system"));
        assertEquals(new Document("insertion_date", -1), queries.get(2).getSortObject());
        queries.forEach(q -> assertEquals(new Document("$ne", true), q.getQueryObject().get("deleted")));
        assertThrows(IllegalArgumentException.class, () -> repository.findById("invalid"));
    }

    @Test void listQueriesRespectSyncBoundariesDeletedFlagAndSorting() throws Exception {
        var entities = List.of(ServiceIsolationTest.entity("1.0")); Date since = new Date(100);
        when(mongo.find(any(Query.class), eq(SchematronETY.class))).thenReturn(entities);
        when(mongo.findAll(SchematronETY.class)).thenReturn(entities);
        when(mongo.count(any(Query.class), eq(SchematronETY.class))).thenReturn(4L);
        assertSame(entities, repository.getInsertions(since));
        assertSame(entities, repository.getDeletions(since));
        assertSame(entities, repository.getEveryActiveSchematron());
        assertSame(entities, repository.findByTemplateIdRoot("root", false));
        assertSame(entities, repository.findByTemplateIdRoot("root", true));
        assertSame(entities, repository.findDocs()); assertEquals(4, repository.getActiveDocumentCount());
        var captor = ArgumentCaptor.forClass(Query.class);
        verify(mongo, times(5)).find(captor.capture(), eq(SchematronETY.class));
        var q = captor.getAllValues();
        assertEquals(new Document("$gt", since), q.get(0).getQueryObject().get("insertion_date"));
        assertEquals(new Document("$gt", since), q.get(1).getQueryObject().get("last_update_date"));
        assertEquals(new Document("$lte", since), q.get(1).getQueryObject().get("insertion_date"));
        assertEquals(true, q.get(1).getQueryObject().get("deleted"));
        assertEquals(new Document("$ne", true), q.get(2).getQueryObject().get("deleted"));
        assertTrue(q.get(3).getQueryObject().containsKey("deleted"));
        assertFalse(q.get(4).getQueryObject().containsKey("deleted"));
        assertEquals(new Document("insertion_date", -1), q.get(4).getSortObject());
    }

    @Test void allMongoFailuresAreWrappedWithTheirOriginalCause() throws Exception {
        MongoException cause = new MongoException("unavailable");
        when(mongo.insert(any(SchematronETY.class))).thenThrow(cause);
        when(mongo.updateMulti(any(Query.class), any(Update.class), eq(SchematronETY.class))).thenThrow(cause);
        when(mongo.findOne(any(Query.class), eq(SchematronETY.class))).thenThrow(cause);
        when(mongo.find(any(Query.class), eq(SchematronETY.class))).thenThrow(cause);
        when(mongo.findAll(SchematronETY.class)).thenThrow(cause);
        when(mongo.count(any(Query.class), eq(SchematronETY.class))).thenThrow(cause);
        List<org.junit.jupiter.api.function.Executable> operations = List.of(
            () -> repository.insert(ServiceIsolationTest.entity("1.0")),
            () -> repository.delete("root", "TS"),
            () -> repository.findByTemplateIdRootAndVersion("root", "1.0"),
            () -> repository.findById("507f1f77bcf86cd799439011"),
            () -> repository.findDocs(), () -> repository.getInsertions(new Date()),
            () -> repository.getDeletions(new Date()), () -> repository.getActiveDocumentCount(),
            () -> repository.getEveryActiveSchematron(), () -> repository.findLatestByTemplateIdRoot("root", "TS"),
            () -> repository.findByTemplateIdRoot("root", false));
        for (var operation : operations) assertSame(cause, assertThrows(OperationException.class, operation).getCause());
    }
}
