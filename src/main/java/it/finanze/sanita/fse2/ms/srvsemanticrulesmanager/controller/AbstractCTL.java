/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.controller;

import brave.Tracer;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.log.LogTraceInfoDTO;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

/**
 * 
 *
 *	Abstract controller.
 */
public abstract class AbstractCTL {
	
	@Autowired
	private Tracer tracer;

	protected boolean isValidFile(MultipartFile file) {
		if (file != null && !file.isEmpty()) {
			try {
				final String extension = Optional.ofNullable(FilenameUtils.getExtension(file.getOriginalFilename())).orElse("");
				return extension.equals("sch");
			} catch (Exception e) {
				return false;
			}
		} else {
			return false;
		}
	}

	protected LogTraceInfoDTO getLogTraceInfo() {
		LogTraceInfoDTO out = new LogTraceInfoDTO(null, null);
		if (tracer.currentSpan() != null) {
			out = new LogTraceInfoDTO(
					tracer.currentSpan().context().spanIdString(), 
					tracer.currentSpan().context().traceIdString());
		}
		return out;
	}

}

