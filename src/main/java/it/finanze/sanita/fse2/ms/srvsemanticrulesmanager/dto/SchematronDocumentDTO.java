package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto;

import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.utility.ValidationUtility.DEFAULT_STRING_MAX_SIZE;

import java.time.OffsetDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.repository.entity.SchematronETY;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.utility.UtilsMisc;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SchematronDocumentDTO {

	@Schema(maxLength = DEFAULT_STRING_MAX_SIZE)
	private String id;
	@Schema(maxLength = DEFAULT_STRING_MAX_SIZE)
	private String nameSchematron;
	@Schema(maxLength = DEFAULT_STRING_MAX_SIZE)
	private String contentSchematron;
	@Schema(maxLength = DEFAULT_STRING_MAX_SIZE)
	private String templateIdExtension;
	@Schema(maxLength = DEFAULT_STRING_MAX_SIZE)
	private String templateIdRoot;
	private OffsetDateTime lastUpdateDate;

	public static SchematronDocumentDTO fromEntity(SchematronETY e) {
		return new SchematronDocumentDTO(
				e.getId(),
				e.getNameSchematron(),
				UtilsMisc.encodeBase64(e.getContentSchematron().getData()),
				e.getTemplateIdExtension(),
				e.getTemplateIdRoot(),
				UtilsMisc.convertToOffsetDateTime(e.getLastUpdateDate()));
	}

}
