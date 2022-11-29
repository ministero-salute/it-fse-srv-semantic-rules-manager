/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.controller.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.*;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.validators.schematron.SchematronValidator;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.controller.AbstractCTL;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.controller.ISchematronCTL;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.SchematronDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.SchematronDocumentDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.GetDocumentResDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.SchematronsDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.SchematronResponseDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.service.ISchematronSRV;
import lombok.extern.slf4j.Slf4j;

/**
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
	public ResponseEntity<SchematronResponseDTO> addSchematron(String templateIdRoot, String version,
															   MultipartFile file, HttpServletRequest request) throws IOException,
		OperationException, DocumentAlreadyPresentException, DocumentNotFoundException, InvalidContentException, SchematronValidatorException {

		log.debug("Called POST /schematron");

		Date date = new Date();
		if (isValidFile(file)) {
			SchematronValidator.verify(file);
			SchematronDTO schematronDTO = new SchematronDTO();
			schematronDTO.setContentSchematron(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
			schematronDTO.setNameSchematron(file.getOriginalFilename());
			schematronDTO.setTemplateIdRoot(templateIdRoot);
			schematronDTO.setVersion(version);
			schematronDTO.setInsertionDate(date);
			schematronDTO.setLastUpdateDate(date);
			schematronService.insert(schematronDTO);
			return new ResponseEntity<>(new SchematronResponseDTO(getLogTraceInfo(), 1, null, null), HttpStatus.CREATED);
		} else {
			throw new InvalidContentException("One or more files appear to be invalid");
		}
	}

	@Override
	public ResponseEntity<SchematronResponseDTO> updateSchematron(String templateIdRoot, String version,
			MultipartFile file, HttpServletRequest request)
		throws IOException, OperationException, DocumentNotFoundException, InvalidContentException, InvalidVersionException, SchematronValidatorException, DocumentAlreadyPresentException {

		Date date = new Date();
		if (isValidFile(file)) {
			SchematronValidator.verify(file);
			SchematronDTO schematron = new SchematronDTO();
			schematron.setContentSchematron(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
			schematron.setNameSchematron(file.getOriginalFilename());
			schematron.setTemplateIdRoot(templateIdRoot);
			schematron.setVersion(version);
			schematron.setInsertionDate(date);
			schematron.setLastUpdateDate(date);
			schematronService.update(schematron);
		} else {
			throw new InvalidContentException("One or more files appear to be invalid");
		}

		return new ResponseEntity<>(new SchematronResponseDTO(getLogTraceInfo(), null, 1, null), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<SchematronResponseDTO> deleteSchematron(String templateIdRoot, String version,
			HttpServletRequest request) throws DocumentNotFoundException, OperationException {
		log.debug("Called DELETE /schematron");
		boolean existsSchematron = schematronService.deleteSchematron(templateIdRoot, version);

		if (existsSchematron) {
			return new ResponseEntity<>(new SchematronResponseDTO(getLogTraceInfo(), null, null, 1), HttpStatus.OK);
		} else {
			throw new DocumentNotFoundException(Constants.Logs.ERROR_DOCUMENT_NOT_FOUND);
		}
	}

	@Override
	public ResponseEntity<SchematronDTO> getSchematronByTemplateIdRootAndTemplateIdExtension(
			String templateIdRoot, String version, HttpServletRequest request)
			throws DocumentNotFoundException, OperationException {

		log.debug("Called GET /schematron by ID Root and Version");
		SchematronDTO response = schematronService.findByTemplateIdRootAndVersion(templateIdRoot, version);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@Override
	public ResponseEntity<SchematronsDTO> getSchematrons(HttpServletRequest request) {
		log.debug("Called GET /schematron");
		List<SchematronDTO> schematrons = schematronService.getSchematrons();
		return new ResponseEntity<>(new SchematronsDTO(getLogTraceInfo(), schematrons), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<GetDocumentResDTO> getSchematronById(HttpServletRequest request, String id)
			throws OperationException, DocumentNotFoundException {
		log.debug("Called GET /schematron by ID");
		SchematronDocumentDTO doc = schematronService.findById(id);
		return new ResponseEntity<>(new GetDocumentResDTO(getLogTraceInfo(), doc), HttpStatus.OK);
	}

}
