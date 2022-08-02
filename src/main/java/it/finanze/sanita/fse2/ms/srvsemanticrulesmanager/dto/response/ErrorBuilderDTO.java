package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response;


import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.ErrorResponseDTO.CONFLICT_ERROR;
import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.ErrorResponseDTO.CONSTRAINT_ERROR;
import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.ErrorResponseDTO.GENERIC_ERROR;
import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.ErrorResponseDTO.NOT_FOUND_ERROR;
import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.ErrorResponseDTO.OPERATION_ERROR;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_CONFLICT;


import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.*;

public final class ErrorBuilderDTO {

    public static final String VALIDATION_TITLE = "Validation error";
    public static final String GENERIC_TITLE = "Unknown error";
    public static final String EXTENSION_NOT_FOUND_TITLE = "Extension not found";
    public static final String OPERATION_TITLE = "Internal error";
    public static final String INTEGRITY_TITLE = "Compromised Data Integrity";
    public static final String EXTENSION_CONFLICT_TITLE = "Extension conflict";

    public static final String DATA_PROCESSING_TITLE = "Unfulfilled request";
    public static final String DOCUMENT_NOT_FOUND_TITLE = "Document not found";
    public static final String DOCUMENT_ALREADY_PRESENT_TITLE = "Document already present";
    public static final String PARAMETER_NOT_FOUND_TITLE = "Missing data parameter";


    /**
     * Private constructor to disallow to access from other classes
     */
    private ErrorBuilderDTO() {}



    public static ErrorResponseDTO createObjectIdNotValidError(LogTraceInfoDTO trace, ObjectIdNotValidException ex) {
        return new ErrorResponseDTO(
            trace,
            CONSTRAINT_ERROR,
            VALIDATION_TITLE,
            ex.getMessage(),
            SC_BAD_REQUEST,
            CONSTRAINT_ERROR
        );
    }


    public static ErrorResponseDTO createGenericError(LogTraceInfoDTO trace, Exception ex) {
        return new ErrorResponseDTO(
            trace,
            GENERIC_ERROR,
            GENERIC_TITLE,
            ex.getMessage(),
            SC_INTERNAL_SERVER_ERROR,
            GENERIC_ERROR
        );
    }


    public static ErrorResponseDTO createOperationError(LogTraceInfoDTO trace, OperationException ex) {
        return new ErrorResponseDTO(
            trace,
            OPERATION_ERROR,
            OPERATION_TITLE,
            ex.getMessage(),
            SC_INTERNAL_SERVER_ERROR,
            OPERATION_ERROR
        );
    }

    public static ErrorResponseDTO createDocumentNotFoundError(LogTraceInfoDTO trace, DocumentNotFoundException ex) {
        return new ErrorResponseDTO(
            trace,
            NOT_FOUND_ERROR,
            DOCUMENT_NOT_FOUND_TITLE,
            ex.getMessage(),
            SC_NOT_FOUND,
            NOT_FOUND_ERROR
        ); 
    }

    public static ErrorResponseDTO createDateNotValidError(LogTraceInfoDTO trace, DateNotValidException ex) {
        return new ErrorResponseDTO(
            trace,
            CONSTRAINT_ERROR,
            VALIDATION_TITLE,
            ex.getMessage(),
            SC_BAD_REQUEST,
            CONSTRAINT_ERROR
        );
    }

    public static ErrorResponseDTO createDocumentAlreadyPresentError(LogTraceInfoDTO trace, DocumentAlreadyPresentException ex) {
        return new ErrorResponseDTO(
            trace,
            CONFLICT_ERROR,
            DOCUMENT_ALREADY_PRESENT_TITLE,
            ex.getMessage(),
            SC_CONFLICT,
            CONFLICT_ERROR
        );
    } 
   



}
