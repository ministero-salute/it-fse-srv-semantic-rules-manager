/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.repository.entity;

import java.util.Date;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;
import lombok.NoArgsConstructor;

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
	 
}