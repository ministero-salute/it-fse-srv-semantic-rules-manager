package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response; 


import io.swagger.v3.oas.annotations.media.Schema;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.SchematronDocumentDTO;
import lombok.Data;

/**
 * DTO use to return a document as response to getDocument by ID request.
 */
@Data
public class GetDocumentResDTO extends ResponseDTO {

    @Schema(implementation = SchematronDocumentDTO.class)
    private SchematronDocumentDTO document;

    public GetDocumentResDTO(LogTraceInfoDTO traceInfo, SchematronDocumentDTO data) {
        super(traceInfo);
        this.document = data;
    }
}
