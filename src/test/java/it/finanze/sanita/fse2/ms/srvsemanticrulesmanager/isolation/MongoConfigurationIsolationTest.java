package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.isolation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.bson.Document;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.config.mongo.MongoDatabaseCFG;

@Tag("ISOLATION")
class MongoConfigurationIsolationTest {
    @Test void mongoTemplateDisablesTypeDiscriminatorsAndRetainsFactory() {
        MongoDatabaseFactory factory = mock(MongoDatabaseFactory.class);
        when(factory.getExceptionTranslator()).thenReturn(new org.springframework.data.mongodb.core.MongoExceptionTranslator());
        MongoDatabaseCFG config = new MongoDatabaseCFG() {
            @Override public MongoDatabaseFactory mongoDatabaseFactory() { return factory; }
        };
        try (var context = new StaticApplicationContext()) {
            var template = config.mongoTemplate(context);
            assertSame(factory, template.getMongoDatabaseFactory());
            var converter = (MappingMongoConverter) template.getConverter();
            Document output = new Document();
            converter.getTypeMapper().writeType(ServiceIsolationTest.class, output);
            assertFalse(output.containsKey("_class"));
        }
    }
}
