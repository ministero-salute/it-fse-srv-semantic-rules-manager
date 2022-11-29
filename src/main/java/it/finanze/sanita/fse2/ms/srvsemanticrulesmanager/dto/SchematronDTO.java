/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto;

import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.utility.ValidationUtility.DEFAULT_BINARY_MAX_SIZE;
import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.utility.ValidationUtility.DEFAULT_BINARY_MIN_SIZE;
import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.utility.ValidationUtility.DEFAULT_STRING_MAX_SIZE;
import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.utility.ValidationUtility.DEFAULT_STRING_MIN_SIZE;

import java.io.IOException;
import java.util.Date;

import javax.validation.constraints.Size;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class SchematronDTO
 {

	@Size(max = DEFAULT_STRING_MAX_SIZE)
	private String id; 

	@Size(max = DEFAULT_BINARY_MAX_SIZE)
	private Binary contentSchematron; 

	@Size(max = DEFAULT_STRING_MAX_SIZE)
	private String nameSchematron; 

	@Size(max = DEFAULT_STRING_MAX_SIZE)
	private String templateIdRoot; 

	@Size(max = DEFAULT_STRING_MAX_SIZE)
	private String version; 

	@Size(max = DEFAULT_STRING_MAX_SIZE)
	private Date insertionDate; 
	
	@Size(max = DEFAULT_STRING_MAX_SIZE)
	private Date lastUpdateDate; 
	
	@Size(max = DEFAULT_STRING_MAX_SIZE)
	private boolean deleted;

}
