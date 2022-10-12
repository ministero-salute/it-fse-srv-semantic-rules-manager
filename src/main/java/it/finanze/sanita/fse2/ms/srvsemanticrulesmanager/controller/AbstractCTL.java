package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.controller;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


import brave.Tracer;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.LogTraceInfoDTO; 

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

		boolean isValid = false;
		if (file != null && !file.isEmpty()) {
			try {
				final Path path = Paths.get(file.getOriginalFilename());
				final String mimeType = Files.probeContentType(path);
				final String content = new String(file.getBytes(), StandardCharsets.UTF_8);

				isValid = MimeTypeUtils.TEXT_XML_VALUE.equals(mimeType) && content.startsWith("<?xml") && content.contains("schema");
			} catch (Exception e) {
				isValid = false;
			}
		}

		return isValid;
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

