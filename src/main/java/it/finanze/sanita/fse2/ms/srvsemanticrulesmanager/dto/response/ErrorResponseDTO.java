package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response; 

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * The Class ErrorResponseDTO.
 *
 * @author CPIERASC
 * 
 * 	Error response.
 */
@Data
public class ErrorResponseDTO extends ResponseDTO {

	/**
	 * Serial Version UID 
	 */
	private static final long serialVersionUID = 6423701106609931088L;


	public static final String GENERIC_ERROR = "/msg/generic-error";
    public static final String CONSTRAINT_ERROR = "/msg/constraint-error";
    public static final String NOT_FOUND_ERROR = "/msg/not-found";
    public static final String OPERATION_ERROR = "/msg/operation-error";
    public static final String CONFLICT_ERROR = "/msg/conflict-error";
    public static final String DATA_PROCESSING_ERROR = "/msg/process-error";
    public static final String DATA_INTEGRITY_ERROR = "/msg/data-integrity-error";

	
	/**
	 * Trace id log.
	 */
	@Schema(description = "Indentificativo univoco della richiesta dell'utente")
	@Size(min = 0, max = 100)
	private String traceID;
	
	/**
	 * Span id log.
	 */
	@Schema(description = "Indentificativo univoco di un task della richiesta dell'utente (differisce dal traceID solo in caso di chiamate sincrone in cascata)")
	@Size(min = 0, max = 100)
	private String spanID;

	@Schema(description = "Identificativo del problema verificatosi")
	@Size(min = 0, max = 100)
	private String type;
	
	@Schema(description = "Sintesi del problema (invariante per occorrenze diverse dello stesso problema)")
	@Size(min = 0, max = 1000)
	private String title;

	@Schema(description = "Descrizione del problema")
	@Size(min = 0, max = 1000)
	private String detail;

	@Schema(description = "Stato http")
	@Min(value = 100)
	@Max(value = 599)
	private Integer status;
	
	@Schema(description = "URI che potrebbe fornire ulteriori informazioni riguardo l'occorrenza del problema")
	@Size(min = 0, max = 100)
	private String instance;

	public ErrorResponseDTO(final LogTraceInfoDTO traceInfo, final String inType, final String inTitle, final String inDetail, final Integer inStatus, final String inInstance) {
		traceID = traceInfo.getTraceID();
		spanID = traceInfo.getSpanID();
		type = inType;
		title = inTitle;
		detail = inDetail;
		status = inStatus;
		instance = inInstance;
	}

	public ErrorResponseDTO(final LogTraceInfoDTO traceInfo) {
		traceID = traceInfo.getTraceID();
		spanID = traceInfo.getSpanID(); 
	}

}