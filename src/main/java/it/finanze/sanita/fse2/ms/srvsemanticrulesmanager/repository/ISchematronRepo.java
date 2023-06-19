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
package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.repository;

import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.repository.entity.SchematronETY;

import java.util.List;

/**
 *
 *	Schemamatron interface repository.
 */
public interface ISchematronRepo extends IChangeSetRepo<SchematronETY> {

	/**
	 * Insert a schematron on database.
	 * 
	 * @param ety Schematron to insert.
	 * @return Schematron inserted.
	 * @throws OperationException 
	 */
	SchematronETY insert(SchematronETY ety) throws OperationException;
	
	/**
	 * Returns a Schematron identified by its {@code templateIdRoot} and {@code version}.
	 * 
	 * @param templateIdRoot, version Primary key of the Schematron to return.
	 * @return Schematron identified by its {@code templateIdRoot} and {@code version}.
	 * @throws OperationException 
	 * @throws DocumentNotFoundException 
	 */
	SchematronETY findByTemplateIdRootAndVersion(String templateIdRoot, String version) throws OperationException, DocumentNotFoundException;

	/**
	 * Returns a Schematron identified by its {@code id}
	 * @param id
	 * @return SchematronETY
	 * @throws OperationException
	 */
	SchematronETY findById(String id) throws OperationException;
	
	/**
	 * Deletes a Schematron identified by its {@code template_id_root}
	 * 
	 * @param templateIdRoot, template_id_extension Primary key of the Schematron to return.
	 * @throws OperationException 
	 */
	int deleteByTemplateIdRoot(String templateIdRoot) throws OperationException;

	/**
	 * Returns all schematrons.
	 * 
	 * @return List of all schematrons.
	 */
	List<SchematronETY> findDocs() throws OperationException;

	/**
     * Retrieves all the not-deleted extensions with their documents data
     *
     * @return Any available schematron
     * @throws OperationException If a data-layer error occurs
     */
    List<SchematronETY> getEveryActiveSchematron() throws OperationException;

	/**
	 * Find schematron by templateIdRoot
	 * @param templateIdRoot
	 * @return
	 * @throws OperationException
	 */
	SchematronETY findLatestByTemplateIdRoot(String templateIdRoot) throws OperationException;
	
	boolean checkExist(String templateIdRoot,String version);

	List<SchematronETY> findByTemplateIdRoot(String templateIdRoot, boolean deleted) throws OperationException;
}
