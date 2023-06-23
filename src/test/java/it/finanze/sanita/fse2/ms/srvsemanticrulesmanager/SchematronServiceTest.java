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
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.repository.entity.SchematronETY;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.service.impl.SchematronSRV;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.utility.UtilsMisc;
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

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.utility.UtilsMisc.convertToOffsetDateTime;
import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.utility.UtilsMisc.encodeBase64;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ComponentScan
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(Constants.Profile.TEST)
class SchematronServiceTest extends AbstractTest {

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
		mongo.dropCollection(SchematronETY.class);
		populateSchematron();
    } 

	@AfterAll
    public void teardown() {
        mongo.dropCollection(SchematronETY.class);
    } 
    
    @Test
    void insertTest() throws DocumentNotFoundException, OperationException, DocumentAlreadyPresentException {
		SchematronETY dto = new SchematronETY();
    	
    	Date date = new Date(); 
    	
    	dto.setNameSchematron(TEST_SCHEMATRON_NAME_INS); 
    	dto.setTemplateIdRoot(TEST_SCHEMATRON_ROOT_INS); 
    	dto.setVersion(TEST_SCHEMATRON_EXTENSION_INS); 
    	dto.setContentSchematron(TEST_SCHEMATRON_CONTENT_INS);
    	dto.setInsertionDate(date); 
    	dto.setLastUpdateDate(date); 

    	schematronService.insert(dto); 
    	
    	SchematronDocumentDTO returnedDto = schematronService.findByTemplateIdRootAndVersion(TEST_SCHEMATRON_ROOT_INS, TEST_SCHEMATRON_EXTENSION_INS);
    	
    	assertEquals(SchematronDocumentDTO.class, returnedDto.getClass());
    	assertEquals(String.class, returnedDto.getContent().getClass());
    	assertEquals(String.class, returnedDto.getName().getClass()); 
    	assertEquals(String.class, returnedDto.getTemplateIdRoot().getClass()); 
    	assertEquals(String.class, returnedDto.getVersion().getClass()); 
    	assertEquals(OffsetDateTime.class, returnedDto.getLastUpdateDate().getClass());
    	
    	assertEquals(TEST_SCHEMATRON_NAME_INS, returnedDto.getName()); 
    	assertEquals(TEST_SCHEMATRON_ROOT_INS, returnedDto.getTemplateIdRoot()); 
    	assertEquals(TEST_SCHEMATRON_EXTENSION_INS, returnedDto.getVersion()); 
    	assertEquals(encodeBase64(TEST_SCHEMATRON_CONTENT_INS.getData()), returnedDto.getContent());
    	assertEquals(UtilsMisc.convertToOffsetDateTime(date), returnedDto.getLastUpdateDate());
    	 	
    } 
    
    @Test
    void insertSchematronIfPresent() throws OperationException, DocumentAlreadyPresentException, DocumentNotFoundException {
		SchematronETY dto = new SchematronETY();
    	
    	Date date = new Date(); 
    	
    	dto.setNameSchematron(TEST_SCHEMATRON_NAME_INS_THROW); 
    	dto.setTemplateIdRoot(TEST_SCHEMATRON_ROOT_INS_THROW); 
    	dto.setVersion(TEST_SCHEMATRON_EXTENSION_INS_THROW); 
    	dto.setContentSchematron(TEST_SCHEMATRON_CONTENT_INS_THROW); 
    	dto.setInsertionDate(date); 
    	dto.setLastUpdateDate(date); 

    	schematronService.insert(dto);

		SchematronETY dtoNew = new SchematronETY();
    	
    	
    	dtoNew.setNameSchematron(TEST_SCHEMATRON_NAME_INS_THROW); 
    	dtoNew.setTemplateIdRoot(TEST_SCHEMATRON_ROOT_INS_THROW); 
    	dtoNew.setVersion(TEST_SCHEMATRON_EXTENSION_INS_THROW); 
    	dtoNew.setContentSchematron(TEST_SCHEMATRON_CONTENT_INS_THROW);
    	dtoNew.setInsertionDate(date); 
    	dtoNew.setLastUpdateDate(date); 
    	
    	 	
    	assertThrows(Exception.class, () -> {
    		schematronService.insert(dtoNew); 
    	}); 

    }

    @Test
    void deleteSchematron() throws DocumentNotFoundException, OperationException, DocumentAlreadyPresentException {
		SchematronETY dto = new SchematronETY();
    	
    	Date date = new Date(); 
    	
    	dto.setNameSchematron(TEST_SCHEMATRON_NAME_DEL); 
    	dto.setTemplateIdRoot(TEST_SCHEMATRON_ROOT_DEL); 
    	dto.setVersion(TEST_SCHEMATRON_EXTENSION_DEL); 
    	dto.setContentSchematron(TEST_SCHEMATRON_CONTENT_DEL); 
    	dto.setInsertionDate(date); 
    	dto.setLastUpdateDate(date); 

    	schematronService.insert(dto); 
    	
    	schematronService.deleteSchematron(TEST_SCHEMATRON_ROOT_DEL, null);
    	
    	assertThrows(DocumentNotFoundException.class, () -> schematronService.findByTemplateIdRootAndVersion(
    			TEST_SCHEMATRON_ROOT_DEL, TEST_SCHEMATRON_EXTENSION_DEL)); 

    }
    
    
    @Test
    void findTest() throws Exception {
		SchematronETY dto = new SchematronETY();
    	
    	Date date = new Date(); 
    	
    	dto.setNameSchematron(TEST_SCHEMATRON_NAME_QUERY); 
    	dto.setTemplateIdRoot(TEST_SCHEMATRON_ROOT_QUERY); 
    	dto.setVersion(TEST_SCHEMATRON_EXTENSION_QUERY); 
    	dto.setContentSchematron(TEST_SCHEMATRON_CONTENT_QUERY); 
    	dto.setInsertionDate(date); 
    	dto.setLastUpdateDate(date); 

    	schematronService.insert(dto); 

		// FIND BY TEMPLATE ID ROOT AND TEMPLATE ID EXTENSION

    	SchematronDocumentDTO returnedDto = schematronService.findByTemplateIdRootAndVersion(TEST_SCHEMATRON_ROOT_QUERY, TEST_SCHEMATRON_EXTENSION_QUERY);
    	
    	
    	assertEquals(SchematronDocumentDTO.class, returnedDto.getClass());
    	assertEquals(String.class, returnedDto.getContent().getClass());
    	assertEquals(String.class, returnedDto.getName().getClass()); 
    	assertEquals(String.class, returnedDto.getTemplateIdRoot().getClass()); 
    	assertEquals(String.class, returnedDto.getVersion().getClass()); 
    	assertEquals(OffsetDateTime.class, returnedDto.getLastUpdateDate().getClass());
    	
    	assertEquals(TEST_SCHEMATRON_NAME_QUERY, returnedDto.getName()); 
    	assertEquals(TEST_SCHEMATRON_ROOT_QUERY, returnedDto.getTemplateIdRoot()); 
    	assertEquals(TEST_SCHEMATRON_EXTENSION_QUERY, returnedDto.getVersion()); 
    	assertEquals(encodeBase64(TEST_SCHEMATRON_CONTENT_DEL.getData()), returnedDto.getContent());
    	assertEquals(convertToOffsetDateTime(date), returnedDto.getLastUpdateDate());


		// FIND BY ID

    	SchematronDocumentDTO returnedDto_1 = schematronService.findById(returnedDto.getId());
		assertNotNull(returnedDto_1); 
    	
		
		// FIND BY ID - Invalid ID 
		
    	
    	
		// FIND BY ID - Non Existent ID 
    	
    	assertThrows(DocumentNotFoundException.class, () -> schematronService.findById(TEST_SCHEMATRON_ID_NOT_FOUND.toString())); 
    	
		
    }
    
    @Test
    void findByTemplateIdRootAndTemplateIdExtensionDeletedElementTest() throws DocumentNotFoundException, OperationException, DocumentAlreadyPresentException {
		SchematronETY dto = new SchematronETY();
    	
    	Date date = new Date(); 
    	
    	dto.setNameSchematron(TEST_SCHEMATRON_NAME_QUERY_DEL); 
    	dto.setTemplateIdRoot(TEST_SCHEMATRON_ROOT_QUERY_DEL); 
    	dto.setVersion(TEST_SCHEMATRON_EXTENSION_QUERY_DEL); 
    	dto.setContentSchematron(TEST_SCHEMATRON_CONTENT_QUERY_DEL); 
    	dto.setInsertionDate(date); 
    	dto.setLastUpdateDate(date); 

    	schematronService.insert(dto); 
    	
    	schematronService.deleteSchematron(TEST_SCHEMATRON_ROOT_QUERY_DEL, null);

    	assertThrows(DocumentNotFoundException.class, () -> schematronService.findByTemplateIdRootAndVersion(TEST_SCHEMATRON_ROOT_QUERY_DEL, TEST_SCHEMATRON_EXTENSION_QUERY_DEL)); 

    } 
    
    
    @Test
    void getSchematronsTest() throws OperationException, DocumentAlreadyPresentException, DocumentNotFoundException {

		SchematronETY dtoFirst = new SchematronETY();
    	
    	Date date = new Date(); 
    	
    	dtoFirst.setNameSchematron(TEST_SCHEMATRON_NAME_INS_3); 
    	dtoFirst.setTemplateIdRoot(TEST_SCHEMATRON_ROOT_INS_3); 
    	dtoFirst.setVersion(TEST_SCHEMATRON_EXTENSION_INS_3); 
    	dtoFirst.setContentSchematron(TEST_SCHEMATRON_CONTENT_INS_3); 
    	dtoFirst.setInsertionDate(date); 
    	dtoFirst.setLastUpdateDate(date); 
   	
    	
    	schematronService.insert(dtoFirst); 
    	
    	List<SchematronDocumentDTO> dtoGetList = schematronService.getSchematrons(false);
    	SchematronDocumentDTO dtoElem = dtoGetList.get(0);
    	
    	assertEquals(ArrayList.class, dtoGetList.getClass()); 
    	assertEquals(SchematronDocumentDTO.class, dtoElem.getClass());
    	
    	assertTrue(dtoGetList.size() > 0); 
    	
    	assertEquals(SchematronDocumentDTO.class, dtoElem.getClass());
    	assertEquals(String.class, dtoElem.getContent().getClass());
    	assertEquals(String.class, dtoElem.getName().getClass()); 
    	assertEquals(String.class, dtoElem.getTemplateIdRoot().getClass()); 
    	assertEquals(String.class, dtoElem.getVersion().getClass()); 
    	assertEquals(OffsetDateTime.class, dtoElem.getLastUpdateDate().getClass());
    	
    }
    
}
