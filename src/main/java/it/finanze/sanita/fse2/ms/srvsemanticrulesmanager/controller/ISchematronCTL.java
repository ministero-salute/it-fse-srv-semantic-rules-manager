/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.controller;

import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.utility.RouteUtility.*;
import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.utility.ValidationUtility.DEFAULT_STRING_MAX_SIZE;

import java.io.IOException;
import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.SchematronDocumentDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.crud.DelDocsResDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.crud.PostDocsResDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.crud.PutDocsResDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.error.base.ErrorResponseDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.impl.GetDocumentResDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.impl.GetDocumentsResDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.EmptyDocumentException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.InvalidContentException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.InvalidVersionException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.SchematronValidatorException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.validators.ValidObjectId;

/**
 * Schematron Controller.
 * 
 */
@RequestMapping(path = API_SCHEMATRON_MAPPER)
@Tag(name = API_SCHEMATRONS_TAG)
@Validated
public interface ISchematronCTL extends Serializable {

        @PostMapping(produces = {
                        MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
        @Operation(summary = "Add schematron to MongoDB", description = "Servizio che consente di aggiungere uno schematron alla base dati.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Creazione Schematron avvenuta con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PostDocsResDTO.class))),
                        @ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "409", description = "Conflitto riscontrato sulla risorsa", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))) })
        @ResponseStatus(HttpStatus.CREATED)
        PostDocsResDTO addSchematron(
                        @RequestPart(API_PATH_TEMPLATEIDROOT_VAR) @Parameter(description = "Template Id Root of the Schematron", schema = @Schema(minLength = 1, maxLength = 100)) @Size(min = 1, max = 100) @NotBlank(message = "Template Id cannot be blank") String templateIdRoot,
                        @RequestPart(API_PATH_VERSION_VAR) @Parameter(description = "Schematron version", schema = @Schema(minLength = 1, maxLength = 100)) @Size(min = 1, max = 100) @NotBlank(message = "version cannot be blank") @Pattern(message = "Version does not match the regex ^(\\d+\\.)(\\d+)$", regexp = "^(\\d+\\.)(\\d+)$") String version,
                        @RequestPart(API_PATH_FILE_VAR) MultipartFile file)
            throws IOException, OperationException, EmptyDocumentException, DocumentAlreadyPresentException, DocumentNotFoundException, InvalidContentException, SchematronValidatorException;

        @PutMapping(produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
                        MediaType.MULTIPART_FORM_DATA_VALUE })
        @Operation(summary = "Update schematron on MongoDB", description = "Servizio che consente di aggiornare uno schematron sulla base dati.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Aggiornamento Schematron avvenuta con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PutDocsResDTO.class))),
                        @ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "404", description = "Schematron non trovato sul database", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "409", description = "Conflitto riscontrato sulla risorsa", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))) })
        PutDocsResDTO updateSchematron(
                        @RequestPart(API_PATH_TEMPLATEIDROOT_VAR) @Parameter(description = "Template Id Root of the Schematron", schema = @Schema(minLength = 1, maxLength = 100)) @Size(min = 1, max = 100) @NotBlank(message = "Template Id cannot be blank") String templateIdRoot,
                        @RequestPart(API_PATH_VERSION_VAR) @Parameter(description = "Schematron version", schema = @Schema(minLength = 1, maxLength = 100)) @Size(min = 1, max = 100) @NotBlank(message = "Version cannot be blank") @Pattern(message = "Version does not match the regex ^(\\d+\\.)(\\d+)$", regexp = "^(\\d+\\.)(\\d+)$") String version,
                        @RequestPart(API_PATH_FILE_VAR) MultipartFile file)
            throws IOException, OperationException, DocumentNotFoundException, InvalidContentException, InvalidVersionException, SchematronValidatorException,
            DocumentAlreadyPresentException;

        @DeleteMapping(value = API_DELETE_BY_TEMPLATEIDROOT, produces = {MediaType.APPLICATION_JSON_VALUE })
        @Operation(summary = "Delete schematron from MongoDB given its Template ID Root", description = "Servizio che consente di cancellare uno schematron dalla base dati dato il Template ID Root")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Cancellazione Schematron avvenuta con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DelDocsResDTO.class))),
                        @ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "404", description = "Schematron non trovato sul database", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))) })
        DelDocsResDTO deleteSchematron(
            @NotBlank(message = "templateIdRoot cannot be blank")
            @PathVariable @Size(max = DEFAULT_STRING_MAX_SIZE, message = "templateIdRoot does not match the expected size")
            String templateIdRoot
        ) throws DocumentNotFoundException, OperationException;

        @GetMapping(value = API_GET_BY_TEMPLATEIDROOT, produces = {MediaType.APPLICATION_JSON_VALUE })
        @Operation(summary = "Returns a schematron from MongoDB, given its Template ID Root", description = "Servizio che consente di ritornare uno schematron dalla base dati dati il suo Template ID Root e Version.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Richiesta Schematron avvenuta con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = GetDocumentsResDTO.class))),
                @ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                @ApiResponse(responseCode = "404", description = "Schematron non trovato sul database", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
        })
        GetDocumentsResDTO getSchematronByTemplateIdRoot(
            @NotBlank(message = "templateIdRoot cannot be blank")
            @PathVariable
            @Size(max = DEFAULT_STRING_MAX_SIZE, message = "templateIdRoot does not match the expected size")
            String templateIdRoot,
            @RequestParam(value = API_QP_INCLUDE_BINARY, defaultValue = "false") @Parameter(description = "Include binary content") boolean binary,
            @RequestParam(value = API_QP_INCLUDE_DELETED, defaultValue = "false") @Parameter(description = "Include deleted content") boolean deleted
        ) throws DocumentNotFoundException, OperationException;

        @GetMapping(value = API_GET_ONE_BY_ID, produces = { MediaType.APPLICATION_JSON_VALUE })
        @Operation(summary = "Returns a Schematron from MongoDB, given its ID", description = "Servizio che consente di ritornare uno Schematron dalla base dati dati il suo ID.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Richiesta Schematron avvenuta con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SchematronDocumentDTO.class))),
                        @ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "404", description = "Schematron non trovato sul database", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))) })
        GetDocumentResDTO getSchematronById(@NotBlank(message = "Id cannot be blank") @PathVariable @Size(max = DEFAULT_STRING_MAX_SIZE, message = "id does not match the expected size") @ValidObjectId(message = "Document id not valid") String id) throws OperationException, DocumentNotFoundException;

        @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE })
        @Operation(summary = "Returns the list of all schematrons from MongoDB", description = "Servizio che consente di ritornare la lista degli schematron dalla base dati.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Richiesta Schematron avvenuta con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = GetDocumentsResDTO.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))) })
        GetDocumentsResDTO getSchematrons(
            @RequestParam(value = API_QP_INCLUDE_BINARY, defaultValue = "false") @Parameter(description = "Include binary content") boolean binary,
            @RequestParam(value = API_QP_INCLUDE_DELETED, defaultValue = "false") @Parameter(description = "Include deleted content") boolean deleted
        ) throws OperationException;

}
