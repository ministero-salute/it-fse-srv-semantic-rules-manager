package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.controller.impl;

import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.utility.ValidationUtility.DEFAULT_STRING_MAX_SIZE;
import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.utility.ValidationUtility.DEFAULT_STRING_MIN_SIZE;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Size;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.config.ValidationCFG;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.controller.AbstractCTL;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.controller.ISchematronCTL;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.SchematronBodyDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.SchematronDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.SchematronDocumentDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.GetDocumentResDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.SchematronResponseDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.SchematronsDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.enums.OperationLogEnum;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.enums.ResultLogEnum;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.enums.UIDModeEnum;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.EmptyDocumentException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.ObjectIdNotValidException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.logging.ElasticLoggerHelper;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.service.ISchematronSRV;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.utility.StringUtility;
import lombok.extern.slf4j.Slf4j;


/** 
 * @author Riccardo Bonesi
 * @author Guido Rocco
 */
@RestController
@Slf4j
public class SchematronCTL extends AbstractCTL implements ISchematronCTL {

	/**
	 * Serial Version UID 
	 */
	private static final long serialVersionUID = 3796163429050020208L;

	
	@Autowired
	private ISchematronSRV schematronService; 
	
	@Autowired
	private ElasticLoggerHelper elasticLogger; 
	
	@Autowired
	private ValidationCFG validationCFG;
	
	
	@Override
	public ResponseEntity<SchematronResponseDTO> addSchematron(HttpServletRequest request, 
			      @RequestBody SchematronBodyDTO body, @RequestPart("content_schematron") MultipartFile contentSchematron) throws IOException, EmptyDocumentException, OperationException, DocumentAlreadyPresentException, DocumentNotFoundException {

		log.info(Constants.Logs.CALLED_API_POST_SCHEMATRON); 
		elasticLogger.info(Constants.Logs.CALLED_API_POST_SCHEMATRON, OperationLogEnum.POST_SCHEMATRON, ResultLogEnum.OK, new Date()); 
		
		final String transactionId = StringUtility.generateTransactionUID(UIDModeEnum.get(validationCFG.getTransactionIDStrategy())); 
		Date date = new Date(); 
		
		SchematronBodyDTO schematronFromBody = getSchematronJSONObject(request.getParameter(Constants.App.BODY)); 
		
		if(!contentSchematron.isEmpty() && schematronFromBody!=null) {
			SchematronDTO schematron = new SchematronDTO();
			schematron.setContentSchematron(new Binary(BsonBinarySubType.BINARY, contentSchematron.getBytes()));
			schematron.setNameSchematron(schematronFromBody.getNameSchematron());
			schematron.setTemplateIdRoot(schematronFromBody.getTemplateIdRoot());
			schematron.setTemplateIdExtension(schematronFromBody.getTemplateIdExtension());
			schematron.setInsertionDate(date); 
			schematron.setLastUpdateDate(date);

			schematronService.insert(schematron);
			return new ResponseEntity<>(new SchematronResponseDTO(getLogTraceInfo(), transactionId), HttpStatus.OK); 
		} 
		
		return new ResponseEntity<>(new SchematronResponseDTO(getLogTraceInfo(), transactionId), HttpStatus.NO_CONTENT); 
		
	}

	@Override
	public ResponseEntity<SchematronResponseDTO> updateSchematron(HttpServletRequest request,
	SchematronBodyDTO body, MultipartFile contentSchematron) throws IOException, EmptyDocumentException, OperationException {
		log.info(Constants.Logs.CALLED_API_PUT_SCHEMATRON); 
		elasticLogger.info(Constants.Logs.CALLED_API_PUT_SCHEMATRON, OperationLogEnum.PUT_SCHEMATRON, ResultLogEnum.OK, new Date()); 
		
		final String transactionId = StringUtility.generateTransactionUID(UIDModeEnum.get(validationCFG.getTransactionIDStrategy()));

		boolean hasBeenUpdated = false; 
		
		SchematronBodyDTO schematronFromBody = getSchematronJSONObject(request.getParameter(Constants.App.BODY)); 
		
		if(!contentSchematron.isEmpty() && schematronFromBody!=null) {
			SchematronDTO schematron = new SchematronDTO();
			schematron.setContentSchematron(new Binary(BsonBinarySubType.BINARY, contentSchematron.getBytes()));
			schematron.setNameSchematron(schematronFromBody.getNameSchematron());
			schematron.setTemplateIdRoot(schematronFromBody.getTemplateIdRoot());
			schematron.setTemplateIdExtension(schematronFromBody.getTemplateIdExtension());
			schematron.setLastUpdateDate(new Date());

			
			hasBeenUpdated = schematronService.update(schematron);
		}
	
		
		if(hasBeenUpdated) {
			return new ResponseEntity<>(new SchematronResponseDTO(getLogTraceInfo(), transactionId), HttpStatus.OK); 
		} 
		else {
			return new ResponseEntity<>(new SchematronResponseDTO(getLogTraceInfo(), transactionId), HttpStatus.NO_CONTENT); 
		} 
		
	}
	

