/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response;

import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.utility.ValidationUtility.DEFAULT_STRING_MAX_SIZE;
import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.utility.ValidationUtility.DEFAULT_STRING_MIN_SIZE;

import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.error.base.ErrorResponseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * The Class ValidationErrorResponseDTO.
 *
 * 
 * 	Error response.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SchematronErrorResponseDTO extends ErrorResponseDTO {

	@Schema(description = "Identificativo della transazione in errore")
	@Size(min = DEFAULT_STRING_MIN_SIZE, max = DEFAULT_STRING_MAX_SIZE)
	private String transactionId;
	
	public SchematronErrorResponseDTO(final LogTraceInfoDTO traceInfo, final String inType, final String inTitle, final String inDetail, final Integer inStatus, final String inInstance, final String inTransactionId) {
		super(traceInfo, inType, inTitle, inDetail, inStatus, inInstance);
		transactionId = inTransactionId;
	}

}
