package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.base.AbstractTest;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.repository.entity.SchematronETY;

@SpringBootTest
@ComponentScan
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(Constants.Profile.TEST)
class SchematronRepositoryTest extends AbstractTest {
    
    // Test Data 
	private final String TEST_ID_NAME = "test_schematron_A"; 
    private final String TEST_ID_ROOT = "Root_A"; 
    private final String TEST_ID_EXTENSION = "Ext_A"; 
    
    private final String TEST_INS_SCHEMATRON = "Root_E"; 
    private final String TEST_INS_ROOT = "Root_E"; 
    private final String TEST_INS_EXTENSION = "Ext_E"; 
    
    private final String TEST_UPD_SCHEMATRON = "Root_UPD"; 
    private final String TEST_UPD_ROOT = "Root_UPD"; 
    private final String TEST_UPD_EXTENSION = "Ext_UPD";
	
	private final String TEST_UPD_SCHEMATRON_NE = "Not Exist"; 
    private final String TEST_UPD_ROOT_NE = "Not Exist"; 
    private final String TEST_UPD_EXTENSION_NE = "Not Exist";
    
    private final String TEST_DEL_SCHEMATRON = "Root_DEL"; 
    private final String TEST_DEL_ROOT = "Root_DEL"; 
    private final String TEST_DEL_EXTENSION = "Ext_DEL"; 
    
    
    
    
    @BeforeAll
    public void setup() throws Exception {
        this.initTestRepository();
    } 

    
    @Test
    void insertTest() throws Exception {
    	SchematronETY ety = new SchematronETY(); 
    	
    	ety.setNameSchematron(TEST_INS_SCHEMATRON);
    	ety.setContentSchematron(new Binary(BsonBinarySubType.BINARY, SCHEMATRON_TEST_STRING.getBytes()));
    	ety.setTemplateIdRoot(TEST_INS_ROOT);
    	ety.setVersion(TEST_INS_EXTENSION); 
    	ety.setInsertionDate(new Date()); 
    	ety.setLastUpdateDate(new Date()); 

    	repository.insert(ety); 
    	
    	SchematronETY retrievedEty = repository.findByTemplateIdRootAndTemplateIdExtension(TEST_INS_ROOT, TEST_INS_EXTENSION); 
    	
    	assertEquals(SchematronETY.class, retrievedEty.getClass()); 
    	assertEquals(Binary.class, retrievedEty.getContentSchematron().getClass()); 
    	assertEquals(String.class, retrievedEty.getNameSchematron().getClass()); 
    	assertEquals(String.class, retrievedEty.getTemplateIdRoot().getClass()); 
    	assertEquals(String.class, retrievedEty.getVersion().getClass()); 
    	assertEquals(Date.class, retrievedEty.getInsertionDate().getClass()); 
    	assertEquals(Date.class, retrievedEty.getLastUpdateDate().getClass()); 
    	
    	assertEquals(TEST_INS_SCHEMATRON, retrievedEty.getNameSchematron()); 
    	assertEquals(TEST_INS_ROOT, retrievedEty.getTemplateIdRoot()); 
    	assertEquals(TEST_INS_EXTENSION, retrievedEty.getVersion()); 

    } 
    
    @Test
    void updateTest() throws Exception {
    	
    	// Inserts the Schematron to Update 
    	SchematronETY etyToUpdate = new SchematronETY(); 
    	
    	etyToUpdate.setNameSchematron(TEST_UPD_SCHEMATRON);
    	etyToUpdate.setContentSchematron(new Binary(BsonBinarySubType.BINARY, SCHEMATRON_TEST_STRING.getBytes()));
    	etyToUpdate.setTemplateIdRoot(TEST_UPD_ROOT);
    	etyToUpdate.setVersion(TEST_UPD_EXTENSION);
    	etyToUpdate.setInsertionDate(new Date());
    	etyToUpdate.setLastUpdateDate(new Date());

    	repository.insert(etyToUpdate); 
    	
    	
    	// Creates new updated schematron and updates the Schematron
    	SchematronETY ety = new SchematronETY(); 
    	
    	ety.setNameSchematron(TEST_UPD_SCHEMATRON);
    	ety.setContentSchematron(new Binary(BsonBinarySubType.BINARY, SCHEMATRON_TEST_STRING_UPDATED.getBytes()));
    	ety.setTemplateIdRoot(TEST_UPD_ROOT);
    	ety.setVersion(TEST_UPD_EXTENSION); 
    	ety.setInsertionDate(new Date()); 
    	ety.setLastUpdateDate(new Date());

    	String updatedBinaryData = ety.getContentSchematron().toString(); 
    	
    	assertTrue(repository.update(ety)); 
    	
    	SchematronETY retrievedEty = repository.findByTemplateIdRootAndTemplateIdExtension(TEST_UPD_ROOT, TEST_UPD_EXTENSION); 
    	
    	assertEquals(Binary.class, retrievedEty.getContentSchematron().getClass()); 

    	assertEquals(0, retrievedEty.getContentSchematron().getType()); 
    	assertEquals(updatedBinaryData, retrievedEty.getContentSchematron().toString()); 

		/// assert not exist

		SchematronETY ety1 = new SchematronETY(); 
    	
    	ety1.setNameSchematron(TEST_UPD_SCHEMATRON_NE);
    	ety1.setContentSchematron(new Binary(BsonBinarySubType.BINARY, SCHEMATRON_TEST_STRING_UPDATED.getBytes()));
    	ety1.setTemplateIdRoot(TEST_UPD_ROOT_NE);
    	ety1.setVersion(TEST_UPD_EXTENSION_NE); 
    	ety1.setInsertionDate(new Date()); 
    	ety1.setLastUpdateDate(new Date());

		assertFalse(repository.update(ety1));

    } 
    
