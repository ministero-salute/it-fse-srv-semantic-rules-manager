package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.isolation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import brave.Tracing;
import brave.Tracer;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotBlank;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.controller.impl.*;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.controller.handler.ExceptionCTL;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.SchematronDocumentDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.changes.*;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.enums.SystemTypeEnum;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.*;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.repository.entity.SchematronETY;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.service.ISchematronSRV;

@Tag("ISOLATION")
@ActiveProfiles("ISOLATION")
@SpringJUnitConfig(ControllersIsolationTest.Context.class)
class ControllersIsolationTest {
    @org.springframework.context.annotation.Profile("ISOLATION")
    @Configuration(proxyBeanMethods = false)
    @Import({SchematronCTL.class, ChangeSetCTL.class, ExceptionCTL.class, LivenessCheckCTL.class})
    static class Context {
        @Bean(destroyMethod = "close") Tracing tracing() { return Tracing.newBuilder().build(); }
        @Bean Tracer tracer(Tracing tracing) { return tracing.tracer(); }
    }
    @MockBean ISchematronSRV service;
    @Autowired SchematronCTL controller;
    @Autowired ChangeSetCTL changes;
    @Autowired LivenessCheckCTL health;
    @Autowired ExceptionCTL advice;
    @Autowired Tracer tracer;
    MockMvc mvc;
    static final byte[] VALID = ("<schema xmlns=\"http://purl.oclc.org/dsdl/schematron\" queryBinding=\"xslt2\">"
        + "<pattern><rule context=\"root\"><assert test=\"true()\">ok</assert></rule></pattern></schema>").getBytes(StandardCharsets.UTF_8);
    static MockMultipartFile file(String name, byte[] bytes) { return new MockMultipartFile("file", name, "text/xml", bytes); }

    @BeforeEach void setup() { mvc = MockMvcBuilders.standaloneSetup(controller, health, new Failures()).setControllerAdvice(advice).build(); }

    @Test void validatesAndPersistsRealSchematronUploads() throws Exception {
        assertEquals(1, controller.addSchematron("root", "1.0", SystemTypeEnum.TS, file("rules.sch", VALID)).getInsertedItems());
        assertEquals(1, controller.updateSchematron("root", "2.0", SystemTypeEnum.NONE, file("rules.sch", VALID)).getUpdatedItems());
        var e = ArgumentCaptor.forClass(SchematronETY.class);
        verify(service).insert(e.capture());
        assertArrayEquals(VALID, e.getValue().getContentSchematron().getData());
        assertEquals("TS", e.getValue().getSystem()); assertEquals("root", e.getValue().getTemplateIdRoot());
        assertEquals(e.getValue().getInsertionDate(), e.getValue().getLastUpdateDate());
        verify(service).update(e.capture()); assertNull(e.getValue().getSystem());
    }

    @Test void rejectsMissingEmptyWrongExtensionAndUnreadableFiles() throws Exception {
        for (MultipartFile f : new MultipartFile[]{null, file("rules.sch", new byte[0]), file("rules.xml", VALID), file(null, VALID)}) {
            assertThrows(InvalidContentException.class, () -> controller.addSchematron("root", "1.0", SystemTypeEnum.NONE, f));
            assertThrows(InvalidContentException.class, () -> controller.updateSchematron("root", "1.0", SystemTypeEnum.NONE, f));
        }
        MultipartFile brokenName = mock(MultipartFile.class);
        when(brokenName.getOriginalFilename()).thenThrow(new IllegalStateException("filename"));
        assertThrows(InvalidContentException.class, () -> controller.addSchematron("root", "1.0", SystemTypeEnum.NONE, brokenName));
        MultipartFile unreadable = mock(MultipartFile.class);
        when(unreadable.getOriginalFilename()).thenReturn("rules.sch");
        when(unreadable.getBytes()).thenThrow(new IOException("read failure"));
        assertInstanceOf(IOException.class, assertThrows(SchematronValidatorException.class,
            () -> controller.addSchematron("root", "1.0", SystemTypeEnum.NONE, unreadable)).getCause());
        assertThrows(SchematronValidatorException.class, () -> controller.addSchematron("root", "1.0", SystemTypeEnum.NONE, file("bad.sch", "<wrong/>".getBytes())));
        verifyNoInteractions(service);
    }

