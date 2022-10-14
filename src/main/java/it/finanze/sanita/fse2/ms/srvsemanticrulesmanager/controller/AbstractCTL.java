package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.controller;

import brave.Tracer;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.LogTraceInfoDTO;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * 
 * @author CPIERASC
 *
 *	Abstract controller.
 */
public abstract class AbstractCTL implements Serializable {


	/**
	 * Serial Version UID 
	 */
	private static final long serialVersionUID = -7177691577253703309L; 
	
	
	@Autowired
	private transient Tracer tracer;

	protected boolean isValidFile(MultipartFile file) {
		if (file != null && !file.isEmpty()) {
			try {
				final String content = new String(file.getBytes(), StandardCharsets.UTF_8);
				final String extension = Optional.ofNullable(FilenameUtils.getExtension(file.getOriginalFilename())).orElse("");
				final boolean isSch = extension.contains("sch");
				return isSch && content.startsWith("<?xml") && content.contains("schema");
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

