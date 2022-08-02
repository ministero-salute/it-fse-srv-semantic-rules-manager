package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.controller.handler;




import brave.Tracer;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.ErrorResponseDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.LogTraceInfoDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.*;
import lombok.extern.slf4j.Slf4j;

import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.ErrorBuilderDTO.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 *	Exceptions handler
 *  @author G. Baittiner
 */
@ControllerAdvice
@Slf4j
public class ExceptionCTL extends ResponseEntityExceptionHandler {

    /**
     * Tracker log.
     */
    @Autowired
    private Tracer tracer;

    /**
     * Handle object id not valid
     *
     * @param ex exception
     */
    @ExceptionHandler(ObjectIdNotValidException.class)
    protected ResponseEntity<ErrorResponseDTO> handleObjectIdNotValidException(ObjectIdNotValidException ex) {
        // Log me
        log.warn("HANDLER handleObjectIdNotValidException()");
        log.error("HANDLER handleObjectIdNotValidException()", ex);
        // Create error DTO
        ErrorResponseDTO out = createObjectIdNotValidError(getLogTraceInfo(), ex);
        // Set HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);
        // Bye bye
        return new ResponseEntity<>(out, headers, out.getStatus());
    }

    /**
     * Handle document not found exception.
     *
     * @param ex		exception
     */
    @ExceptionHandler(DocumentNotFoundException.class)
    protected ResponseEntity<ErrorResponseDTO> handleDocumentNotFoundException(DocumentNotFoundException ex) {
        // Log me
        log.warn("HANDLER handleDocumentNotFoundException()");
        log.error("HANDLER handleDocumentNotFoundException()", ex);
        // Create error DTO
        ErrorResponseDTO out = createDocumentNotFoundError(getLogTraceInfo(), ex);
        // Set HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);
        // Bye bye
        return new ResponseEntity<>(out, headers, out.getStatus());
    }

    /**
     * Handle date not valid exception.
     *
     * @param ex		exception
     */
    @ExceptionHandler(DateNotValidException.class)
    protected ResponseEntity<ErrorResponseDTO> handleDateNotValidException(DateNotValidException ex) {
        // Log me
        log.warn("HANDLER handleDateNotValidException()");
        log.error("HANDLER handleDateNotValidException()", ex);
        // Create error DTO
        ErrorResponseDTO out = createDateNotValidError(getLogTraceInfo(), ex);
        // Set HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);
        // Bye bye
        return new ResponseEntity<>(out, headers, out.getStatus());
    }
    
    /**
     * Handle document already present exception.
     *
     * @param ex		exception
     */
    @ExceptionHandler(DocumentAlreadyPresentException.class)
    protected ResponseEntity<ErrorResponseDTO> handleDocumentAlreadyPresentException(DocumentAlreadyPresentException ex) {
        // Log me
        log.warn("HANDLER handleDocumentAlreadyPresentException()");
        log.error("HANDLER handleDocumentAlreadyPresentException()", ex);
        // Create error DTO
        ErrorResponseDTO out = createDocumentAlreadyPresentError(getLogTraceInfo(), ex);
        // Set HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);
        // Bye bye
        return new ResponseEntity<>(out, headers, out.getStatus());
    } 
    


    /**
     * Handle operation exception.
     *
     * @param ex		exception
     */
    @ExceptionHandler(OperationException.class)
    protected ResponseEntity<ErrorResponseDTO> handleOperationException(OperationException ex) {
        // Log me
        log.warn("HANDLER handleOperationException()");
        log.error("HANDLER handleOperationException()", ex);
        // Create error DTO
        ErrorResponseDTO out = createOperationError(getLogTraceInfo(), ex);
        // Set HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);
        // Bye bye
        return new ResponseEntity<>(out, headers, out.getStatus());
    }





    /**
     * Handle generic exception.
     *
     * @param ex		exception
     */
    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {
        // Log me
        log.warn("HANDLER handleGenericException()");
        log.error("HANDLER handleGenericException()", ex);
        // Create error DTO
        ErrorResponseDTO out = createGenericError(getLogTraceInfo(), ex);
        // Set HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);
        // Bye bye
        return new ResponseEntity<>(out, headers, out.getStatus());
    } 

    
    
    /**
     * Generate a new {@link LogTraceInfoDTO} instance
     * @return The new instance
     */
    private LogTraceInfoDTO getLogTraceInfo() {
        // Create instance
        LogTraceInfoDTO out = new LogTraceInfoDTO(null, null);
        // Verify if context is available
        if (tracer.currentSpan() != null) {
            out = new LogTraceInfoDTO(
                tracer.currentSpan().context().spanIdString(),
                tracer.currentSpan().context().traceIdString());
        }
        // Return the log trace
        return out;
    }
}
