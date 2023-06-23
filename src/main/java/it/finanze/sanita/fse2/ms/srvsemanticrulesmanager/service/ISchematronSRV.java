/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 * 
 * Copyright (C) 2023 Ministero della Salute
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.service;

import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.SchematronDocumentDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.InvalidVersionException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.repository.entity.SchematronETY;

import java.util.List;

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
	int deleteSchematron(String templateIdRoot, String system) throws DocumentNotFoundException, OperationException;
	
	/**
	 * Find a schematron by Template Id Root and Template Id Extension.
	 * @param templateIdRoot
	 * @param version
	 * @return SchematronDTO
	 * @throws DocumentNotFoundException 
	 * @throws OperationException 
	 */
	SchematronDocumentDTO findByTemplateIdRootAndVersion(String templateIdRoot, String version) throws DocumentNotFoundException, OperationException;

	List<SchematronDocumentDTO> findByTemplateIdRoot(String templateIdRoot, boolean deleted)  throws DocumentNotFoundException, OperationException;

	/**
	 * Finds all schematrons.
	 * @return List<SchematronDTO>
	 */
	List<SchematronDocumentDTO> getSchematrons(boolean deleted) throws OperationException;

	/**
     * Retrieves the Schematron by identifier
     *
     * @param id
     * @return SchematronDocumentDTO
     * @throws OperationException
     * @throws DocumentNotFoundException
     */
	SchematronDocumentDTO findById(String id) throws OperationException, DocumentNotFoundException;
	
}
