package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.service;

import java.util.List;

import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.SchematronDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.SchematronDocumentDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.repository.entity.SchematronETY;

/**
    @author Riccardo Bonesi
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
	SchematronETY insert(SchematronDTO ety) throws OperationException, DocumentAlreadyPresentException, DocumentNotFoundException;

	/**
	 * Update a Schematron entity.
	 * @param ety
	 * @return boolean
	 * @throws OperationException 
	 */
	boolean update(SchematronDTO ety) throws OperationException; 

	/**
	 * Delete a Schematron by templateIdRoot and templateIdExtension.
	 * @param templateIdRoot
	 * @param templateIdExtension
	 * @return boolean
	 * @throws DocumentNotFoundException 
	 * @throws OperationException 
	 */
	boolean deleteSchematron(String templateIdRoot, String templateIdExtension) throws DocumentNotFoundException, OperationException; 
	
	/**
	 * Find a schematron by Template Id Root and Template Id Extension.
	 * @param templateIdRoot
	 * @param templateIdExtension
	 * @return SchematronDTO
	 * @throws DocumentNotFoundException 
	 * @throws OperationException 
	 * @throws EmptyParameterException 
	 */
	SchematronDTO findByTemplateIdRootAndTemplateIdExtension(String templateIdRoot, String templateIdExtension) throws DocumentNotFoundException, OperationException; 

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
	 * @throws ObjectIdNotValidException
	 */
	SchematronDocumentDTO findById(String id) throws OperationException, DocumentNotFoundException;
	
}
