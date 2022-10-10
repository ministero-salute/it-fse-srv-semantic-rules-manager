package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.base;

import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.SchematronDTO;

public final class MockRequests {

    /**
     * Private constructor to disallow to access from other classes
     */
    private MockRequests() {}

    public static MockHttpServletRequestBuilder getSchematronChangeSet(String lastUpdate) {
        return get("http://127.0.0.1:9085/v1/changeset/schematron/status?lastUpdate=" + lastUpdate)
        			.contentType(MediaType.APPLICATION_JSON_VALUE); 
    }
    
    public static MockHttpServletRequestBuilder insertSchematronMockRequest(SchematronDTO dto) {
        return post("http://127.0.0.1:9085/v1/schematron/", dto)
        			.contentType(MediaType.APPLICATION_JSON_VALUE); 
    } 
    
    public static MockHttpServletRequestBuilder deleteSchematronMockRequest(String root, String extension) {
        return delete("http://127.0.0.1:9085/v1/schematron/root/" + root + "/extension/" + extension)
        			.contentType(MediaType.APPLICATION_JSON_VALUE); 
    } 
 
    public static MockHttpServletRequestBuilder getSchematronByIdMockRequest(String id) {
        return get("http://127.0.0.1:9085/v1/schematron/id/" + id).contentType(MediaType.APPLICATION_JSON_VALUE);
    } 
    
    public static MockHttpServletRequestBuilder getSchematronsMockRequest() {
        return get("http://127.0.0.1:9085/v1/schematron").contentType(MediaType.APPLICATION_JSON_VALUE);
    } 
    
    public static MockHttpServletRequestBuilder getSchematronsErrorMockRequest() {
        return get("http://127.0.0.1:9085/v1/schematron").contentType(MediaType.MULTIPART_FORM_DATA);
    } 
    
    public static MockHttpServletRequestBuilder querySchematronMockRequest(String root, String version) {
        return get("http://127.0.0.1:9085/v1/schematron/root/" + root + "/version/" + version)
        			.contentType(MediaType.APPLICATION_JSON_VALUE); 
    } 
   

    public static String createBlankString() {
        return "  ";
    }

    public static MockMultipartFile createFakeMultipartEmpty(String filename) {
        return new MockMultipartFile(
            "files",
            filename,
            APPLICATION_XML_VALUE,
            new byte[0]
        );
    }
    
    public static MockMultipartFile createFakeMultipart(String filename) {
        return new MockMultipartFile(
            "files",
            filename,
            APPLICATION_XML_VALUE,
            "Hello world!".getBytes()
        );
    }

}
