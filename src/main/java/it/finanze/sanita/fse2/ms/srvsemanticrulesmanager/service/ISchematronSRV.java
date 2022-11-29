/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.service;

import java.util.List;

import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.SchematronDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.SchematronDocumentDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.InvalidVersionException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.repository.entity.SchematronETY;

/**
 *
 *	Schematron interface service.
 */
public interface ISchematronSRV extends IChangeSetSRV {

	/**
	 * Create a new Schematron.
	 * @param ety
	 * @return 
	 * @throws OperationException 
	 * @throws DocumentAlreadyPresentException 
	 * @throws DocumentNotFoundException 
	 */
	SchematronETY insert(SchematronETY ety) throws OperationException, DocumentAlreadyPresentException;

	/**
	 * Update a Schematron entity.
	 * @throws OperationException
	 * @throws InvalidVersionException
	 * @throws DocumentNotFoundException
	 * @throws DocumentAlreadyPresentException
	 */
	void update(SchematronETY dto) throws OperationException, InvalidVersionException, DocumentNotFoundException, DocumentAlreadyPresentException;

	/**
	 * Delete a Schematron by templateIdRoot and templateIdExtension.
	 * @param templateIdRoot
	 * @return boolean
	 * @throws DocumentNotFoundException 
	 * @throws OperationException 
	 */
	int deleteSchematron(String templateIdRoot) throws DocumentNotFoundException, OperationException;
	
	/**
	 * Find a schematron by Template Id Root and Template Id Extension.
	 * @param templateIdRoot
	 * @param version
	 * @return SchematronDTO
	 * @throws DocumentNotFoundException 
	 * @throws OperationException 
	 */
	SchematronDTO findByTemplateIdRootAndVersion(String templateIdRoot, String version) throws DocumentNotFoundException, OperationException;

	SchematronDTO findByTemplateIdRoot(String templateIdRoot)  throws DocumentNotFoundException, OperationException;

	/**
	 * Finds all schematrons.
	 * @return List<SchematronDTO>
	 */
	List<SchematronDTO> getSchematrons();

	/**
	 * Retrieves the Schematron by identifier
	 * @param id
	 * @return SchematronDocumentDTO
	 * @throws OperationException
	 * @throws DocumentNotFoundException
	 */
	SchematronDocumentDTO findById(String id) throws OperationException, DocumentNotFoundException;
	
}
