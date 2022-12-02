/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.impl;

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
public class GetDocumentsResDTO extends ResponseDTO {


	@ArraySchema(minItems = DEFAULT_ARRAY_MIN_SIZE, maxItems = DEFAULT_ARRAY_MAX_SIZE, uniqueItems = true)
	private List<SchematronDocumentDTO> schematrons;


	public GetDocumentsResDTO(LogTraceInfoDTO info, List<SchematronDocumentDTO> data, Options o) {
		super(info);
		this.schematrons = applyOptions(data, o);
	}

	private List<SchematronDocumentDTO> applyOptions(List<SchematronDocumentDTO> documents, Options options) {
		return documents.stream().map(d -> d.applyOptions(options)).collect(Collectors.toList());
	}

}
