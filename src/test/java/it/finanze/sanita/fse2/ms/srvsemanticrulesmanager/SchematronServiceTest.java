package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.base.AbstractTest;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.SchematronDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.SchematronDocumentDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.repository.entity.SchematronETY;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.service.impl.SchematronSRV; 


@SpringBootTest
@ComponentScan
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(Constants.Profile.TEST)
class SchematronServiceTest extends AbstractTest {
	
    // Test Data 
    private final String TEST_SCHEMATRON_ID = "sd7awksdda"; 
    private final String TEST_SCHEMATRON_NAME = "testName"; 
    private final String TEST_SCHEMATRON_ROOT = "testRoot"; 
    private final String TEST_SCHEMATRON_EXTENSION = "testExtension"; 
    private final Binary TEST_SCHEMATRON_CONTENT = new Binary(BsonBinarySubType.BINARY, SCHEMATRON_TEST_STRING.getBytes()); 

    private final String TEST_SCHEMATRON_NAME_INS = "testNameIns"; 
    private final String TEST_SCHEMATRON_ROOT_INS = "testRootIns"; 
    private final String TEST_SCHEMATRON_EXTENSION_INS = "testExtensionIns"; 
    private final Binary TEST_SCHEMATRON_CONTENT_INS = new Binary(BsonBinarySubType.BINARY, SCHEMATRON_TEST_STRING.getBytes()); 
    
    
    private final String TEST_SCHEMATRON_NAME_INS_THROW = "testNameInsThrow"; 
    private final String TEST_SCHEMATRON_ROOT_INS_THROW = "testRootInsThrow"; 
    private final String TEST_SCHEMATRON_EXTENSION_INS_THROW = "testExtensionInsThrow"; 
    private final Binary TEST_SCHEMATRON_CONTENT_INS_THROW = new Binary(BsonBinarySubType.BINARY, SCHEMATRON_TEST_STRING.getBytes()); 
    
    private final ObjectId TEST_SCHEMATRON_ID_NOT_FOUND = new ObjectId("62c6de0e959b683ce2d996d7"); 

    private final String TEST_SCHEMATRON_NAME_INS_3 = "testNameIns_3"; 
    private final String TEST_SCHEMATRON_ROOT_INS_3 = "testRootIns_3"; 
    private final String TEST_SCHEMATRON_EXTENSION_INS_3 = "testExtensionIns_3"; 
    private final Binary TEST_SCHEMATRON_CONTENT_INS_3 = new Binary(BsonBinarySubType.BINARY, SCHEMATRON_TEST_STRING.getBytes()); 
    
    private final String TEST_SCHEMATRON_NAME_DEL = "testNameToDel"; 
    private final String TEST_SCHEMATRON_ROOT_DEL = "testRootDel"; 
    private final String TEST_SCHEMATRON_EXTENSION_DEL = "testExtensionDel"; 
    private final Binary TEST_SCHEMATRON_CONTENT_DEL= new Binary(BsonBinarySubType.BINARY, SCHEMATRON_TEST_STRING.getBytes()); 
    
    private final String TEST_SCHEMATRON_NAME_QUERY = "testNameQuery"; 
    private final String TEST_SCHEMATRON_ROOT_QUERY = "testRootQuery"; 
    private final String TEST_SCHEMATRON_EXTENSION_QUERY = "testExtensionQuery"; 
    private final Binary TEST_SCHEMATRON_CONTENT_QUERY = new Binary(BsonBinarySubType.BINARY, SCHEMATRON_TEST_STRING.getBytes()); 
    
    private final String TEST_SCHEMATRON_NAME_QUERY_DEL = "testNameQueryDel"; 
    private final String TEST_SCHEMATRON_ROOT_QUERY_DEL = "testRootQueryDel"; 
    private final String TEST_SCHEMATRON_EXTENSION_QUERY_DEL = "testExtensionQueryDel"; 
    private final Binary TEST_SCHEMATRON_CONTENT_QUERY_DEL = new Binary(BsonBinarySubType.BINARY, SCHEMATRON_TEST_STRING.getBytes()); 
    
    @Autowired 
    SchematronSRV schematronService; 
    

    
    @BeforeAll
    public void setup() throws Exception {
        this.initTestRepository();
    } 
    
