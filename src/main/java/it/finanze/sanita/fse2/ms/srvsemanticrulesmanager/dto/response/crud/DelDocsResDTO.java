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
public class DelDocsResDTO extends ResponseDTO {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5857199886068379718L;

	private Integer deletedItems;

	public DelDocsResDTO(final LogTraceInfoDTO traceInfo, final Integer deletedItems) {
		super(traceInfo);
		this.deletedItems = deletedItems;
	}
	
}
