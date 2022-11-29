/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.repository.entity;

import java.io.IOException;
import java.util.Date;

import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.SchematronDTO;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * Model to save schematron.
 */
@Data
@NoArgsConstructor
@Document(collection = "#{@schematronBean}")
public class SchematronETY {

	@Id
	private String id;
	
	@Field(name = "content_schematron")
	private Binary contentSchematron;

	@Field(name = "name_schematron")
	private String nameSchematron;

	@Field(name = "template_id_root")
	private String templateIdRoot;
	
	@Field(name = "version")
	private String version;
	
	@Field(name = "insertion_date")
	private Date insertionDate; 
	
	@Field(name = "last_update_date")
	private Date lastUpdateDate; 
	
	@Field(name = "deleted")
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