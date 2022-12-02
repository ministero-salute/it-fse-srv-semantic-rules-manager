/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto;

import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.repository.entity.SchematronETY;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.time.OffsetDateTime;

import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.utility.UtilsMisc.convertToOffsetDateTime;
import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.utility.UtilsMisc.encodeBase64;
import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.utility.ValidationUtility.DEFAULT_BINARY_MAX_SIZE;
import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.utility.ValidationUtility.DEFAULT_STRING_MAX_SIZE;

@Data
@NoArgsConstructor
public class SchematronDocumentDTO {

	@Size(max = DEFAULT_STRING_MAX_SIZE)
	private String id; 

	@Size(max = DEFAULT_BINARY_MAX_SIZE)
	private String contentSchematron;

	@Size(max = DEFAULT_STRING_MAX_SIZE)
	private String nameSchematron; 

	@Size(max = DEFAULT_STRING_MAX_SIZE)
	private String templateIdRoot; 

	@Size(max = DEFAULT_STRING_MAX_SIZE)
	private String version;
	
	@Size(max = DEFAULT_STRING_MAX_SIZE)
	private OffsetDateTime lastUpdateDate;

	@AllArgsConstructor
	public static class Options {
		private final boolean binary;
	}

	public static SchematronDocumentDTO fromEntity(SchematronETY e) {
		SchematronDocumentDTO out = new SchematronDocumentDTO();
		out.setId(e.getId());
		out.setNameSchematron(e.getNameSchematron());
		out.setContentSchematron(
			encodeBase64(e.getContentSchematron().getData())
		);
		out.setVersion(e.getVersion());
		out.setTemplateIdRoot(e.getTemplateIdRoot());
		out.setLastUpdateDate(
			convertToOffsetDateTime(e.getLastUpdateDate())
		);
		return out;
	}

	public SchematronDocumentDTO applyOptions(Options o) {
		if(!o.binary) contentSchematron = null;
		return this;
	}

}