    @Test
    void removeSchematronTest() throws Exception {
    	
    	// Inserts the Schematron to Update 
    	SchematronETY etyToDelete = new SchematronETY(); 
    	
    	etyToDelete.setNameSchematron(TEST_DEL_SCHEMATRON);
    	etyToDelete.setContentSchematron(new Binary(BsonBinarySubType.BINARY, SCHEMATRON_TEST_STRING.getBytes()));
    	etyToDelete.setTemplateIdRoot(TEST_DEL_ROOT);
    	etyToDelete.setVersion(TEST_DEL_EXTENSION); 
    	etyToDelete.setInsertionDate(new Date()); 
    	etyToDelete.setLastUpdateDate(new Date());

    	repository.insert(etyToDelete); 
   
    	repository.removeSchematron(TEST_DEL_ROOT, TEST_DEL_EXTENSION); 
   
    	SchematronETY retrievedEty = repository.findByTemplateIdRootAndTemplateIdExtension(TEST_DEL_ROOT, TEST_DEL_EXTENSION); 	

    	assertNull(retrievedEty.getNameSchematron()); 
    	assertNull(retrievedEty.getTemplateIdRoot()); 
    	assertNull(retrievedEty.getVersion()); 
    	assertNull(retrievedEty.getInsertionDate()); 
    	assertNull(retrievedEty.getLastUpdateDate()); 

    } 
    
    @Test
    void findByTemplateIdRootAndTemplateIdExtensionTest() throws Exception {
    	// Inserts the Schematron to Update 
    	SchematronETY etyToInsert = new SchematronETY(); 
    	
    	etyToInsert.setNameSchematron(TEST_ID_NAME);
    	etyToInsert.setContentSchematron(new Binary(BsonBinarySubType.BINARY, SCHEMATRON_TEST_STRING.getBytes()));
    	etyToInsert.setTemplateIdRoot(TEST_ID_ROOT);
    	etyToInsert.setVersion(TEST_ID_EXTENSION); 
    	etyToInsert.setInsertionDate(new Date()); 
    	etyToInsert.setLastUpdateDate(new Date());

    	repository.insert(etyToInsert); 
    	
    	SchematronETY ety = repository.findByTemplateIdRootAndTemplateIdExtension(TEST_ID_ROOT, TEST_ID_EXTENSION); 
    	
    	
    	assertEquals(SchematronETY.class, ety.getClass()); 
    	assertEquals(Binary.class, ety.getContentSchematron().getClass()); 
    	assertEquals(String.class, ety.getNameSchematron().getClass()); 
    	assertEquals(String.class, ety.getTemplateIdRoot().getClass()); 
    	assertEquals(String.class, ety.getVersion().getClass()); 
    	assertEquals(Date.class, ety.getInsertionDate().getClass()); 
    	assertEquals(Date.class, ety.getLastUpdateDate().getClass()); 
    	
    	assertEquals("test_schematron_A", ety.getNameSchematron()); 
    	assertEquals("Root_A", ety.getTemplateIdRoot()); 
    	assertEquals( "Ext_A", ety.getVersion()); 

    }
    
   
    
    @Test
    void findAllTest() throws Exception {
    	List<SchematronETY> etyList = repository.findAll(); 
    	
    	SchematronETY ety = etyList.get(0); 

    	assertEquals(etyList.getClass(), ArrayList.class); 

    	assertEquals(SchematronETY.class, ety.getClass()); 
    	assertEquals(Binary.class, ety.getContentSchematron().getClass()); 
    	assertEquals(String.class, ety.getNameSchematron().getClass()); 
    	assertEquals(String.class, ety.getTemplateIdRoot().getClass()); 
    	assertEquals(String.class, ety.getVersion().getClass()); 
    	assertEquals(Date.class, ety.getInsertionDate().getClass()); 
    
    }

    
    @Test
    void existByTemplateIdRootTest() throws Exception {
    	boolean existsEty = repository.existByTemplateIdRoot(TEST_ID_ROOT); 
    	
    	assertEquals(true, existsEty); 

    } 
    
    @Test
    void getCollectionNameTest() {
    	String testDB = repository.getCollectionName(); 
    	
    	assertEquals(String.class, testDB.getClass()); 
    	assertEquals("test-schematron", testDB); 
    }
    
    
    @AfterAll
    public void teardown() {
        this.dropTestSchema();
    } 
    
}