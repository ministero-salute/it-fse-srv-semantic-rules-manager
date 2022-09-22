package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response;

import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.utility.ValidationUtility.DEFAULT_ARRAY_MAX_SIZE;
import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.utility.ValidationUtility.DEFAULT_ARRAY_MIN_SIZE;

import java.util.List;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.SchematronDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Riccardo Bonesi
 * 
 * The Class SchematronsDTO, used to return the list of all schematrons.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SchematronsDTO extends ResponseDTO {


	@ArraySchema(minItems = DEFAULT_ARRAY_MIN_SIZE, maxItems = DEFAULT_ARRAY_MAX_SIZE, uniqueItems = true)
	private transient List<SchematronDTO> schematrons;

	public SchematronsDTO() {
		super();
	}

	public SchematronsDTO(final LogTraceInfoDTO traceInfo, final List<SchematronDTO> inSchematrons) {
		super(traceInfo);
		schematrons = inSchematrons;
	}

}