    @Test void readsApplyBinaryOptionsAndIncludeRealTraceIdentifiers() throws Exception {
        when(service.getSchematrons(false)).thenAnswer(i -> List.of(SchematronDocumentDTO.fromEntity(ServiceIsolationTest.entity("1.0"))));
        when(service.findByTemplateIdRoot("root", true)).thenReturn(List.of(SchematronDocumentDTO.fromEntity(ServiceIsolationTest.entity("1.0"))));
        when(service.findById("id")).thenReturn(SchematronDocumentDTO.fromEntity(ServiceIsolationTest.entity("1.0")));
        when(service.deleteSchematron("root", "TS")).thenReturn(2);
        assertNull(controller.getSchematrons(false, false).getItems().get(0).getContent());
        var span = tracer.nextSpan().start();
        try (var scope = tracer.withSpanInScope(span)) {
            var result = controller.getSchematrons(true, false);
            assertEquals("AQID", result.getItems().get(0).getContent());
            assertEquals(span.context().traceIdString(), result.getTraceID());
            assertEquals(span.context().spanIdString(), result.getSpanID());
        } finally { span.finish(); }
        assertEquals(1, controller.getSchematronByTemplateIdRoot("root", true, true).getNumberOfItems());
        assertEquals("1.0", controller.getSchematronById("id").getDocument().getVersion());
        assertEquals(2, controller.deleteSchematron("root", SystemTypeEnum.TS).getDeletedItems());
        assertEquals("UP", health.health().getStatus().getCode());
        mvc.perform(get("/status")).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("UP"));
    }

    @Test void aggregatesChangesetCountsAndTimestamps() throws Exception {
        Date since = new Date(100);
        var insertion = new ChangeSetDTO("one", new ChangeSetDTO.Payload("root", "1.0"));
        when(service.getInsertions(since)).thenReturn(List.of(insertion));
        when(service.getDeletions(since)).thenReturn(List.of(insertion, insertion));
        when(service.getCollectionSize()).thenReturn(9L);
        var result = (ChangeSetResDTO) changes.getSchematronChangeSet(null, since);
        assertEquals(since, result.getLastUpdate()); assertNotNull(result.getTimestamp());
        assertEquals(3, result.getTotalNumberOfElements()); assertEquals(9, result.getCollectionSize());
        assertEquals(1, result.getInsertions().size()); assertEquals(2, result.getDeletions().size());
    }

    @Test void translatesEveryApplicationFailureToProblemJson() throws Exception {
        String[] kinds = {"missing", "duplicate", "operation", "version", "content", "schematron", "generic", "constraint"};
        int[] codes = {404, 409, 500, 409, 400, 422, 500, 400};
        for (int i = 0; i < kinds.length; i++) {
            mvc.perform(get("/failure/" + kinds[i])).andExpect(status().is(codes[i]))
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(jsonPath("$.status").value(codes[i])).andExpect(jsonPath("$.detail").isNotEmpty());
        }
        var span = tracer.nextSpan().start();
        try (var scope = tracer.withSpanInScope(span)) {
            mvc.perform(get("/failure/missing")).andExpect(jsonPath("$.traceID").value(span.context().traceIdString()));
        } finally { span.finish(); }
        mvc.perform(get("/number").param("value", "wrong")).andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.instance").value("/constraint/field/value"));
        mvc.perform(multipart("/v1/schematron").param("system", "NONE"))
            .andExpect(status().isBadRequest()).andExpect(jsonPath("$.instance").value("/constraint/field/templateIdRoot"));
    }

    static class InvalidBean { @NotBlank String field = ""; }
    @org.springframework.context.annotation.Profile("ISOLATION")
    @RestController static class Failures {
        @GetMapping("/number") int number(@RequestParam int value) { return value; }
        @GetMapping("/failure/{kind}") void fail(@PathVariable String kind) throws Exception {
            switch (kind) {
                case "missing": throw new DocumentNotFoundException("missing");
                case "duplicate": throw new DocumentAlreadyPresentException("duplicate");
                case "operation": throw new OperationException("offline", new com.mongodb.MongoException("offline"));
                case "version": throw new InvalidVersionException("older");
                case "content": throw new InvalidContentException("empty");
                case "schematron": throw new SchematronValidatorException("invalid");
                case "constraint":
                    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
                        throw new ConstraintViolationException(factory.getValidator().validate(new InvalidBean()));
                    }
                default: throw new IllegalStateException("unexpected");
            }
        }
    }
}
