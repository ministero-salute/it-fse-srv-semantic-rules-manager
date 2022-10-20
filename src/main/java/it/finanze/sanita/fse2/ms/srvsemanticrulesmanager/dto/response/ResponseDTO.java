/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response;

import javax.validation.constraints.Size;

import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author CPIERASC
 *
 *         Base response.
 */
@Getter
@Setter
public class ResponseDTO implements AbstractDTO {


	/**
	 * Serial Version UID 
	 */
	private static final long serialVersionUID = -5302147600940881591L;

	/**
	 * Trace id log.
	 */
	@Size(min = 0, max = 100)
	private String traceID;

	/**
	 * Span id log.
	 */
	@Size(min = 0, max = 100)
	private String spanID;

	/**
	 * Instantiates a new response DTO.
	 */
	public ResponseDTO() {
	}

	/**
	 * Instantiates a new response DTO.
	 *
	 * @param traceInfo the trace info
	 */
	public ResponseDTO(final LogTraceInfoDTO traceInfo) {
		traceID = traceInfo.getTraceID();
		spanID = traceInfo.getSpanID();
	}

}