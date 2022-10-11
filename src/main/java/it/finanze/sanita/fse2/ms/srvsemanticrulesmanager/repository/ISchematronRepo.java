package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.repository;

import java.util.List;

import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.repository.entity.SchematronETY;

/**
 *	@author vincenzoingenito
	@author Riccardo Bonesi
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
	 * Update a schematron on database: set as deleted the old version and insert a new one in database
	 * 
	 * @param ety Schematron to update.
	 * @return Schematron updated.
	 * @throws OperationException 
	 */
	boolean update(SchematronETY ety) throws OperationException;
	
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
	 * Deletes a Schematron identified by its {@code template_id_root} and its {@code template_id_extension}.
	 * 
	 * @param template_id_root, template_id_extension Primary key of the Schematron to return.
	 * @throws OperationException 
	 */
	boolean logicallyRemoveSchematron(String templateIdRoot, String templateIdExtension) throws OperationException;

	/**
	 * Returns all schematrons.
	 * 
	 * @return List of all schematrons.
	 */
	List<SchematronETY> findAll();

	
	boolean existByTemplateIdRoot(String templateIdRoot) throws OperationException;

	/**
     * Retrieves all the not-deleted extensions with their documents data
     *
     * @return Any available schematron
     * @throws OperationException If a data-layer error occurs
     */
    List<SchematronETY> getEveryActiveSchematron() throws OperationException;
}
