package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.controller.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.controller.AbstractCTL;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.controller.ISchematronCTL;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.SchematronBodyDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.SchematronDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.SchematronDocumentDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.GetDocumentResDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.SchematronResponseDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.SchematronsDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.UploadSchematronResponseDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.BusinessException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.EmptyDocumentException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.OperationException;
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
	private transient ISchematronSRV schematronService; 
 
	@Override
	public ResponseEntity<UploadSchematronResponseDTO> addSchematron(HttpServletRequest request, 
			      @RequestBody SchematronBodyDTO body, @RequestPart("content_schematron") MultipartFile contentSchematron) throws IOException, EmptyDocumentException, OperationException, DocumentAlreadyPresentException, DocumentNotFoundException {

		log.info(Constants.Logs.CALLED_API_POST_SCHEMATRON); 
		
		Date date = new Date(); 
		SchematronBodyDTO schematronFromBody = getSchematronJSONObject(request.getParameter(Constants.App.BODY)); 
		
		if(!contentSchematron.isEmpty() && schematronFromBody!=null) {
			SchematronDTO schematron = new SchematronDTO();
			if(contentSchematron.getBytes().length==0) {
				throw new BusinessException("Attenzione il file fornito risulta essere vuoto");
			}
			schematron.setContentSchematron(new Binary(BsonBinarySubType.BINARY, contentSchematron.getBytes()));
			schematron.setNameSchematron(schematronFromBody.getNameSchematron());
			schematron.setTemplateIdRoot(schematronFromBody.getTemplateIdRoot());
			schematron.setVersion(schematronFromBody.getVersion());
			schematron.setInsertionDate(date); 
			schematron.setLastUpdateDate(date);
			schematronService.insert(schematron);
			return new ResponseEntity<>(new UploadSchematronResponseDTO(getLogTraceInfo(),1), HttpStatus.CREATED); 
		} 
		
		return new ResponseEntity<>(new UploadSchematronResponseDTO(getLogTraceInfo(),0), HttpStatus.NO_CONTENT); 
		
	}

	@Override
	public ResponseEntity<SchematronResponseDTO> updateSchematron(HttpServletRequest request,
	SchematronBodyDTO body, MultipartFile contentSchematron) throws IOException, EmptyDocumentException, OperationException {
		log.info(Constants.Logs.CALLED_API_PUT_SCHEMATRON); 
		
		Date date = new Date();

		boolean hasBeenUpdated = false; 
		
		SchematronBodyDTO schematronFromBody = getSchematronJSONObject(request.getParameter(Constants.App.BODY)); 
		
		if(!contentSchematron.isEmpty() && schematronFromBody!=null) {
			SchematronDTO schematron = new SchematronDTO();
			schematron.setContentSchematron(new Binary(BsonBinarySubType.BINARY, contentSchematron.getBytes()));
			schematron.setNameSchematron(schematronFromBody.getNameSchematron());
			schematron.setTemplateIdRoot(schematronFromBody.getTemplateIdRoot());
			schematron.setVersion(schematronFromBody.getVersion());
			schematron.setInsertionDate(date);
			schematron.setLastUpdateDate(date);

			
			hasBeenUpdated = schematronService.update(schematron);
		}
	
		
		if(hasBeenUpdated) {
			return new ResponseEntity<>(new SchematronResponseDTO(getLogTraceInfo()), HttpStatus.OK); 
		} 
		else {
			return new ResponseEntity<>(new SchematronResponseDTO(getLogTraceInfo()), HttpStatus.NO_CONTENT); 
		} 
		
	}
	

	@Override
	public ResponseEntity<SchematronResponseDTO> deleteSchematron(HttpServletRequest request, String templateIdRoot, String templateIdExtension) throws DocumentNotFoundException, OperationException {
		
		log.info(Constants.Logs.CALLED_API_DELETE_SCHEMATRON); 
		
		
		boolean existsSchematron = schematronService.deleteSchematron(templateIdRoot, templateIdExtension); 	
		
		if(existsSchematron) {
			return new ResponseEntity<>(new SchematronResponseDTO(getLogTraceInfo()), HttpStatus.OK); 
		} 
		else {
			throw new DocumentNotFoundException(Constants.Logs.ERROR_DOCUMENT_NOT_FOUND); 
		}
	} 
	
	@Override
	public ResponseEntity<SchematronDTO> getSchematronByTemplateIdRootAndTemplateIdExtension(HttpServletRequest request, 
			String templateIdRoot, String templateIdExtension) throws DocumentNotFoundException, OperationException {
		
		log.info(Constants.Logs.CALLED_API_QUERY_ROOT_EXTENSION); 
				
		SchematronDTO response =  schematronService.findByTemplateIdRootAndTemplateIdExtension(templateIdRoot, templateIdExtension); 
				
		return ResponseEntity.status(HttpStatus.OK).body(response);
	} 
	
	@Override
	public ResponseEntity<SchematronsDTO> getSchematrons(HttpServletRequest request) {
		
		log.info(Constants.Logs.CALLED_API_GET_SCHEMATRON);  
		
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
	public ResponseEntity<GetDocumentResDTO> getSchematronById(HttpServletRequest request,  String id) throws OperationException, DocumentNotFoundException {

		log.info(Constants.Logs.CALLED_API_QUERY_ID); 

		SchematronDocumentDTO doc = schematronService.findById(id); 

		return new ResponseEntity<>(new GetDocumentResDTO(getLogTraceInfo(), doc), HttpStatus.OK);

	}
	
	
}
