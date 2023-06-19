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
	
	@Size(max = DEFAULT_STRING_MAX_SIZE)
	private String name;
	
	@Size(max = DEFAULT_STRING_MAX_SIZE)
	private String templateIdRoot; 
	
	@Size(max = DEFAULT_STRING_MAX_SIZE)
	private String version;

	@Size(max = DEFAULT_STRING_MAX_SIZE)
	private OffsetDateTime insertionDate;
	
	@Size(max = DEFAULT_STRING_MAX_SIZE)
	private OffsetDateTime lastUpdateDate;

	private boolean deleted;

	@Size(max = DEFAULT_BINARY_MAX_SIZE)
	private String content;

	@AllArgsConstructor
	public static class Options {
		private final boolean binary;
	}

	public static SchematronDocumentDTO fromEntity(SchematronETY e) {
		SchematronDocumentDTO out = new SchematronDocumentDTO();
		out.setId(e.getId());
		out.setName(e.getNameSchematron());
		out.setContent(
			encodeBase64(e.getContentSchematron().getData())
		);
		out.setVersion(e.getVersion());
		out.setTemplateIdRoot(e.getTemplateIdRoot());
		out.setLastUpdateDate(
			convertToOffsetDateTime(e.getLastUpdateDate())
		);
		out.setInsertionDate(convertToOffsetDateTime(e.getInsertionDate()));
		out.setDeleted(e.isDeleted());
		return out;
	}

	public SchematronDocumentDTO applyOptions(Options o) {
		if(!o.binary) content = null;
		return this;
	}

}
