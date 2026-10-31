package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.isolation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.*;
import io.swagger.v3.oas.models.parameters.RequestBody;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.config.*;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.utility.ProfileUtility;

@Tag("ISOLATION")
@ActiveProfiles("ISOLATION")
@SpringJUnitConfig(ConfigurationIsolationTest.Context.class)
@TestPropertySource(properties = {"docs.info.api-id=semantic", "docs.info.summary=summary", "docs.info.title=Rules",
    "info.app.version=1.2", "docs.info.description=description", "docs.info.termsOfService=terms",
    "docs.info.contact.name=Support", "docs.info.contact.url=https://example.org", "docs.info.contact.mail=support@example.org",
    "server.port=8080", "validation.file-max-size=4096"})
class ConfigurationIsolationTest {
    @org.springframework.context.annotation.Profile("ISOLATION")
    @Configuration(proxyBeanMethods = false)
    @Import({CustomSwaggerCFG.class, OpenApiCFG.class, CollectionNaming.class, ProfileUtility.class})
    static class Context {}
    @Autowired OpenApiCFG config;
    @Autowired CollectionNaming naming;
    @Autowired ProfileUtility profile;

    static Schema<Object> uploadSchema() {
        Schema<Object> schema = new Schema<>();
        schema.addProperties("file", new StringSchema());
        schema.addProperties("content_schematron", new StringSchema());
        return schema;
    }
    static Operation upload(Schema<?> schema) {
        return new Operation().requestBody(new RequestBody().content(new Content()
            .addMediaType("multipart/form-data", new MediaType().schema(schema))));
    }
    static OpenAPI api() { return new OpenAPI().info(new Info()).components(new Components().schemas(new java.util.LinkedHashMap<>())).paths(new Paths()); }

    @Test void customizesMetadataMultipartLimitsAndSchemaProperties() {
        var post = uploadSchema(); var put = uploadSchema(); var patch = uploadSchema();
        OpenAPI api = api();
        api.getComponents().addSchemas("Upload", patch);
        api.getPaths().addPathItem("/post", new PathItem().post(upload(post)));
        api.getPaths().addPathItem("/put", new PathItem().put(upload(put)));
        api.getPaths().addPathItem("/patch", new PathItem().patch(upload(new Schema<>().$ref("#/components/schemas/Upload"))));
        api.getPaths().addPathItem("/get", new PathItem().get(new Operation()));
        config.openApiCustomiser().customise(api);
        assertEquals("Rules", api.getInfo().getTitle()); assertEquals("1.2", api.getInfo().getVersion());
        assertEquals("description", api.getInfo().getDescription()); assertEquals("terms", api.getInfo().getTermsOfService());
        assertEquals("Support", api.getInfo().getContact().getName());
        assertEquals("https://example.org", api.getInfo().getContact().getUrl());
        assertEquals("support@example.org", api.getInfo().getContact().getEmail());
        assertEquals("semantic", api.getInfo().getExtensions().get("x-api-id"));
        assertEquals("summary", api.getInfo().getExtensions().get("x-summary"));
        assertEquals("http://localhost:8080", api.getServers().get(0).getUrl());
        assertEquals(true, api.getServers().get(0).getExtensions().get("x-sandbox"));
        for (var schema : new Schema[]{post, put, patch}) {
            assertEquals(false, schema.getAdditionalProperties());
            assertEquals(4096, ((Schema<?>) schema.getProperties().get("file")).getMaxLength());
        }
        assertEquals(4096, post.getProperties().get("content_schematron").getMaxLength());
        assertEquals(4096, put.getProperties().get("content_schematron").getMaxLength());
        config.disableAdditionalResponseProperties().customise(api);
        assertEquals(false, patch.getAdditionalProperties());
    }

