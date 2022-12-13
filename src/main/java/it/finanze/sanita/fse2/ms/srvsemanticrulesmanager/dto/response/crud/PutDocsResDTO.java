/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.crud;

import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.ResponseDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.log.LogTraceInfoDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * The Class SchematronCreationResponseDTO.
 *
 * 
 * 	Schematron Response.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PutDocsResDTO extends ResponseDTO {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5857199886068379718L;

	private Integer updatedItems;

	public PutDocsResDTO(final LogTraceInfoDTO traceInfo, final Integer updatedItems) {
		super(traceInfo);
		this.updatedItems = updatedItems;
	}
	
}
