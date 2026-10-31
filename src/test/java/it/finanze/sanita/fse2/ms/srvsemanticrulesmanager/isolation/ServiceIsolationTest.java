package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.isolation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;
import org.bson.types.Binary;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.repository.entity.SchematronETY;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.repository.mongo.impl.SchematronRepo;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.service.impl.SchematronSRV;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.*;

@Tag("ISOLATION")
@ExtendWith(MockitoExtension.class)
class ServiceIsolationTest {
    @Mock SchematronRepo repository;
    @InjectMocks SchematronSRV service;

    static SchematronETY entity(String version) {
        SchematronETY e = new SchematronETY();
        e.setId("507f1f77bcf86cd799439011");
        e.setTemplateIdRoot("root"); e.setVersion(version); e.setSystem("TS");
        e.setNameSchematron("rules.sch"); e.setContentSchematron(new Binary(new byte[]{1, 2, 3}));
        e.setInsertionDate(new Date(100)); e.setLastUpdateDate(new Date(200));
        return e;
    }

    @Test void insertsOnlyWhenNoActiveVersionExists() throws Exception {
        SchematronETY e = entity("1.0");
        when(repository.insert(e)).thenReturn(e);
        assertSame(e, service.insert(e));
        when(repository.findLatestByTemplateIdRoot("root", "TS")).thenReturn(e);
        assertThrows(DocumentAlreadyPresentException.class, () -> service.insert(e));
        verify(repository, times(1)).insert(e);
    }

    @Test void updateRejectsMissingDuplicateAndOlderVersionsBeforeWriting() throws Exception {
        SchematronETY e = entity("1.0");
        assertThrows(DocumentNotFoundException.class, () -> service.update(e));
        when(repository.findLatestByTemplateIdRoot("root", "TS")).thenReturn(entity("1.0"));
        assertThrows(DocumentAlreadyPresentException.class, () -> service.update(e));
        e.setVersion("0.9");
        assertThrows(InvalidVersionException.class, () -> service.update(e));
        verify(repository, never()).insert(any()); verify(repository, never()).delete(any(), any());
        e.setVersion("2.0"); service.update(e);
        var order = inOrder(repository);
        order.verify(repository).delete("root", "TS"); order.verify(repository).insert(e);
    }

    @Test void readsMapContentDatesAndMissingDocuments() throws Exception {
        SchematronETY e = entity("1.0");
        assertThrows(DocumentNotFoundException.class, () -> service.findById(e.getId()));
        assertThrows(DocumentNotFoundException.class, () -> service.findByTemplateIdRootAndVersion("root", "1.0"));
        assertThrows(DocumentNotFoundException.class, () -> service.findByTemplateIdRoot("root", false));
        when(repository.findById(e.getId())).thenReturn(e);
        when(repository.findByTemplateIdRootAndVersion("root", "1.0")).thenReturn(e);
        when(repository.findByTemplateIdRoot("root", false)).thenReturn(List.of(e));
        assertEquals("AQID", service.findById(e.getId()).getContent());
        assertEquals("1.0", service.findByTemplateIdRootAndVersion("root", "1.0").getVersion());
        assertEquals("root", service.findByTemplateIdRoot("root", false).get(0).getTemplateIdRoot());
        when(repository.findDocs()).thenReturn(List.of(e));
        when(repository.getEveryActiveSchematron()).thenReturn(List.of(e));
        assertEquals(1, service.getSchematrons(true).size());
        assertEquals(1, service.getSchematrons(false).size());
    }

    @Test void changesetsHandleInitialAndIncrementalSyncAndDeletion() throws Exception {
        SchematronETY e = entity("1.0"); Date date = new Date(10);
        when(repository.getEveryActiveSchematron()).thenReturn(List.of(e));
        when(repository.getInsertions(date)).thenReturn(List.of(e));
        when(repository.getDeletions(date)).thenReturn(List.of(e));
        when(repository.getActiveDocumentCount()).thenReturn(7L);
        assertEquals(e.getId(), service.getInsertions(null).get(0).getId());
        assertEquals("1.0", service.getInsertions(date).get(0).getDescription().getVersion());
        assertTrue(service.getDeletions(null).isEmpty());
        assertEquals("root", service.getDeletions(date).get(0).getDescription().getTemplateIdRoot());
        assertEquals(7, service.getCollectionSize());
        assertThrows(DocumentNotFoundException.class, () -> service.deleteSchematron("root", "TS"));
        when(repository.findLatestByTemplateIdRoot("root", "TS")).thenReturn(e);
        when(repository.delete("root", "TS")).thenReturn(2);
        assertEquals(2, service.deleteSchematron("root", "TS"));
    }

    @Test void persistenceErrorsPropagateWithoutBeingRewritten() throws Exception {
        OperationException failure = new OperationException("offline", new com.mongodb.MongoException("offline"));
        when(repository.getActiveDocumentCount()).thenThrow(failure);
        assertSame(failure, assertThrows(OperationException.class, service::getCollectionSize));
    }
}
