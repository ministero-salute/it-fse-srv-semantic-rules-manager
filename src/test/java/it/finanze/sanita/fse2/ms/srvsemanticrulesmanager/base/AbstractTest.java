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
package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.base;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.mongodb.core.MongoTemplate;

import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.repository.entity.SchematronETY;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.repository.mongo.impl.SchematronRepo;



public abstract class AbstractTest {

	@Autowired
	protected SchematronRepo repository;


	/**
	 * This is a test collection initialized on MongoDB 
	 */
	public static final String SCHEMATRON_TEST_NAME_A = "test_schematron_A";
	public static final String SCHEMATRON_TEST_NAME_B = "test_schematron_B";
	public static final String SCHEMATRON_TEST_NAME_C = "test_schematron_C";
	public static final String SCHEMATRON_TEST_NAME_D = "test_schematron_D"; 
	public static final String SCHEMATRON_TEST_NAME_E = "test_schematron_E"; 


	public static final String SCHEMATRON_TEST_ROOT_A = "Root_A";
	public static final String SCHEMATRON_TEST_ROOT_B = "Root_B";
	public static final String SCHEMATRON_TEST_ROOT_C = "Root_C";
	public static final String SCHEMATRON_TEST_ROOT_D = "Root_D"; 
	public static final String SCHEMATRON_TEST_ROOT_E = "Root_E";

	public static final String SCHEMATRON_TEST_EXT_A = "Ext_A";
	public static final String SCHEMATRON_TEST_EXT_B = "Ext_B";
	public static final String SCHEMATRON_TEST_EXT_C = "Ext_C";
	public static final String SCHEMATRON_TEST_EXT_D = "Ext_D";
	public static final String SCHEMATRON_TEST_EXT_E = "Ext_E";

	public static final String SCHEMATRON_TEST_STRING = "Hello World!";
	public static final String SCHEMATRON_TEST_STRING_UPDATED = "Hello World, folks!";



	/**
	 * This collection does not exist
	 */
	public static final String SCHEMATRON_TEST_FAKE_NAME = "test_schematron_F";

	/**
	 * Sample parameter for multiple tests
	 */
	public static final String SCHEMATRON_TEST_NAME = "name_schematron";
	/**
	 * Sample parameter for multiple tests
	 */
	public static final String SCHEMATRON_TEST_ROOT = "test_root";
	/**
	 * Sample parameter for multiple tests
	 */
	public static final String SCHEMATRON_TEST_EXTENSION = "test_extension";
	/**
	 * Directory containing sample files to upload as test
	 */
	public static final Path SCHEMATRON_SAMPLE_FILES = Paths.get(
			"src",
			"test",
			"resources",
			"schematron",
			"files",
			"standard");
	/**
	 * Directory containing modified files used to replace sample ones
	 */
	public static final Path SCHEMATRON_MOD_SAMPLE_FILES = Paths.get(
			"src",
			"test",
			"resources",
			"schematron",
			"files",
			"modified");

	@SpyBean
	public MongoTemplate mongo;


	protected AbstractTest() {		

	}

	protected void populateSchematron() {

		SchematronETY schematronA = new SchematronETY(); 
		schematronA.setNameSchematron(SCHEMATRON_TEST_NAME_A); 
		schematronA.setContentSchematron(new Binary(BsonBinarySubType.BINARY, SCHEMATRON_TEST_STRING.getBytes()));
		schematronA.setTemplateIdRoot(SCHEMATRON_TEST_ROOT_A); 
		schematronA.setVersion(SCHEMATRON_TEST_EXT_A); 
		schematronA.setInsertionDate(new Date()); 
		schematronA.setLastUpdateDate(new Date()); 

		SchematronETY schematronB = new SchematronETY(); 
		schematronB.setNameSchematron(SCHEMATRON_TEST_NAME_B); 
		schematronB.setContentSchematron(new Binary(BsonBinarySubType.BINARY, SCHEMATRON_TEST_STRING.getBytes()));
		schematronB.setTemplateIdRoot(SCHEMATRON_TEST_ROOT_B); 
		schematronB.setVersion(SCHEMATRON_TEST_EXT_B);
		schematronB.setInsertionDate(new Date()); 
		schematronB.setLastUpdateDate(new Date()); 

		SchematronETY schematronC = new SchematronETY(); 
		schematronC.setNameSchematron(SCHEMATRON_TEST_NAME_C); 
		schematronC.setContentSchematron(new Binary(BsonBinarySubType.BINARY, SCHEMATRON_TEST_STRING.getBytes()));
		schematronC.setTemplateIdRoot(SCHEMATRON_TEST_ROOT_C); 
		schematronC.setVersion(SCHEMATRON_TEST_EXT_C); 
		schematronC.setInsertionDate(new Date()); 
		schematronC.setLastUpdateDate(new Date()); 

		mongo.insert(schematronA);
		mongo.insert(schematronB);
		mongo.insert(schematronC);


	}
}
