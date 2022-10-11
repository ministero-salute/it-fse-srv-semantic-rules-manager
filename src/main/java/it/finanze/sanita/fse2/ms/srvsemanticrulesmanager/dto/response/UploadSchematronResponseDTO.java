package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * The Class SchematronCreationResponseDTO.
 *
 * @author Guido Rocco
 * 
 * 	Schematron Creation Response.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UploadSchematronResponseDTO extends ResponseDTO {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5857196886068379718L;
 
	private Integer insertedSchematron;

	public UploadSchematronResponseDTO() {
		super();
	}

	public UploadSchematronResponseDTO(final LogTraceInfoDTO traceInfo, final Integer inInsertedSchematron) {
		super(traceInfo);
		insertedSchematron = inInsertedSchematron;
	}
	
}
