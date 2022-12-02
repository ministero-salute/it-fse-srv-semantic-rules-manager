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
public class SchematronResponseDTO extends ResponseDTO {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5857196886068379718L;
 
	private Integer insertedSchematron;
	private Integer updatedSchematron;
	private Integer deletedSchematron;

	public SchematronResponseDTO() {
		super();
	}

	public SchematronResponseDTO(LogTraceInfoDTO traceInfo, Integer insertedSchematron, Integer updatedSchematron, Integer deletedSchematron) {
		super(traceInfo);
		this.insertedSchematron = insertedSchematron;
		this.updatedSchematron = updatedSchematron;
		this.deletedSchematron = deletedSchematron;
	}
}
