/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.base.AbstractTest;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.enums.ErrorLogEnum;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.enums.OperationLogEnum;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.enums.ResultLogEnum;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.enums.UIDModeEnum;

@SpringBootTest
@ComponentScan
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(Constants.Profile.TEST)
class EnumTest extends AbstractTest {
	
	@Test
	void errorLogEnumTest() {
		ErrorLogEnum errorLogEnum = ErrorLogEnum.KO_SCHEMATRON_CREATE; 
		
		assertEquals(ErrorLogEnum.class, errorLogEnum.getClass()); 
		assertEquals(String.class, errorLogEnum.getCode().getClass()); 
		assertEquals(String.class, errorLogEnum.getDescription().getClass()); 

		assertEquals("KO-SCHEMATRON-CREATE", errorLogEnum.getCode()); 
		assertEquals("Error while creating Schematron", errorLogEnum.getDescription()); 

	} 
	
	@Test
	void operationLogEnumTest() {
		OperationLogEnum operationLogEnum = OperationLogEnum.POST_SCHEMATRON; 
		
		assertEquals(OperationLogEnum.class, operationLogEnum.getClass()); 
		assertEquals(String.class, operationLogEnum.getCode().getClass()); 
		assertEquals(String.class, operationLogEnum.getDescription().getClass()); 

		assertEquals("POST-SCHEMATRON", operationLogEnum.getCode()); 
		assertEquals("Aggiunta Schematron", operationLogEnum.getDescription()); 

	} 
	
	@Test
	void resultLogEnumTest() {
		ResultLogEnum resultLogEnum = ResultLogEnum.OK; 
		
		assertEquals(ResultLogEnum.class, resultLogEnum.getClass()); 
		assertEquals(String.class, resultLogEnum.getCode().getClass()); 
		assertEquals(String.class, resultLogEnum.getDescription().getClass()); 

		assertEquals("OK", resultLogEnum.getCode()); 
		assertEquals("Operazione eseguita con successo", resultLogEnum.getDescription()); 

	} 
	
	@Test
	void uidModeEnumTest() {
		UIDModeEnum uidModeEnum = UIDModeEnum.UUID_UUID; 
		
		assertEquals(UIDModeEnum.class, uidModeEnum.getClass()); 
		assertEquals(Integer.class, uidModeEnum.getId().getClass()); 


	} 
	
}
