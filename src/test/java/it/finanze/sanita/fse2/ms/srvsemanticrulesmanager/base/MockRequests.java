/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.base;

import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class MockRequests {

    private static final Path VALID_SCH = Paths.get(
        "src",
        "test",
        "resources",
        "schematron",
        "files",
        "schematron_certificato_VACC v1.2.sch"
    );

    private static final Path NOT_VALID_SCH = Paths.get(
        "src",
        "test",
        "resources",
        "schematron",
        "files",
        "schematron_invalid.sch"
    );

    /**
     * Private constructor to disallow to access from other classes
     */
    private MockRequests() {}

    public static MockHttpServletRequestBuilder insertSchematronByTemplateAndVersion(
        String template,
        String version,
        MockMultipartFile file
    ) {
       return multipart("/v1/schematron").part(
           new MockPart("templateIdRoot", template.getBytes()),
           new MockPart("version", version.getBytes())
       ).file(file);
    }

    public static MockHttpServletRequestBuilder updateSchematronByTemplateAndVersion(
        String template,
        String version,
        MockMultipartFile file
    ) {
        MockMultipartHttpServletRequestBuilder req = multipart("/v1/schematron").part(
            new MockPart("templateIdRoot", template.getBytes()),
            new MockPart("version", version.getBytes())
        ).file(file);
        // Modify output method
        req.with(request -> {
            request.setMethod(HttpMethod.PUT.name());
            return request;
        });
        return req;
    }

    public static MockHttpServletRequestBuilder getSchematronChangeSet(String lastUpdate) {
        return get("/v1/changeset/schematron/status?lastUpdate=" + lastUpdate)
        			.contentType(MediaType.APPLICATION_JSON_VALUE); 
    }

    public static MockHttpServletRequestBuilder deleteSchematronMockRequest(String root, String extension) {
        return delete("/v1/schematron/root/" + root + "/extension/" + extension)
        			.contentType(MediaType.APPLICATION_JSON_VALUE); 
    } 
 
    public static MockHttpServletRequestBuilder getSchematronByIdMockRequest(String id) {
        return get("/v1/schematron/id/" + id).contentType(MediaType.APPLICATION_JSON_VALUE);
    } 
    
    public static MockHttpServletRequestBuilder getSchematronsMockRequest() {
        return get("/v1/schematron").contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    public static MockHttpServletRequestBuilder querySchematronMockRequest(String root, String version) {
        return get("/v1/schematron/root/" + root + "/version/" + version)
        			.contentType(MediaType.APPLICATION_JSON_VALUE); 
    }

    public static MockMultipartFile createFromResourceSCH(String field, boolean isValid) throws IOException {
        return new MockMultipartFile(
            field,
            isValid ? VALID_SCH.getFileName().toString() : NOT_VALID_SCH.getFileName().toString(),
            APPLICATION_XML_VALUE,
            isValid ? new FileInputStream(VALID_SCH.toFile()) : new FileInputStream(NOT_VALID_SCH.toFile())
        );
    }

}
