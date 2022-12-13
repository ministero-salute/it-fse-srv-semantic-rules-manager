/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.impl;

import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.ResponseDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.log.LogTraceInfoDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * The Class SchematronCreationResponseDTO.
 * 	Schematron Creation Response.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CrudDocumentResDTO extends ResponseDTO {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5857196886068379718L;
 
	private Integer insertedItems;
	private Integer updatedItems;
	private Integer deletedItems;

	public CrudDocumentResDTO() {
		super();
	}

	public CrudDocumentResDTO(LogTraceInfoDTO traceInfo, Integer insertedItems, Integer updatedItems, Integer deletedItems) {
		super(traceInfo);
		this.insertedItems = insertedItems;
		this.updatedItems = updatedItems;
		this.deletedItems = deletedItems;
	}
}
