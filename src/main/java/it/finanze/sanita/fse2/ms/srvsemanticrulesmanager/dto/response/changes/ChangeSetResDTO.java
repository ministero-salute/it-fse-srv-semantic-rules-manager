/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.changes;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.ResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.utility.ValidationUtility.*;

/**
 * DTO for Change Set status endpoint response.
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeSetResDTO extends ResponseDTO {

	  /**
     * Trace id log.
     */
    @Schema(maxLength = DEFAULT_STRING_MAX_SIZE)
    private String traceID;

    /**
     * Span id log.
     */
    @Schema(maxLength = DEFAULT_STRING_MAX_SIZE)
    private String spanID;

	private Date lastUpdate;
	private Date timestamp;

	@ArraySchema(minItems = DEFAULT_ARRAY_MIN_SIZE, maxItems = DEFAULT_ARRAY_MAX_SIZE, uniqueItems = true)
	private List<ChangeSetDTO> insertions;

	@ArraySchema(minItems = DEFAULT_ARRAY_MIN_SIZE, maxItems = DEFAULT_ARRAY_MAX_SIZE, uniqueItems = true)
	private List<ChangeSetDTO> deletions;

	@Schema(minimum = DEFAULT_ARRAY_MIN_SIZE + "", maximum = DEFAULT_ARRAY_MAX_SIZE + "")
	private long totalNumberOfElements;

	private long collectionSize;
}
