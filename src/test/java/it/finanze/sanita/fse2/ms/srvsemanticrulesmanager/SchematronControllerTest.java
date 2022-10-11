package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager;

import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.base.MockRequests.deleteSchematronMockRequest;
import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.base.MockRequests.getSchematronByIdMockRequest;
import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.base.MockRequests.getSchematronsMockRequest;
import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.base.MockRequests.querySchematronMockRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;

import com.mongodb.MongoException;

import brave.Tracer;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.base.AbstractTest;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.controller.impl.SchematronCTL;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.SchematronBodyDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.SchematronDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.BusinessException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.repository.entity.SchematronETY;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.service.ISchematronSRV;


@WebMvcTest(SchematronCTL.class)
@ComponentScan
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(Constants.Profile.TEST)
@AutoConfigureMockMvc
class SchematronControllerTest extends AbstractTest {

    private final String TEST_SCHEMATRON_ID= "62dfca42dcbf3c671892df93"; 
	private final String TEST_NAME_SCHEMATRON = "Name_A"; 
    private final String TEST_ID_ROOT = "Root_A"; 
    private final String TEST_ID_EXTENSION = "Ext_A"; 
    
    private final String TEST_ID_ROOT_INV = "Root_A_INV"; 

    private final String TEST_ID_ROOT_NOT_FOUND = "Root_A_NF"; 
    private final String TEST_ID_EXTENSION_NOT_FOUND = "Ext_A_NF"; 

	public final String TEST_JSON_SCHEMATRON = "{\"nameSchematron\":\"Test_AB\",\"templateIdRoot\":\"Root_AB\", \"version\":\"1.0\"}"; 
	public final Binary TEST_CONTENT_SCHEMATRON = new Binary(BsonBinarySubType.BINARY, "Hello World!".getBytes());

	
	@Autowired
	private MockMvc mvc; 
	
	@MockBean
	private Tracer tracer; 
	
	@MockBean
	private ISchematronSRV schematronService; 
	
	
	@BeforeAll
    public void setup() throws Exception {
		mongo.dropCollection(SchematronETY.class);
		populateSchematron();
    }

	@Test
	void insertSchematron() throws Exception {
	    MockMultipartFile multipartFile = new MockMultipartFile("file", "schematron_post.xml", MediaType.APPLICATION_JSON_VALUE, "Hello World!".getBytes());

		LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.put("templateIdRoot", Collections.singletonList(TEST_ID_ROOT));
		map.put("version", Collections.singletonList("1.0"));
	    
	    MockMultipartHttpServletRequestBuilder builder =
	            MockMvcRequestBuilders.multipart("/v1/schematron");
	    
	    builder.with(new RequestPostProcessor() {
	        @Override
	        public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
	            request.setMethod("POST");
	            request.setParameter("file", "test");
	            return request;
	        }
	    }); 
	    