    @Test
    void insertTest() throws DocumentNotFoundException, OperationException, DocumentAlreadyPresentException {
    	SchematronDTO dto = new SchematronDTO(); 
    	
    	Date date = new Date(); 
    	
    	dto.setNameSchematron(TEST_SCHEMATRON_NAME_INS); 
    	dto.setTemplateIdRoot(TEST_SCHEMATRON_ROOT_INS); 
    	dto.setTemplateIdExtension(TEST_SCHEMATRON_EXTENSION_INS); 
    	dto.setContentSchematron(TEST_SCHEMATRON_CONTENT_INS); 
    	dto.setInsertionDate(date); 
    	dto.setLastUpdateDate(date); 

    	schematronService.insert(dto); 
    	
    	SchematronDTO returnedDto = schematronService.findByTemplateIdRootAndTemplateIdExtension(TEST_SCHEMATRON_ROOT_INS, TEST_SCHEMATRON_EXTENSION_INS); 
    	
    	assertEquals(SchematronDTO.class, returnedDto.getClass()); 
    	assertEquals(Binary.class, returnedDto.getContentSchematron().getClass()); 
    	assertEquals(String.class, returnedDto.getNameSchematron().getClass()); 
    	assertEquals(String.class, returnedDto.getTemplateIdRoot().getClass()); 
    	assertEquals(String.class, returnedDto.getTemplateIdExtension().getClass()); 
    	assertEquals(Date.class, returnedDto.getInsertionDate().getClass()); 
    	assertEquals(Date.class, returnedDto.getLastUpdateDate().getClass()); 
    	
    	assertEquals(TEST_SCHEMATRON_NAME_INS, returnedDto.getNameSchematron()); 
    	assertEquals(TEST_SCHEMATRON_ROOT_INS, returnedDto.getTemplateIdRoot()); 
    	assertEquals(TEST_SCHEMATRON_EXTENSION_INS, returnedDto.getTemplateIdExtension()); 
    	assertEquals(TEST_SCHEMATRON_CONTENT_INS, returnedDto.getContentSchematron()); 
    	assertEquals(date, returnedDto.getInsertionDate()); 
    	assertEquals(date, returnedDto.getLastUpdateDate()); 
    	 	
    } 
    
    @Test
    void insertSchematronIfPresent() throws OperationException, DocumentAlreadyPresentException, DocumentNotFoundException {
    	SchematronDTO dto = new SchematronDTO(); 
    	
    	Date date = new Date(); 
    	
    	dto.setNameSchematron(TEST_SCHEMATRON_NAME_INS_THROW); 
    	dto.setTemplateIdRoot(TEST_SCHEMATRON_ROOT_INS_THROW); 
    	dto.setTemplateIdExtension(TEST_SCHEMATRON_EXTENSION_INS_THROW); 
    	dto.setContentSchematron(TEST_SCHEMATRON_CONTENT_INS_THROW); 
    	dto.setInsertionDate(date); 
    	dto.setLastUpdateDate(date); 

    	schematronService.insert(dto); 
    	
    	SchematronDTO dtoNew = new SchematronDTO(); 
    	
    	
    	dtoNew.setNameSchematron(TEST_SCHEMATRON_NAME_INS_THROW); 
    	dtoNew.setTemplateIdRoot(TEST_SCHEMATRON_ROOT_INS_THROW); 
    	dtoNew.setTemplateIdExtension(TEST_SCHEMATRON_EXTENSION_INS_THROW); 
    	dtoNew.setContentSchematron(TEST_SCHEMATRON_CONTENT_INS_THROW); 
    	dtoNew.setInsertionDate(date); 
    	dtoNew.setLastUpdateDate(date); 
    	
    	 	
    	assertThrows(Exception.class, () -> {
    		schematronService.insert(dtoNew); 
    	}); 

    }

