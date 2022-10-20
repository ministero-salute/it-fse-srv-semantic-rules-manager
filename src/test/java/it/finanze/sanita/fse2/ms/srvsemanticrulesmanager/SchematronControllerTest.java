package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager;

import brave.Tracer;
import com.mongodb.MongoException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.base.AbstractTest;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.base.MockRequests;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.controller.impl.SchematronCTL;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.SchematronBodyDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.SchematronDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.BusinessException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.repository.entity.SchematronETY;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.service.ISchematronSRV;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Date;

import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.base.MockRequests.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(SchematronCTL.class)
@ComponentScan
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(Constants.Profile.TEST)
@AutoConfigureMockMvc
class SchematronControllerTest extends AbstractTest {

    private final String TEST_SCHEMATRON_ID= "62dfca42dcbf3c671892df93";
	private final String TEST_NAME_SCHEMATRON = "Name_A";
    private final String TEST_ID_ROOT = "Root_A"; 
    
    private final String TEST_ID_ROOT_INV = "Root_A_INV"; 

    private final String TEST_ID_ROOT_NOT_FOUND = "Root_A_NF"; 
    private final String TEST_ID_EXTENSION_NOT_FOUND = "Ext_A_NF"; 

	public final String TEST_JSON_SCHEMATRON = "{\"nameSchematron\":\"Test_AB\",\"templateIdRoot\":\"Root_AB\", \"version\":\"1.0\"}"; 
	public final Binary TEST_CONTENT_SCHEMATRON = new Binary(BsonBinarySubType.BINARY, "Hello World!".getBytes());

	
	@Autowired
	private MockMvc mvc; 
	
	@MockBean
	private Tracer tracer; 
	
	@SpyBean
	private ISchematronSRV schematronService; 
	
	
	@BeforeEach
    public void setup() throws Exception {
		mongo.dropCollection(SchematronETY.class);
    }

	@AfterAll
	public void teardown() {
		mongo.dropCollection(SchematronETY.class);
	}

	@Test
	void insertSchematron() throws Exception {
	    MockMultipartFile multipartFile = new MockMultipartFile("file", "schematron_post.sch", MediaType.APPLICATION_JSON_VALUE, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>!<schema><schema>".getBytes());

	    MockMultipartHttpServletRequestBuilder builder =
	            MockMvcRequestBuilders.multipart("/v1/schematron");
	    
	    builder.with(new RequestPostProcessor() {
	        @Override
	        public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
	            request.setMethod("POST");
	            return request;
	        }
	    }); 
	    
	    mvc.perform(builder
	            .file(multipartFile)
				.part(new MockPart("templateIdRoot", TEST_ID_ROOT.getBytes()))
				.part(new MockPart("version", "1.0".getBytes()))
				.contentType(MediaType.MULTIPART_FORM_DATA))
	            .andExpect(MockMvcResultMatchers.status().isCreated());
	} 
	
	@Test
	void insertSchematronEmptyFileTest() throws Exception {
	    MockMultipartFile multipartFile = new MockMultipartFile("file", "schematron_post.sch", MediaType.APPLICATION_JSON_VALUE, "".getBytes());

	    MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/v1/schematron");
	    
	    builder.with(new RequestPostProcessor() {
	        @Override
	        public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
	            request.setMethod("POST");
	            return request;
	        }
	    });

