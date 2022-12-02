/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.controller.impl;

import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.controller.AbstractCTL;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.controller.ISchematronCTL;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.SchematronDocumentDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.impl.GetDocumentResDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.impl.SchematronResponseDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.impl.SchematronsDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.*;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.repository.entity.SchematronETY;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.service.ISchematronSRV;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.validators.schematron.SchematronValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 */
@RestController
@Slf4j
public class SchematronCTL extends AbstractCTL implements ISchematronCTL {

	@Autowired
	private ISchematronSRV service;

	@Override
	public SchematronResponseDTO addSchematron(String templateIdRoot, String version, MultipartFile file) throws IOException,
		OperationException, DocumentAlreadyPresentException, InvalidContentException, SchematronValidatorException {

		// Check file consistency
		if (isValidFile(file)) {
			// Verify integrity
			SchematronValidator.verify(file);
			// Insert into database
			service.insert(SchematronETY.fromMultipart(templateIdRoot, version, file));
		} else {
			throw new InvalidContentException("One or more files appear to be invalid");
		}

		return new SchematronResponseDTO(getLogTraceInfo(), 1, null, null);
	}

	@Override
	public SchematronResponseDTO updateSchematron(String templateIdRoot, String version, MultipartFile file)
		throws IOException, OperationException, DocumentNotFoundException, InvalidContentException, InvalidVersionException, SchematronValidatorException, DocumentAlreadyPresentException {

		if (isValidFile(file)) {
			SchematronValidator.verify(file);
			service.update(SchematronETY.fromMultipart(templateIdRoot, version, file));
		} else {
			throw new InvalidContentException("One or more files appear to be invalid");
		}

		return new SchematronResponseDTO(getLogTraceInfo(), null, 1, null);
	}

	@Override
	public SchematronResponseDTO deleteSchematron(String templateIdRoot) throws DocumentNotFoundException, OperationException {
		return new SchematronResponseDTO(getLogTraceInfo(), null, null, service.deleteSchematron(templateIdRoot));
	}

	@Override
	public SchematronDocumentDTO getSchematronByTemplateIdRoot(String templateIdRoot) throws DocumentNotFoundException, OperationException {
		return service.findByTemplateIdRoot(templateIdRoot);
	}

	@Override
	public SchematronsDTO getSchematrons() {
		log.debug("Called GET /schematron");
		List<SchematronDocumentDTO> schematrons = service.getSchematrons();
		return new SchematronsDTO(getLogTraceInfo(), schematrons);
	}

	@Override
	public GetDocumentResDTO getSchematronById(String id) throws OperationException, DocumentNotFoundException {
		log.debug("Called GET /schematron by ID");
		return new GetDocumentResDTO(getLogTraceInfo(), service.findById(id));
	}

}
