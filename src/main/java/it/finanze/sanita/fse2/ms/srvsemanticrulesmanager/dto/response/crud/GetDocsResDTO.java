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
package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.crud;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.SchematronDocumentDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.ResponseDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.log.LogTraceInfoDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.stream.Collectors;

import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.SchematronDocumentDTO.Options;
import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.utility.ValidationUtility.DEFAULT_ARRAY_MAX_SIZE;
import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.utility.ValidationUtility.DEFAULT_ARRAY_MIN_SIZE;

/**
 * 
 * The Class SchematronsDTO, used to return the list of all schematrons.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GetDocsResDTO extends ResponseDTO {

	private long numberOfItems;

	@ArraySchema(minItems = DEFAULT_ARRAY_MIN_SIZE, maxItems = DEFAULT_ARRAY_MAX_SIZE, uniqueItems = true)
	private List<SchematronDocumentDTO> items;


	public GetDocsResDTO(LogTraceInfoDTO info, List<SchematronDocumentDTO> data, Options o) {
		super(info);
		this.items = applyOptions(data, o);
		this.numberOfItems = data.size();
	}

	private List<SchematronDocumentDTO> applyOptions(List<SchematronDocumentDTO> documents, Options options) {
		return documents.stream().map(d -> d.applyOptions(options)).collect(Collectors.toList());
	}

}
