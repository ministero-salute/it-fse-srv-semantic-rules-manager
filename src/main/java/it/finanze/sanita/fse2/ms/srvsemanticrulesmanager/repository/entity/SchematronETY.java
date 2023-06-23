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
package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.repository.entity;

import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.enums.SystemTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

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
	public static final String FIELD_SYSTEM = "system";

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

	@Field(name = FIELD_SYSTEM)
	private String system;
	
	@Field(name = FIELD_INSERTION_DATE)
	private Date insertionDate; 
	
	@Field(name = FIELD_LAST_UPDATE)
	private Date lastUpdateDate;
	
	@Field(name = FIELD_DELETED)
	private boolean deleted;

	public static SchematronETY fromMultipart(String root, String version, SystemTypeEnum system, MultipartFile file) throws IOException {
		SchematronETY sch = new SchematronETY();
		Date now = new Date();
		sch.setContentSchematron(new Binary(file.getBytes()));
		sch.setNameSchematron(file.getOriginalFilename());
		sch.setTemplateIdRoot(root);
		sch.setSystem(system.value());
		sch.setVersion(version);
		sch.setInsertionDate(now);
		sch.setLastUpdateDate(now);
		return sch;
	}

}