/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.controller.impl;

import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.controller.AbstractCTL;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.controller.ISchematronCTL;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.SchematronDocumentDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.impl.CrudDocumentResDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.impl.GetDocumentResDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.impl.GetDocumentsResDTO;
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

import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.SchematronDocumentDTO.Options;

/**
 */
@RestController
@Slf4j
public class SchematronCTL extends AbstractCTL implements ISchematronCTL {

	@Autowired
	private ISchematronSRV service;

	@Override
	public CrudDocumentResDTO addSchematron(String templateIdRoot, String version, MultipartFile file) throws IOException,
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

		return new CrudDocumentResDTO(getLogTraceInfo(), 1, null, null);
	}

	@Override
	public CrudDocumentResDTO updateSchematron(String templateIdRoot, String version, MultipartFile file)
		throws IOException, OperationException, DocumentNotFoundException, InvalidContentException, InvalidVersionException, SchematronValidatorException, DocumentAlreadyPresentException {

		if (isValidFile(file)) {
			SchematronValidator.verify(file);
			service.update(SchematronETY.fromMultipart(templateIdRoot, version, file));
		} else {
			throw new InvalidContentException("One or more files appear to be invalid");
		}

		return new CrudDocumentResDTO(getLogTraceInfo(), null, 1, null);
	}

	@Override
	public CrudDocumentResDTO deleteSchematron(String templateIdRoot) throws DocumentNotFoundException, OperationException {
		return new CrudDocumentResDTO(getLogTraceInfo(), null, null, service.deleteSchematron(templateIdRoot));
	}

	@Override
	public SchematronDocumentDTO getSchematronByTemplateIdRoot(String templateIdRoot, boolean binary) throws DocumentNotFoundException, OperationException {
		return service.findByTemplateIdRoot(templateIdRoot).applyOptions(new Options(binary));
	}

	@Override
	public GetDocumentsResDTO getSchematrons(boolean binary) {
		log.debug("Called GET /schematron");
		List<SchematronDocumentDTO> schematrons = service.getSchematrons();
		return new GetDocumentsResDTO(getLogTraceInfo(), schematrons, new Options(binary));
	}

	@Override
	public GetDocumentResDTO getSchematronById(String id) throws OperationException, DocumentNotFoundException {
		log.debug("Called GET /schematron by ID");
		return new GetDocumentResDTO(getLogTraceInfo(), service.findById(id));
	}

}