	    mvc.perform(builder
	            .file(new MockMultipartFile("file", multipartFile.getBytes()))
	            .params(map)
	            .contentType(MediaType.MULTIPART_FORM_DATA))
	            .andExpect(MockMvcResultMatchers.status().isCreated());
	} 
	
	@Test
	void insertSchematronEmptyFileTest() throws Exception {
	    MockMultipartFile multipartFile = new MockMultipartFile("file", "schematron_post.xml", MediaType.APPLICATION_JSON_VALUE, "".getBytes());

	    MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/v1/schematron");
	    
	    builder.with(new RequestPostProcessor() {
	        @Override
	        public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
	            request.setMethod("POST");
	            return request;
	        }
	    });

		LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.put("templateIdRoot", Collections.singletonList(TEST_ID_ROOT));
		map.put("version", Collections.singletonList("1.0"));
	    
	    mvc.perform(builder
	            .file(multipartFile)
						.params(map)
	            .contentType(MediaType.MULTIPART_FORM_DATA))
	            .andExpect(status().is2xxSuccessful()); 
	}
    
    
	@Test
	void insertSchematronWithBusinessException() throws Exception {
		
		SchematronBodyDTO dto = new SchematronBodyDTO(); 
	    dto.setNameSchematron("name"); 
	    dto.setTemplateIdRoot("root"); 
	    dto.setVersion("extension"); 
	    
	    MockMvcRequestBuilders.multipart("/v1/schematron", dto);
	    
        when(
                schematronService.findById(anyString())
            ).thenThrow(new BusinessException("Unknown id"));
	   
	    
		mvc.perform(getSchematronByIdMockRequest(TEST_ID_ROOT_NOT_FOUND)).andExpectAll(
	            status().isBadRequest()
	        ); 
	                
	} 
	
	@Test
	void insertSchematronWithOperationException() throws Exception {
		SchematronBodyDTO dto = new SchematronBodyDTO(); 
	    dto.setNameSchematron("name"); 
	    dto.setTemplateIdRoot("root"); 
	    dto.setVersion("extension"); 
	    
	    
	    MockMvcRequestBuilders.multipart("/v1/schematron", dto);
	    
        when(
                schematronService.findById(anyString())
            ).thenThrow(new OperationException("Database Error", new MongoException("MongoDB: Error while inserting schematron"))); 
	   
	    
		mvc.perform(getSchematronByIdMockRequest(TEST_ID_ROOT_NOT_FOUND)).andExpectAll(
	            status().isBadRequest()
	        ); 
	                
	}
	
    
	
	@Test
	void updateSchematron() throws Exception {
	    MockMultipartFile multipartFile = new MockMultipartFile("file", "schematron.xml", MediaType.APPLICATION_JSON_VALUE, "Hello World!".getBytes());

		LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.put("nameSchematron", Collections.singletonList("name"));
		map.put("templateIdRoot", Collections.singletonList(TEST_ID_ROOT));
		map.put("version", Collections.singletonList("1.0"));
	    
	    MockMultipartHttpServletRequestBuilder builder =
	            MockMvcRequestBuilders.multipart("/v1/schematron");
	    
	    builder.with(new RequestPostProcessor() {
	        @Override
	        public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
	            request.setParameter("file", "test");
				request.setMethod("PUT");
	            return request;
	        }
	    }); 
	    
	    mvc.perform(builder
	            .file(new MockMultipartFile("file", multipartFile.getBytes()))
				.params(map)
	            .contentType(MediaType.MULTIPART_FORM_DATA))
	            .andExpect(status().is2xxSuccessful()); 
	    
	} 
 	
	@Test
	void updateSchematronInvalidMethod() throws Exception {
	    MockMultipartFile multipartFile = new MockMultipartFile("file", "schematron.xml", MediaType.APPLICATION_JSON_VALUE, "Hello World!".getBytes());

	    MockMultipartHttpServletRequestBuilder builder =
	            MockMvcRequestBuilders.multipart("/v1/schematron");
	    
	    builder.with(new RequestPostProcessor() {
	        @Override
	        public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
	            request.setMethod("PATCH");
	            return request;
	        }
	    }); 
	    
	    mvc.perform(builder
	            .file(multipartFile)
	            .contentType(MediaType.MULTIPART_FORM_DATA))
	            .andExpect(status().is4xxClientError()); 
	    
	} 
	
	
	 @Test
	void deleteSchematronNotFoundTest() throws Exception {
		
		mvc.perform(deleteSchematronMockRequest(TEST_ID_ROOT_NOT_FOUND, TEST_ID_EXTENSION_NOT_FOUND)).andExpectAll(
	            status().is4xxClientError()
	        ); 
	}  

	    
	 
	@Test
	void findSchematronByIdRootAndExtensionTest() throws Exception {
		SchematronDTO dto = new SchematronDTO(); 
		
		dto.setId(TEST_SCHEMATRON_ID); 
		dto.setNameSchematron(TEST_NAME_SCHEMATRON); 
		dto.setContentSchematron(TEST_CONTENT_SCHEMATRON);
		dto.setTemplateIdRoot(TEST_ID_ROOT); 
		dto.setVersion(TEST_ID_EXTENSION); 
		dto.setInsertionDate(new Date()); 
		dto.setLastUpdateDate(new Date()); 
		
		schematronService.insert(dto); 
		
		
		when(schematronService.findByTemplateIdRootAndVersion(TEST_ID_ROOT, TEST_ID_EXTENSION))
			.thenReturn(new SchematronDTO()); 
	
		mvc.perform(querySchematronMockRequest(TEST_ID_ROOT, TEST_ID_EXTENSION)).andExpectAll(
	            status().is2xxSuccessful()
	        );
		
		mvc.perform(getSchematronByIdMockRequest(TEST_SCHEMATRON_ID)).andExpectAll(
	            status().is2xxSuccessful()
	        );
		
	}
	
	@Test
	void findSchematronByIdRootAndExtensionInvalidRootTest() throws Exception {
		SchematronDTO dto = new SchematronDTO(); 
		
		dto.setNameSchematron(TEST_NAME_SCHEMATRON); 
		dto.setTemplateIdRoot(TEST_ID_ROOT); 
		dto.setVersion(TEST_ID_EXTENSION); 
		dto.setInsertionDate(new Date()); 
		dto.setLastUpdateDate(new Date()); 
		
		schematronService.insert(dto); 
		
		
		when(schematronService.findByTemplateIdRootAndVersion(TEST_ID_ROOT_INV, TEST_ID_EXTENSION))
			.thenReturn(new SchematronDTO()); 
	
		mvc.perform(querySchematronMockRequest(TEST_ID_ROOT, TEST_ID_EXTENSION)).andExpectAll(
	            status().is(200)
	        );
	} 
	
	@Test
	void findSchematronByIdRootAndExtensionInvalidRootAndExtensionTest() throws Exception {
	
		mvc.perform(querySchematronMockRequest(TEST_ID_ROOT_NOT_FOUND, TEST_ID_EXTENSION_NOT_FOUND)).andExpectAll(
	            status().is(200)
	        );
	} 
	
	@Test
	void getSchematronsTest() throws Exception {
		when(schematronService.getSchematrons())
			.thenReturn(new ArrayList<>()); 
		
		mvc.perform(getSchematronsMockRequest()).andExpectAll(
	            status().is2xxSuccessful()
	        );
	} 
	
	@Test
	void getSchematronJsonObject() {
		SchematronBodyDTO dto = SchematronCTL.getSchematronJSONObject(TEST_JSON_SCHEMATRON); 
		
		assertEquals(SchematronBodyDTO.class, dto.getClass()); 
		assertEquals(String.class, dto.getTemplateIdRoot().getClass()); 
		assertEquals(String.class, dto.getVersion().getClass()); 
		
		assertEquals("Root_AB", dto.getTemplateIdRoot()); 
		assertEquals("1.0", dto.getVersion()); 

	} 
	
	 
}