    @Test void binarySchemasReceiveTypeAndArrayConstraintsOnlyWhereApplicable() {
        OpenAPI api = api(); var binary = new Schema<>().name("binary");
        var type = new StringSchema().name("type"); var data = new ArraySchema().items(new StringSchema()).name("data");
        binary.addProperties("type", type); binary.addProperties("data", data); binary.addProperties("other", new Schema<>().name("other"));
        api.getComponents().addSchemas("Binary", binary).addSchemas("Other", new Schema<>().name("other"));
        config.binaryProperties().customise(api);
        assertEquals(10000, type.getMaxLength()); assertEquals(0, type.getMinLength());
        assertEquals(10000, data.getMaxItems()); assertEquals(0, data.getMinItems());
        assertEquals(10000, ((ArraySchema) data).getItems().getMaxLength());
    }

    @Test void putCustomizerResolvesReferencesAndToleratesAbsentRequestMetadata() {
        OpenAPI api = api(); Schema<Object> resolved = uploadSchema();
        api.getComponents().addSchemas("Upload", resolved);
        api.getPaths().addPathItem("/reference", new PathItem().put(upload(new Schema<>().$ref("#/components/schemas/Upload"))));
        api.getPaths().addPathItem("/missing-reference", new PathItem().put(upload(new Schema<>().$ref("#/components/schemas/Missing"))));
        api.getPaths().addPathItem("/no-body", new PathItem().put(new Operation()));
        api.getPaths().addPathItem("/no-content", new PathItem().put(new Operation().requestBody(new RequestBody())));
        api.getPaths().addPathItem("/no-media", new PathItem().put(new Operation().requestBody(new RequestBody().content(new Content()))));
        api.getPaths().addPathItem("/no-schema", new PathItem().put(new Operation().requestBody(new RequestBody().content(new Content().addMediaType("multipart/form-data", new MediaType())))));
        api.getPaths().addPathItem("/no-file", new PathItem().put(upload(new Schema<>().addProperties("other", new StringSchema()))));
        api.getPaths().addPathItem("/get", new PathItem().get(new Operation()));
        config.updateFileMaxLength().customise(api);
        assertEquals(4096, resolved.getProperties().get("file").getMaxLength());
        assertEquals(false, resolved.getAdditionalProperties());
    }

    @Test void patchCustomizationHandlesMissingMetadataAndUnresolvedReferences() {
        OpenAPI api = api();
        api.getPaths().addPathItem("/no-body", new PathItem().patch(new Operation()));
        api.getPaths().addPathItem("/no-content", new PathItem().patch(new Operation().requestBody(new RequestBody())));
        api.getPaths().addPathItem("/no-media", new PathItem().patch(new Operation().requestBody(new RequestBody().content(new Content()))));
        api.getPaths().addPathItem("/unresolved", new PathItem().patch(upload(new Schema<>().$ref("#/components/schemas/Missing"))));
        api.getPaths().addPathItem("/empty", new PathItem().patch(upload(new Schema<>())));
        api.getPaths().addPathItem("/other", new PathItem().patch(upload(new Schema<>().addProperties("other", new StringSchema()))));
        config.openApiCustomiser().customise(api);
        assertEquals(false, api.getPaths().get("/empty").getPatch().getRequestBody().getContent().get("multipart/form-data").getSchema().getAdditionalProperties());
    }

    @Test void collectionNamingUsesTestPrefixOnlyForTestProfiles() {
        assertFalse(profile.isTestProfile()); assertEquals("schematron_eds", naming.getIniEdsInvocationCollection(profile));
        ProfileUtility test = mock(ProfileUtility.class); when(test.isTestProfile()).thenReturn(true);
        assertEquals("test_schematron_eds", new CollectionNaming().getIniEdsInvocationCollection(test));
        assertFalse(new ProfileUtility().isTestProfile());
        try (var context = new AnnotationConfigApplicationContext()) {
            context.getEnvironment().setActiveProfiles("test"); context.register(ProfileUtility.class); context.refresh();
            assertTrue(context.getBean(ProfileUtility.class).isTestProfile());
        }
        try (var context = new AnnotationConfigApplicationContext(ProfileUtility.class)) {
            assertFalse(context.getBean(ProfileUtility.class).isTestProfile());
        }
    }
}
