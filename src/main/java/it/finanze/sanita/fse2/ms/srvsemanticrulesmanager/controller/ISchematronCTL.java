package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.controller;

import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.utility.ValidationUtility.DEFAULT_STRING_MAX_SIZE;
import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.utility.ValidationUtility.DEFAULT_STRING_MIN_SIZE;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Size;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.SchematronBodyDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.SchematronDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.GetDocumentResDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.SchematronErrorResponseDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.SchematronResponseDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.SchematronsDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.EmptyDocumentException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.validators.ValidObjectId;

/**
 * Schematron Controller.
 * 
 * @author Guido Rocco 
 */
@RequestMapping(path = "/v1")
@Tag(name = "Schematron Controller")
@Validated
public interface ISchematronCTL extends Serializable {

    @PostMapping(value = "/schematron",  produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @Operation(summary = "Add schematron to MongoDB", description = "Servizio che consente di aggiungere uno schematron alla base dati.")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = SchematronResponseDTO.class)))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Creazione Schematron avvenuta con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SchematronResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = SchematronErrorResponseDTO.class))) })
    ResponseEntity<SchematronResponseDTO> addSchematron(HttpServletRequest request, @RequestBody SchematronBodyDTO body, @RequestPart("content_schematron") MultipartFile contentSchematron) throws IOException, OperationException, EmptyDocumentException, DocumentAlreadyPresentException, DocumentNotFoundException;
    
    @PutMapping(value = "/schematron",  produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @Operation(summary = "Update schematron on MongoDB", description = "Servizio che consente di aggiornare uno schematron sulla base dati.")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = SchematronResponseDTO.class)))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aggiornamento Schematron avvenuta con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SchematronResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Schematron non trovato sul database", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = SchematronErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = SchematronErrorResponseDTO.class))) })
    ResponseEntity<SchematronResponseDTO> updateSchematron(HttpServletRequest request, @RequestBody SchematronBodyDTO body, @RequestPart("content_schematron") MultipartFile contentSchematron) throws IOException, EmptyDocumentException, OperationException;
    
    @DeleteMapping(value = "/schematron/root/{templateIdRoot}/extension/{templateIdExtension}",  produces = {
			MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Delete schematron from MongoDB given its Template ID Root and Template ID Extension", description = "Servizio che consente di cancellare uno schematron dalla base dati dato il Template ID Root e l'Extension.")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = SchematronResponseDTO.class)))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cancellazione Schematron avvenuta con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SchematronResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Schematron non trovato sul database", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = SchematronErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = SchematronErrorResponseDTO.class))) })
    ResponseEntity<SchematronResponseDTO> deleteSchematron(HttpServletRequest request, @PathVariable @Size(min = DEFAULT_STRING_MIN_SIZE, max = DEFAULT_STRING_MAX_SIZE, message = "templateIdRoot does not match the expected size") String templateIdRoot, @PathVariable @Size(min = DEFAULT_STRING_MIN_SIZE, max = DEFAULT_STRING_MAX_SIZE, message = "templateIdExtension does not match the expected size") String templateIdExtension) throws DocumentNotFoundException, OperationException; 
    
    
    @GetMapping(value = "/schematron/root/{templateIdRoot}/extension/{templateIdExtension}",  produces = {
 			MediaType.APPLICATION_JSON_VALUE })
     @Operation(summary = "Returns a schematron from MongoDB, given its Template ID Root and its Template ID Extension", description = "Servizio che consente di ritornare uno schematron dalla base dati dati il suo Template ID Root e Template ID Extension.")
     @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = SchematronResponseDTO.class)))
     @ApiResponses(value = {
             @ApiResponse(responseCode = "200", description = "Richiesta Schematron avvenuta con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SchematronDTO.class))),
             @ApiResponse(responseCode = "404", description = "Schematron non trovato sul database", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = SchematronErrorResponseDTO.class))),
             @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = SchematronErrorResponseDTO.class))) })
    ResponseEntity<SchematronDTO> getSchematronByTemplateIdRootAndTemplateIdExtension(HttpServletRequest request, @PathVariable @Size(min = DEFAULT_STRING_MIN_SIZE, max = DEFAULT_STRING_MAX_SIZE, message = "templateIdRoot does not match the expected size") String templateIdRoot, @PathVariable @Size(min = DEFAULT_STRING_MIN_SIZE, max = DEFAULT_STRING_MAX_SIZE, message = "templateIdExtension does not match the expected size") String templateIdExtension) throws DocumentNotFoundException, OperationException; 

    @GetMapping(value = "/schematron/id/{id}", produces = {MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Returns a Schematron from MongoDB, given its ID", description = "Servizio che consente di ritornare uno Schematron dalla base dati dati il suo ID.")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = SchematronResponseDTO.class)))
    @ApiResponses(value = {
                    @ApiResponse(responseCode = "200", description = "Richiesta Schematron avvenuta con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SchematronDTO.class))),
                    @ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = SchematronErrorResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Schematron non trovato sul database", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = SchematronErrorResponseDTO.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = SchematronErrorResponseDTO.class))) })
    ResponseEntity<GetDocumentResDTO> getSchematronById(HttpServletRequest request, @PathVariable @Size(min = DEFAULT_STRING_MIN_SIZE, max = DEFAULT_STRING_MAX_SIZE, message = "id does not match the expected size") @ValidObjectId(message = "Document id not valid") String id)
    throws OperationException, DocumentNotFoundException;


    @GetMapping(value = "/schematron",  produces = {
 			MediaType.APPLICATION_JSON_VALUE })
     @Operation(summary = "Returns the list of all schematrons from MongoDB", description = "Servizio che consente di ritornare la lista degli schematron dalla base dati.")
     @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = SchematronsDTO.class)))
     @ApiResponses(value = {
             @ApiResponse(responseCode = "200", description = "Richiesta Schematron avvenuta con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SchematronsDTO.class))),
             @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = SchematronErrorResponseDTO.class))) })
        ResponseEntity<SchematronsDTO> getSchematrons(HttpServletRequest request); 


}