		mvc.perform(builder
						.file(new MockMultipartFile("file", multipartFile.getBytes()))
						.part(new MockPart("templateIdRoot", TEST_ID_ROOT.getBytes()))
						.part(new MockPart("version", "1.0".getBytes()))
						.contentType(MediaType.MULTIPART_FORM_DATA))
	            .andExpect(status().isBadRequest()); 
	}
    
	@Test
	void insertSchematronWithBusinessException() throws Exception {	    
		Mockito.doThrow(new BusinessException("")).when(mongo).findOne(any(Query.class), eq(SchematronETY.class));

	    MockMultipartFile multipartFile = new MockMultipartFile("file", "schematron_post.sch", MediaType.APPLICATION_JSON_VALUE, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>!<schema><schema>".getBytes());

	    MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/v1/schematron");
	    

	    builder.with(new RequestPostProcessor() {
	        @Override
	        public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
	            request.setMethod("POST");
	            return request;
	        }
	    });

		mvc.perform(builder
						.file(multipartFile)
						.part(new MockPart("templateIdRoot", TEST_ID_ROOT.getBytes()))
						.part(new MockPart("version", "1.0".getBytes()))
						.contentType(MediaType.MULTIPART_FORM_DATA))
	            .andExpect(status().is5xxServerError()); 
	} 
    
	@Test
	void getSchematronWithBusinessException() throws Exception {	    
	    MockMvcRequestBuilders.multipart("/v1/schematron");

		// invalid id -> bad request
		mvc.perform(getSchematronByIdMockRequest(TEST_ID_ROOT_INV)).andExpectAll(
				status().isBadRequest()
		);

		// when mongo fails for generic exception
		Mockito.doThrow(new BusinessException("")).when(mongo).findOne(any(Query.class), eq(SchematronETY.class));

		mvc.perform(getSchematronByIdMockRequest("690000000000000000000000")).andExpectAll(
				status().is5xxServerError()
		);
	} 
	
	@Test
	void insertSchematronWithOperationException() throws Exception {
		SchematronBodyDTO dto = new SchematronBodyDTO(); 
	    dto.setNameSchematron("name"); 
	    dto.setTemplateIdRoot("root"); 
	    dto.setVersion("extension"); 
	    
	    
	    MockMvcRequestBuilders.multipart("/v1/schematron", dto);

		// when mongo is down
		Mockito.doThrow(new MongoException("")).when(mongo).findOne(any(Query.class), eq(SchematronETY.class));

		mvc.perform(getSchematronByIdMockRequest("690000000000000000000000")).andExpectAll(
				status().is5xxServerError()
		);
	                
	}
	
    
	
	@Test
	void updateSchematron() throws Exception {
		this.insertSchematron();
	    MockMultipartFile multipartFile = new MockMultipartFile("file", "schematron_post.sch", MediaType.APPLICATION_JSON_VALUE, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>!<schema><schema>".getBytes());
	    
	    MockMultipartHttpServletRequestBuilder builder =
	            MockMvcRequestBuilders.multipart("/v1/schematron");
	    
	    builder.with(new RequestPostProcessor() {
	        @Override
	        public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
				request.setMethod("PUT");
	            return request;
	        }
	    });

		mvc.perform(builder
				.file(multipartFile)
				.part(new MockPart("templateIdRoot", TEST_ID_ROOT.getBytes()))
				.part(new MockPart("version", "1.1".getBytes()))
				.contentType(MediaType.MULTIPART_FORM_DATA))
				.andExpect(status().isOk());
	    
	} 
 	
	@Test
	void updateSchematronInvalidMethod() throws Exception {
	    MockMultipartFile multipartFile = new MockMultipartFile("file", "schematron.sch", MediaType.APPLICATION_JSON_VALUE, "Hello World!".getBytes());

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
	            .andExpect(status().isMethodNotAllowed()); 
	    
	} 
	
	@Test
	void updateSchematronNotFound() throws Exception {
		this.insertSchematron();
	    MockMultipartFile multipartFile = new MockMultipartFile("file", "schematron_post.sch", MediaType.APPLICATION_JSON_VALUE, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>!<schema><schema>".getBytes());
	    
	    MockMultipartHttpServletRequestBuilder builder =
	            MockMvcRequestBuilders.multipart("/v1/schematron");
	    
	    builder.with(new RequestPostProcessor() {
	        @Override
	        public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
				request.setMethod("PUT");
	            return request;
	        }
	    });

		mvc.perform(builder
				.file(multipartFile)
				.part(new MockPart("templateIdRoot", TEST_ID_ROOT_INV.getBytes()))
				.part(new MockPart("version", "1.1".getBytes()))
				.contentType(MediaType.MULTIPART_FORM_DATA))
				.andExpect(status().isNotFound());
	    
	} 

	
	 @Test
	void deleteSchematronNotFoundTest() throws Exception {
		mvc.perform(deleteSchematronMockRequest(TEST_ID_ROOT_NOT_FOUND, TEST_ID_EXTENSION_NOT_FOUND)).andExpectAll(
	            status().isNotFound()
	        ); 
	}  

	    
	 
	@Test
	void findSchematronByIdRootAndExtensionTest() throws Exception {
		SchematronDTO dto = new SchematronDTO();

		dto.setId(TEST_SCHEMATRON_ID);
		dto.setNameSchematron(TEST_NAME_SCHEMATRON);
		dto.setContentSchematron(TEST_CONTENT_SCHEMATRON);
		dto.setTemplateIdRoot("Root_AB");
		dto.setVersion("1.0");
		dto.setInsertionDate(new Date());
		dto.setLastUpdateDate(new Date());

		schematronService.insert(dto);

		mvc.perform(querySchematronMockRequest("Root_AB", "1.0")).andExpectAll(
	            status().isOk()
	        );
	}

	@Test
	void findSchematronByIdTest() throws Exception {
		SchematronDTO dto = new SchematronDTO();

		dto.setId(TEST_SCHEMATRON_ID);
		dto.setNameSchematron(TEST_NAME_SCHEMATRON);
		dto.setContentSchematron(TEST_CONTENT_SCHEMATRON);
		dto.setTemplateIdRoot("Root_AB");
		dto.setVersion("1.0");
		dto.setInsertionDate(new Date());
		dto.setLastUpdateDate(new Date());

		schematronService.insert(dto);

		mvc.perform(MockRequests.getSchematronByIdMockRequest(TEST_SCHEMATRON_ID)).andExpectAll(
	            status().isOk()
	        );
	}

	@Test
	void findSchematronByIdNotFoundTest() throws Exception {
		mvc.perform(MockRequests.getSchematronByIdMockRequest(TEST_SCHEMATRON_ID)).andExpectAll(
	            status().isNotFound()
	        );
	}
	
	@Test
	void findSchematronByIdRootAndExtensionInvalidRootAndExtensionTest() throws Exception {
		mvc.perform(querySchematronMockRequest(TEST_ID_ROOT_NOT_FOUND, "1.0")).andExpectAll(
	            status().isNotFound()
	        );
	} 
	
	@Test
	void getSchematronsTest() throws Exception {
		when(schematronService.getSchematrons())
			.thenReturn(new ArrayList<>()); 
		
		mvc.perform(getSchematronsMockRequest()).andExpectAll(
	            status().isOk()
	        );
	} 
}

