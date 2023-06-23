/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 * 
 * Copyright (C) 2023 Ministero della Salute
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager;

import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.base.AbstractTest;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.SchematronDocumentDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.changes.ChangeSetDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.repository.entity.SchematronETY;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import java.time.OffsetDateTime;
import java.util.Date;

import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.utility.UtilsMisc.convertToOffsetDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
    	SchematronDocumentDTO schematron = new SchematronDocumentDTO();
    	Date dateNow = new Date();
    	
    	schematron.setName(TEST_SCHEMATRON_NAME); 
    	schematron.setContent(SCHEMATRON_TEST_STRING);
    	schematron.setTemplateIdRoot(TEST_SCHEMATRON_ROOT); 
    	schematron.setVersion(TEST_SCHEMATRON_EXT); 
    	schematron.setLastUpdateDate(convertToOffsetDateTime(dateNow));
//    	schematron.setInsertionDate(convertToOffsetDateTime(dateNow));
//    	schematron.setDeleted(false);
    	
    	
    	assertEquals(schematron.getClass(), SchematronDocumentDTO.class);
    	
    	assertEquals(String.class, schematron.getName().getClass()); 
    	assertEquals(String.class, schematron.getContent().getClass());
    	assertEquals(String.class, schematron.getTemplateIdRoot().getClass()); 
    	assertEquals(String.class, schematron.getVersion().getClass()); 
    	assertEquals(OffsetDateTime.class, schematron.getLastUpdateDate().getClass());
    	assertEquals(String.class, schematron.getVersion().getClass()); 

    	
    	assertEquals(TEST_SCHEMATRON_NAME, schematron.getName()); 
    	assertEquals(new String(TEST_SCHEMATRON_CONTENT.getData()), schematron.getContent());
    	assertEquals(TEST_SCHEMATRON_ROOT, schematron.getTemplateIdRoot()); 
    	assertEquals(TEST_SCHEMATRON_EXT, schematron.getVersion()); 
    	assertEquals(convertToOffsetDateTime(dateNow), schematron.getLastUpdateDate());
    	
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