    @Test
    void deleteSchematron() throws DocumentNotFoundException, OperationException, DocumentAlreadyPresentException {
    	SchematronDTO dto = new SchematronDTO(); 
    	
    	Date date = new Date(); 
    	
    	dto.setNameSchematron(TEST_SCHEMATRON_NAME_DEL); 
    	dto.setTemplateIdRoot(TEST_SCHEMATRON_ROOT_DEL); 
    	dto.setTemplateIdExtension(TEST_SCHEMATRON_EXTENSION_DEL); 
    	dto.setContentSchematron(TEST_SCHEMATRON_CONTENT_DEL); 
    	dto.setInsertionDate(date); 
    	dto.setLastUpdateDate(date); 

    	schematronService.insert(dto); 
    	
    	schematronService.deleteSchematron(TEST_SCHEMATRON_ROOT_DEL, TEST_SCHEMATRON_EXTENSION_DEL); 
    	
    	assertThrows(DocumentNotFoundException.class, () -> schematronService.findByTemplateIdRootAndTemplateIdExtension(
    			TEST_SCHEMATRON_ROOT_DEL, TEST_SCHEMATRON_EXTENSION_DEL)); 

    }
    
    
    @Test
    void findTest() throws Exception {
    	SchematronDTO dto = new SchematronDTO(); 
    	
    	Date date = new Date(); 
    	
    	dto.setNameSchematron(TEST_SCHEMATRON_NAME_QUERY); 
    	dto.setTemplateIdRoot(TEST_SCHEMATRON_ROOT_QUERY); 
    	dto.setTemplateIdExtension(TEST_SCHEMATRON_EXTENSION_QUERY); 
    	dto.setContentSchematron(TEST_SCHEMATRON_CONTENT_QUERY); 
    	dto.setInsertionDate(date); 
    	dto.setLastUpdateDate(date); 

    	schematronService.insert(dto); 

		// FIND BY TEMPLATE ID ROOT AND TEMPLATE ID EXTENSION

    	SchematronDTO returnedDto = schematronService.findByTemplateIdRootAndTemplateIdExtension(TEST_SCHEMATRON_ROOT_QUERY, TEST_SCHEMATRON_EXTENSION_QUERY); 
    	
    	
    	assertEquals(SchematronDTO.class, returnedDto.getClass()); 
    	assertEquals(Binary.class, returnedDto.getContentSchematron().getClass()); 
    	assertEquals(String.class, returnedDto.getNameSchematron().getClass()); 
    	assertEquals(String.class, returnedDto.getTemplateIdRoot().getClass()); 
    	assertEquals(String.class, returnedDto.getTemplateIdExtension().getClass()); 
    	assertEquals(Date.class, returnedDto.getInsertionDate().getClass()); 
    	assertEquals(Date.class, returnedDto.getLastUpdateDate().getClass()); 
    	
    	assertEquals(TEST_SCHEMATRON_NAME_QUERY, returnedDto.getNameSchematron()); 
    	assertEquals(TEST_SCHEMATRON_ROOT_QUERY, returnedDto.getTemplateIdRoot()); 
    	assertEquals(TEST_SCHEMATRON_EXTENSION_QUERY, returnedDto.getTemplateIdExtension()); 
    	assertEquals(TEST_SCHEMATRON_CONTENT_QUERY, returnedDto.getContentSchematron()); 
    	assertEquals(date, returnedDto.getInsertionDate()); 
    	assertEquals(date, returnedDto.getLastUpdateDate()); 


		// FIND BY ID

    	SchematronDocumentDTO returnedDto_1 = schematronService.findById(returnedDto.getId());
		assertNotNull(returnedDto_1); 
    	
		
		// FIND BY ID - Invalid ID 
		
    	
    	
		// FIND BY ID - Non Existent ID 
    	
    	assertThrows(DocumentNotFoundException.class, () -> schematronService.findById(TEST_SCHEMATRON_ID_NOT_FOUND.toString())); 
    	
		
    }
    