	@Override
	public ResponseEntity<SchematronResponseDTO> deleteSchematron(HttpServletRequest request, String templateIdRoot, String templateIdExtension) throws DocumentNotFoundException, OperationException {
		
		log.info(Constants.Logs.CALLED_API_DELETE_SCHEMATRON); 
		elasticLogger.info(Constants.Logs.CALLED_API_DELETE_SCHEMATRON, OperationLogEnum.DELETE_SCHEMATRON, ResultLogEnum.OK, new Date()); 
		
		final String transactionId = StringUtility.generateTransactionUID(UIDModeEnum.get(validationCFG.getTransactionIDStrategy()));
		
		boolean existsSchematron = schematronService.deleteSchematron(templateIdRoot, templateIdExtension); 	
		
		if(existsSchematron) {
			return new ResponseEntity<>(new SchematronResponseDTO(getLogTraceInfo(), transactionId), HttpStatus.OK); 
		} 
		else {
			throw new DocumentNotFoundException(Constants.Logs.ERROR_DOCUMENT_NOT_FOUND); 
		}
	} 
	
	@Override
	public ResponseEntity<SchematronDTO> getSchematronByTemplateIdRootAndTemplateIdExtension(HttpServletRequest request, 
			String templateIdRoot, String templateIdExtension) throws DocumentNotFoundException, OperationException {
		
		log.info(Constants.Logs.CALLED_API_QUERY_ROOT_EXTENSION); 
		elasticLogger.info(Constants.Logs.CALLED_API_QUERY_ROOT_EXTENSION, OperationLogEnum.QUERY_SCHEMATRON, ResultLogEnum.OK, new Date()); 
				
		SchematronDTO response =  schematronService.findByTemplateIdRootAndTemplateIdExtension(templateIdRoot, templateIdExtension); 
				
		return ResponseEntity.status(HttpStatus.OK).body(response);
	} 
	
	@Override
	public ResponseEntity<SchematronsDTO> getSchematrons(HttpServletRequest request) {
		
		log.info(Constants.Logs.CALLED_API_GET_SCHEMATRON);  
		elasticLogger.info(Constants.Logs.CALLED_API_GET_SCHEMATRON, OperationLogEnum.QUERY_SCHEMATRON, ResultLogEnum.OK, new Date()); 
		
		List<SchematronDTO> schematrons = schematronService.getSchematrons(); 

		return new ResponseEntity<>(new SchematronsDTO(getLogTraceInfo(), schematrons), HttpStatus.OK); 
	}  
	
	
	public static SchematronBodyDTO getSchematronJSONObject(String jsonREQ) {
		SchematronBodyDTO out = null;
		if(!StringUtility.isNullOrEmpty(jsonREQ)) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				out = mapper.readValue(jsonREQ, SchematronBodyDTO.class);
			} catch (Exception ex) {
				log.error(Constants.Logs.ERROR_JSON_HANDLING, ex); 
			}
		}
		return out;
	
	}

	@Override
	public ResponseEntity<GetDocumentResDTO> getSchematronById(HttpServletRequest request, @Size(min = DEFAULT_STRING_MIN_SIZE, max = DEFAULT_STRING_MAX_SIZE, message = "id does not match the expected size") String id) throws OperationException, DocumentNotFoundException, ObjectIdNotValidException {

		log.info(Constants.Logs.CALLED_API_QUERY_ID); 
		elasticLogger.info(Constants.Logs.CALLED_API_GET_SCHEMATRON_BY_ID, OperationLogEnum.QUERY_SCHEMATRON_BY_ID, ResultLogEnum.OK, new Date()); 

		SchematronDocumentDTO doc = schematronService.findById(id); 

		return new ResponseEntity<>(new GetDocumentResDTO(getLogTraceInfo(), doc), HttpStatus.OK);

	}
	
	
}
