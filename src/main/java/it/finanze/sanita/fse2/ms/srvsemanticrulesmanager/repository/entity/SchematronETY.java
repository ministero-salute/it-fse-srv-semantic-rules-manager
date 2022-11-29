/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.repository.entity;

import java.io.IOException;
import java.util.Date;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.repository.IChangeSetRepo.*;

/**
 * Model to save schematron.
 */
@Data
@NoArgsConstructor
@Document(collection = "#{@schematronBean}")
public class SchematronETY {

	public static final String FIELD_ID = "_id";
	public static final String FIELD_CONTENT = "content_schematron";
	public static final String FIELD_NAME = "name_schematron";
	public static final String FIELD_TEMPLATE_ID_ROOT = "template_id_root";
	public static final String FIELD_VERSION = "version";

	@Id
	private String id;
	
	@Field(name = FIELD_CONTENT)
	private Binary contentSchematron;

	@Field(name = FIELD_NAME)
	private String nameSchematron;

	@Field(name = FIELD_TEMPLATE_ID_ROOT)
	private String templateIdRoot;
	
	@Field(name = FIELD_VERSION)
	private String version;
	
	@Field(name = FIELD_INSERTION_DATE)
	private Date insertionDate; 
	
	@Field(name = FIELD_LAST_UPDATE)
	private Date lastUpdateDate; 
	
	@Field(name = FIELD_DELETED)
	private boolean deleted;

	public static SchematronETY fromMultipart(String templateIdRoot, String version, MultipartFile file) throws IOException {
		SchematronETY sch = new SchematronETY();
		Date timestamp = new Date();
		sch.setContentSchematron(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
		sch.setNameSchematron(file.getOriginalFilename());
		sch.setTemplateIdRoot(templateIdRoot);
		sch.setVersion(version);
		sch.setInsertionDate(timestamp);
		sch.setLastUpdateDate(timestamp);
		return sch;
	}

}