    @Test
    void findByTemplateIdRootAndTemplateIdExtensionDeletedElementTest() throws DocumentNotFoundException, OperationException, DocumentAlreadyPresentException {
    	SchematronDTO dto = new SchematronDTO(); 
    	
    	Date date = new Date(); 
    	
    	dto.setNameSchematron(TEST_SCHEMATRON_NAME_QUERY_DEL); 
    	dto.setTemplateIdRoot(TEST_SCHEMATRON_ROOT_QUERY_DEL); 
    	dto.setTemplateIdExtension(TEST_SCHEMATRON_EXTENSION_QUERY_DEL); 
    	dto.setContentSchematron(TEST_SCHEMATRON_CONTENT_QUERY_DEL); 
    	dto.setInsertionDate(date); 
    	dto.setLastUpdateDate(date); 

    	schematronService.insert(dto); 
    	
    	schematronService.deleteSchematron(TEST_SCHEMATRON_ROOT_QUERY_DEL, TEST_SCHEMATRON_EXTENSION_QUERY_DEL); 

    	assertThrows(DocumentNotFoundException.class, () -> schematronService.findByTemplateIdRootAndTemplateIdExtension(TEST_SCHEMATRON_ROOT_QUERY_DEL, TEST_SCHEMATRON_EXTENSION_QUERY_DEL)); 

    } 
    
    
    @Test
    void getSchematronsTest() throws OperationException, DocumentAlreadyPresentException, DocumentNotFoundException {

    	SchematronDTO dtoFirst = new SchematronDTO(); 
    	
    	Date date = new Date(); 
    	
    	dtoFirst.setNameSchematron(TEST_SCHEMATRON_NAME_INS_3); 
    	dtoFirst.setTemplateIdRoot(TEST_SCHEMATRON_ROOT_INS_3); 
    	dtoFirst.setTemplateIdExtension(TEST_SCHEMATRON_EXTENSION_INS_3); 
    	dtoFirst.setContentSchematron(TEST_SCHEMATRON_CONTENT_INS_3); 
    	dtoFirst.setInsertionDate(date); 
    	dtoFirst.setLastUpdateDate(date); 
   	
    	
    	schematronService.insert(dtoFirst); 
    	
    	List<SchematronDTO> dtoGetList = schematronService.getSchematrons(); 
    	SchematronDTO dtoElem = dtoGetList.get(0); 
    	
    	assertEquals(ArrayList.class, dtoGetList.getClass()); 
    	assertEquals(SchematronDTO.class, dtoElem.getClass()); 
    	
    	assertTrue(dtoGetList.size() > 0); 
    	
    	assertEquals(SchematronDTO.class, dtoElem.getClass()); 
    	assertEquals(Binary.class, dtoElem.getContentSchematron().getClass()); 
    	assertEquals(String.class, dtoElem.getNameSchematron().getClass()); 
    	assertEquals(String.class, dtoElem.getTemplateIdRoot().getClass()); 
    	assertEquals(String.class, dtoElem.getTemplateIdExtension().getClass()); 
    	assertEquals(Date.class, dtoElem.getLastUpdateDate().getClass()); 
    	
    }
    
    @Test
    void buildDtoFromEtyTest() {
    	List<SchematronETY> etyList = new ArrayList<SchematronETY>(); 
		SchematronETY schematronEty = new SchematronETY();
		
		Date date = new Date(); 
		
		schematronEty.setId(TEST_SCHEMATRON_ID); 
		schematronEty.setNameSchematron(TEST_SCHEMATRON_NAME); 
		schematronEty.setTemplateIdRoot(TEST_SCHEMATRON_ROOT); 
		schematronEty.setTemplateIdExtension(TEST_SCHEMATRON_EXTENSION); 
		schematronEty.setContentSchematron(TEST_SCHEMATRON_CONTENT); 
		schematronEty.setInsertionDate(date); 
		schematronEty.setLastUpdateDate(date); 
		
		etyList.add(schematronEty); 
		
		List<SchematronDTO> dtoList = schematronService.buildDtoFromEty(etyList); 
		SchematronDTO firstElemInParsedList = dtoList.get(0); 
		
		assertEquals(ArrayList.class, dtoList.getClass()); 
		assertEquals(1, dtoList.size()); 
		
		assertEquals(SchematronDTO.class, firstElemInParsedList.getClass()); 
		assertEquals(String.class, firstElemInParsedList.getNameSchematron().getClass()); 
		assertEquals(String.class, firstElemInParsedList.getTemplateIdRoot().getClass()); 
		assertEquals(String.class, firstElemInParsedList.getTemplateIdExtension().getClass()); 
		assertEquals(Binary.class, firstElemInParsedList.getContentSchematron().getClass()); 
		assertEquals(Date.class, firstElemInParsedList.getInsertionDate().getClass()); 
		assertEquals(Date.class, firstElemInParsedList.getLastUpdateDate().getClass()); 

		assertEquals(TEST_SCHEMATRON_NAME, firstElemInParsedList.getNameSchematron()); 
		assertEquals(TEST_SCHEMATRON_ROOT, firstElemInParsedList.getTemplateIdRoot()); 
		assertEquals(TEST_SCHEMATRON_EXTENSION, firstElemInParsedList.getTemplateIdExtension()); 
		assertEquals(TEST_SCHEMATRON_CONTENT, firstElemInParsedList.getContentSchematron()); 
		assertEquals(date, firstElemInParsedList.getInsertionDate()); 
		assertEquals(date, firstElemInParsedList.getLastUpdateDate()); 
		
    } 
    
