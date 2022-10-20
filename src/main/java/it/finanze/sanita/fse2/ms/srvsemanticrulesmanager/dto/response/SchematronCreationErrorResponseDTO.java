/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response;

import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.error.base.ErrorResponseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The Class ValidationErrorResponseDTO.
 * 
 * 	Error response.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SchematronCreationErrorResponseDTO extends ErrorResponseDTO {

	/**
	 * Serial Version UID 
	 */
	private static final long serialVersionUID = 3002572298324519942L; 
	
	
	@Schema(description = "Identificativo della transazione in errore")
	@Size(min = 0, max = 100)
	private String transactionId;
	
	public SchematronCreationErrorResponseDTO(final LogTraceInfoDTO traceInfo, final String inType, final String inTitle, final String inDetail, final Integer inStatus, final String inInstance, final String inTransactionId) {
		super(traceInfo, inType, inTitle, inDetail, inStatus, inInstance);
		transactionId = inTransactionId;
	}

}
