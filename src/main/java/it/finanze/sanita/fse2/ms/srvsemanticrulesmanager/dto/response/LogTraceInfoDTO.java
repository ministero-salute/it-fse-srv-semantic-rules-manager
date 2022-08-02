package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response;

import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.AbstractDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.LogTraceInfoDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class LogTraceInfoDTO implements AbstractDTO {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5174354198542609857L;

	
	/**
	 * Span.
	 */
	private final String spanID;
	
	/**
	 * Trace.
	 */
	private final String traceID;

}