	@Test
	void parseEtyToDtoTest() {
		SchematronETY schematronEty = new SchematronETY(); 
		SchematronDTO parsedDto = new SchematronDTO(); 
		
		Date date = new Date(); 
		
		schematronEty.setId(TEST_SCHEMATRON_ID); 
		schematronEty.setNameSchematron(TEST_SCHEMATRON_NAME); 
		schematronEty.setTemplateIdRoot(TEST_SCHEMATRON_ROOT); 
		schematronEty.setTemplateIdExtension(TEST_SCHEMATRON_EXTENSION); 
		schematronEty.setContentSchematron(TEST_SCHEMATRON_CONTENT); 
		schematronEty.setInsertionDate(date); 
		schematronEty.setLastUpdateDate(date); 

		parsedDto = schematronService.parseEtyToDto(schematronEty); 
		
		
		assertEquals(SchematronDTO.class, parsedDto.getClass()); 
		assertEquals(String.class, parsedDto.getNameSchematron().getClass()); 
		assertEquals(String.class, parsedDto.getTemplateIdRoot().getClass()); 
		assertEquals(String.class, parsedDto.getTemplateIdExtension().getClass()); 
		assertEquals(Binary.class, parsedDto.getContentSchematron().getClass()); 
		assertEquals(Date.class, parsedDto.getInsertionDate().getClass()); 
		assertEquals(Date.class, parsedDto.getLastUpdateDate().getClass()); 

		assertEquals(TEST_SCHEMATRON_NAME, parsedDto.getNameSchematron()); 
		assertEquals(TEST_SCHEMATRON_ROOT, parsedDto.getTemplateIdRoot()); 
		assertEquals(TEST_SCHEMATRON_EXTENSION, parsedDto.getTemplateIdExtension()); 
		assertEquals(TEST_SCHEMATRON_CONTENT, parsedDto.getContentSchematron()); 
		assertEquals(date, parsedDto.getInsertionDate()); 
		assertEquals(date, parsedDto.getLastUpdateDate()); 

	} 
	
	@Test
	void parseDtoToEtyTest() {
		SchematronDTO schematronDto = new SchematronDTO(); 
		SchematronETY parsedEty = new SchematronETY(); 
		
		Date date = new Date(); 
		
		schematronDto.setNameSchematron(TEST_SCHEMATRON_NAME); 
		schematronDto.setTemplateIdRoot(TEST_SCHEMATRON_ROOT); 
		schematronDto.setTemplateIdExtension(TEST_SCHEMATRON_EXTENSION); 
		schematronDto.setContentSchematron(TEST_SCHEMATRON_CONTENT); 
		schematronDto.setInsertionDate(date); 
		schematronDto.setLastUpdateDate(date); 

		parsedEty = schematronService.parseDtoToEty(schematronDto); 
		
		
		assertEquals(SchematronETY.class, parsedEty.getClass()); 
		assertEquals(String.class, parsedEty.getNameSchematron().getClass()); 
		assertEquals(String.class, parsedEty.getTemplateIdRoot().getClass()); 
		assertEquals(String.class, parsedEty.getTemplateIdExtension().getClass()); 
		assertEquals(Binary.class, parsedEty.getContentSchematron().getClass()); 
		assertEquals(Date.class, parsedEty.getInsertionDate().getClass()); 
		assertEquals(Date.class, parsedEty.getLastUpdateDate().getClass()); 

		assertEquals(TEST_SCHEMATRON_NAME, parsedEty.getNameSchematron()); 
		assertEquals(TEST_SCHEMATRON_ROOT, parsedEty.getTemplateIdRoot()); 
		assertEquals(TEST_SCHEMATRON_EXTENSION, parsedEty.getTemplateIdExtension()); 
		assertEquals(TEST_SCHEMATRON_CONTENT, parsedEty.getContentSchematron()); 
		assertEquals(date, parsedEty.getInsertionDate()); 
		assertEquals(date, parsedEty.getLastUpdateDate()); 

	} 
	

    
    @AfterAll
    public void teardown() {
        this.dropTestSchema();
    } 
	

    
}
