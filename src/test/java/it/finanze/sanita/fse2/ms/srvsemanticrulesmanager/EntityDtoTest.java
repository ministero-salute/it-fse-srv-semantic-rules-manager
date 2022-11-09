/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.base.AbstractTest;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.ChangeSetDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.SchematronDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.repository.entity.SchematronETY;

@SpringBootTest
@ComponentScan
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(Constants.Profile.TEST)
class EntityDtoTest extends AbstractTest {


    private final String TEST_SCHEMATRON_ID = "as63hFd3aM"; 
    private final String TEST_SCHEMATRON_NAME = "NAME_SCHEMATRON"; 
    private final String TEST_SCHEMATRON_ROOT = "ROOT_SCHEMATRON"; 
    private final String TEST_SCHEMATRON_EXT = "EXT_SCHEMATRON"; 
    private final Binary TEST_SCHEMATRON_CONTENT = new Binary(BsonBinarySubType.BINARY, SCHEMATRON_TEST_STRING.getBytes());  

    
    // Test Cases 
    @Test
    void createSchematronDto() {
    	SchematronDTO schematron = new SchematronDTO(); 
    	Date dateNow = new Date(); 
    	
    	schematron.setNameSchematron(TEST_SCHEMATRON_NAME); 
    	schematron.setContentSchematron(TEST_SCHEMATRON_CONTENT); 
    	schematron.setTemplateIdRoot(TEST_SCHEMATRON_ROOT); 
    	schematron.setVersion(TEST_SCHEMATRON_EXT); 
    	schematron.setInsertionDate(dateNow); 
    	schematron.setLastUpdateDate(dateNow); 
    	
    	
    	assertEquals(schematron.getClass(), SchematronDTO.class); 
    	
    	assertEquals(String.class, schematron.getNameSchematron().getClass()); 
    	assertEquals(Binary.class, schematron.getContentSchematron().getClass()); 
    	assertEquals(String.class, schematron.getTemplateIdRoot().getClass()); 
    	assertEquals(String.class, schematron.getVersion().getClass()); 
    	assertEquals(Date.class, schematron.getInsertionDate().getClass()); 
    	assertEquals(Date.class, schematron.getLastUpdateDate().getClass()); 
    	
    	assertEquals(TEST_SCHEMATRON_NAME, schematron.getNameSchematron()); 
    	assertEquals(TEST_SCHEMATRON_CONTENT, schematron.getContentSchematron()); 
    	assertEquals(TEST_SCHEMATRON_ROOT, schematron.getTemplateIdRoot()); 
    	assertEquals(TEST_SCHEMATRON_EXT, schematron.getVersion()); 
    	assertEquals(dateNow, schematron.getInsertionDate()); 
    	assertEquals(dateNow, schematron.getLastUpdateDate()); 


    	
    } 
    
    @Test
    void createSchematronEty() {
    	SchematronETY schematron = new SchematronETY(); 
    	Date dateNow = new Date(); 
    	
    	schematron.setId(TEST_SCHEMATRON_ID);
    	schematron.setNameSchematron(TEST_SCHEMATRON_NAME); 
    	schematron.setContentSchematron(TEST_SCHEMATRON_CONTENT); 
    	schematron.setTemplateIdRoot(TEST_SCHEMATRON_ROOT); 
    	schematron.setVersion(TEST_SCHEMATRON_EXT); 
    	schematron.setInsertionDate(dateNow); 
    	schematron.setLastUpdateDate(dateNow); 
    	
    	assertEquals(SchematronETY.class, schematron.getClass()); 
    	assertEquals(String.class, schematron.getId().getClass()); 
    	assertEquals(String.class, schematron.getNameSchematron().getClass()); 
    	assertEquals(Binary.class, schematron.getContentSchematron().getClass()); 
    	assertEquals(String.class, schematron.getTemplateIdRoot().getClass()); 
    	assertEquals(String.class, schematron.getVersion().getClass()); 
    	assertEquals(Date.class, schematron.getInsertionDate().getClass()); 
    	assertEquals(Date.class, schematron.getLastUpdateDate().getClass()); 
    	
    	assertEquals(TEST_SCHEMATRON_ID, schematron.getId()); 
    	assertEquals(TEST_SCHEMATRON_NAME, schematron.getNameSchematron()); 
    	assertEquals(TEST_SCHEMATRON_CONTENT, schematron.getContentSchematron()); 
    	assertEquals(TEST_SCHEMATRON_ROOT, schematron.getTemplateIdRoot()); 
    	assertEquals(TEST_SCHEMATRON_EXT, schematron.getVersion()); 
    	assertEquals(dateNow, schematron.getInsertionDate()); 
    	assertEquals(dateNow, schematron.getLastUpdateDate()); 

    	
    } 
    
    @Test
    void createChangeSetDto() {
    	ChangeSetDTO changeset = new ChangeSetDTO(); 
    	
    	changeset.setId("id"); 
    	changeset.setDescription(new ChangeSetDTO.Payload("templateIdRoot", "version"));
    	
    	assertEquals(String.class, changeset.getId().getClass()); 
    	assertEquals(ChangeSetDTO.Payload.class, changeset.getDescription().getClass()); 
    	
    	assertEquals("id", changeset.getId()); 
    	assertEquals("templateIdRoot", changeset.getDescription().getTemplateIdRoot()); 
		assertEquals("version", changeset.getDescription().getVersion());
    	
    }
    

    	
 }


