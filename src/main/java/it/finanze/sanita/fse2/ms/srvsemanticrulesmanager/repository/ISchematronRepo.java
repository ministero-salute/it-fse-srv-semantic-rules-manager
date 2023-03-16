/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
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
	int delete(String root, String system) throws OperationException;

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
     *
     * @param templateIdRoot
     * @param system
     * @return
     * @throws OperationException
     */
	SchematronETY findLatestByTemplateIdRoot(String templateIdRoot, String system) throws OperationException;

	List<SchematronETY> findByTemplateIdRoot(String templateIdRoot, boolean deleted) throws OperationException;
}
