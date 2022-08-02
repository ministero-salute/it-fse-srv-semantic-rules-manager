package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response;

import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.utility.ValidationUtility.DEFAULT_STRING_MAX_SIZE;
import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.utility.ValidationUtility.DEFAULT_STRING_MIN_SIZE;

import javax.validation.constraints.Size;

import lombok.Data;


/**
 * The Class SchematronCreationResponseDTO.
 *
 * @author Guido Rocco
 * 
 * 	Schematron Creation Response.
 */
@Data
public class SchematronResponseDTO extends ResponseDTO {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5857196886068379718L;

	@Size(min = DEFAULT_STRING_MIN_SIZE, max = DEFAULT_STRING_MAX_SIZE)
	private String transactionId;

	public SchematronResponseDTO() {
		super();
	}

	public SchematronResponseDTO(final LogTraceInfoDTO traceInfo, final String inTransactionId) {
		super(traceInfo);
		transactionId = inTransactionId;
	}
	
}
