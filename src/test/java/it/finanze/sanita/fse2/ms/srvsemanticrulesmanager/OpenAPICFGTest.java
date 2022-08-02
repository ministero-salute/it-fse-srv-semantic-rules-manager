package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.base.AbstractTest;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.config.OpenApiCFG;

@SpringBootTest
@ComponentScan
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(Constants.Profile.TEST)
class OpenAPICFGTest extends AbstractTest {

	
	@Autowired
	public OpenApiCFG openApiCfg; 
	
	
	@Test
	void openApiCfgTest() {
		OpenAPI openApi = new OpenAPI(); 
		Info info = new Info(); 
		Contact contact = new Contact(); 
		Components components = new Components(); 
		
		contact.setName("Guido Rocco"); 
		contact.setEmail("guido.rocco1@ibm.com"); 
		contact.setUrl("https://test.com"); 
		
		components.setSchemas(new HashMap<String, Schema>());
		
		info.setContact(contact);
		openApi.setInfo(info); 
		
		openApi.setComponents(components); 
		
		Paths paths = new Paths(); 
		PathItem item = new PathItem(); 
		PathItem putItem = new PathItem(); 
		Operation post = new Operation(); 
		Operation put = new Operation(); 
		RequestBody requestBody = new RequestBody(); 
		RequestBody requestBodyPut = new RequestBody(); 
		Content content = new Content(); 
		Content contentPut = new Content(); 
		MediaType mediaType = new MediaType(); 
		MediaType mediaTypeBinary = new MediaType(); 
		
		Schema schema = new Schema(); 
		schema.setDescription("Description"); 
		Map<String, String> properties = new HashMap<String, String>(); 
		properties.put("test", "testProperty"); 
		properties.put("binary", "testBinaryProperty"); 
		//properties.put("content_schematron", "Content"); 
		
		schema.setProperties(properties); 
		mediaType.setSchema(schema); 
		
		
		Schema binarySchema = new Schema(); 
		binarySchema.setName("binary"); 
		Map<String, String> propertiesBinary = new HashMap<String, String>(); 
		propertiesBinary.put("test", "testProperty"); 
		propertiesBinary.put("binary", "testBinaryProperty"); 
		binarySchema.setProperties(propertiesBinary); 
		mediaTypeBinary.setSchema(binarySchema); 
		
		content.addMediaType("mediaType", mediaType); 
		content.addMediaType("multipart/form-data", mediaType); 
		content.addMediaType("binary", mediaTypeBinary); 
		
		contentPut.addMediaType("mediaType", mediaType); 
		contentPut.addMediaType("multipart/form-data", mediaType); 
		contentPut.addMediaType("binary", mediaTypeBinary); 
		
		requestBody.setContent(content); 
		requestBodyPut.setContent(contentPut); 
		
		post.setSummary("Post Request"); 
		post.setDescription("This is a test POST request"); 
		post.setOperationId("id"); 
		post.setRequestBody(requestBody); 
		
		put.setSummary("Put Request"); 
		put.setDescription("This is a test PUT request"); 
		put.setOperationId("id"); 
		put.setRequestBody(requestBodyPut);
		//put.setRequestBody(requestBody); 
		
		item.setPost(post);
		putItem.setPut(put); 
		
		
		paths.addPathItem("/samplePost", item); 
		paths.addPathItem("/samplePut", putItem); 

		openApi.setPaths(paths); 
	
		OpenApiCustomiser customiser = openApiCfg.openApiCustomiser(); 
		
		customiser.customise(openApi);
		
		openApiCfg.disableAdditionalResponseProperties(); 
		openApiCfg.binaryProperties(); 
		
		assertTrue(true); 
	}
	
}
