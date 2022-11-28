/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.MongoException;

import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.ChangeSetDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.SchematronDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.SchematronDocumentDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.BusinessException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.InvalidVersionException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.repository.entity.SchematronETY;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.repository.mongo.impl.SchematronRepo;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.service.ISchematronSRV;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.utility.ChangeSetUtility;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.utility.ValidationUtility;
import lombok.extern.slf4j.Slf4j;


/**
 *
 *	Schematron service.
 */
@Service
@Slf4j
public class SchematronSRV implements ISchematronSRV {

	@Autowired
	private SchematronRepo schematronRepo;
	
	@Override
	public SchematronETY insert(final SchematronDTO dto) throws OperationException, DocumentAlreadyPresentException, DocumentNotFoundException {
		try {
			SchematronETY ety = parseDtoToEty(dto); 
			SchematronETY schematronIfPresent = schematronRepo.findByTemplateIdRootAndVersion(ety.getTemplateIdRoot(), ety.getVersion()); 
		
			if (schematronIfPresent != null) {
				log.error("Error: schematron already present in the database");
				throw new DocumentAlreadyPresentException("Error: schematron already present in the database"); 
			}
		
			return schematronRepo.insert(ety); 
		} catch(MongoException ex) {
			log.error("Error inserting all ety schematron :" , ex);
			throw new OperationException("Error inserting all ety schematron :" , ex);
		}
	}
	
	@Override
	public void update(SchematronDTO dto) throws OperationException, InvalidVersionException, DocumentNotFoundException {
		SchematronETY ety = parseDtoToEty(dto);

		SchematronETY lastSchematron = schematronRepo.findByTemplateIdRoot(dto.getTemplateIdRoot());
		
		if (lastSchematron != null) {
			if (ValidationUtility.isMajorVersion(dto.getVersion(), lastSchematron.getVersion())) {
				schematronRepo.logicallyRemoveSchematron(lastSchematron.getTemplateIdRoot(), lastSchematron.getVersion());
				schematronRepo.insert(ety);
			} else {
				throw new InvalidVersionException(String.format("Invalid version: %s. The version must be greater than %s", dto.getVersion(), lastSchematron.getVersion()));
			}
		} else {
			throw new DocumentNotFoundException(String.format("Document with templateIdRoot: %s not found", dto.getTemplateIdRoot()));
		}
	}
	
	@Override
	public SchematronDTO findByTemplateIdRootAndVersion(final String templateIdRoot,final String version) throws DocumentNotFoundException, OperationException {
		SchematronETY output = schematronRepo.findByTemplateIdRootAndVersion(templateIdRoot, version);
		if (output == null) {
			throw new DocumentNotFoundException(Constants.Logs.ERROR_DOCUMENT_NOT_FOUND); 
		} 
		
		return parseEtyToDto(output); 
	}

	@Override
	public SchematronDocumentDTO findById(String id) throws OperationException, DocumentNotFoundException {
		SchematronETY output = schematronRepo.findById(id);

        if (output == null) {
            throw new DocumentNotFoundException(Constants.Logs.ERROR_REQUESTED_DOCUMENT_DOES_NOT_EXIST);
        }
		
		return SchematronDocumentDTO.fromEntity(output);
	}
	
	@Override
	public boolean deleteSchematron(String templateIdRoot, String templateIdExtension) throws DocumentNotFoundException, OperationException {
		try {
			return schematronRepo.logicallyRemoveSchematron(templateIdRoot, templateIdExtension); 
		} catch(MongoException e) {
			throw new OperationException(e.getMessage(), e); 
		}
	}
	
	@Override
	public List<SchematronDTO> getSchematrons() {
		List<SchematronETY> etyList = schematronRepo.findAll(); 
		if (etyList == null || etyList.isEmpty()) {
			return new ArrayList<>();
		}
		return buildDtoFromEty(etyList); 
	}
	
	
	public List<SchematronDTO> buildDtoFromEty(List<SchematronETY> schematronEtyList) {
		List<SchematronDTO> output = new ArrayList<>();
		
		for(SchematronETY schematron : schematronEtyList) {
			output.add(parseEtyToDto(schematron));
		}
		
		return output;
	} 
	
	public SchematronDTO parseEtyToDto(SchematronETY schematronEty) {
		SchematronDTO output = new SchematronDTO();
		output.setId(schematronEty.getId());
		output.setNameSchematron(schematronEty.getNameSchematron()); 
		output.setContentSchematron(schematronEty.getContentSchematron()); 
		output.setTemplateIdRoot(schematronEty.getTemplateIdRoot());
		output.setVersion(schematronEty.getVersion()); 
		output.setInsertionDate(schematronEty.getInsertionDate()); 
		output.setDeleted(schematronEty.isDeleted());
		
		if (schematronEty.getLastUpdateDate() != null) {
			output.setLastUpdateDate(schematronEty.getLastUpdateDate()); 
		} 

		return output;
	}
	
	public SchematronETY parseDtoToEty(SchematronDTO schematronDto) {
		SchematronETY output = new SchematronETY();
		output.setId(schematronDto.getId());
		output.setNameSchematron(schematronDto.getNameSchematron()); 
		output.setContentSchematron(schematronDto.getContentSchematron()); 
		output.setTemplateIdRoot(schematronDto.getTemplateIdRoot());
		output.setVersion(schematronDto.getVersion()); 
		output.setInsertionDate(schematronDto.getInsertionDate()); 
		output.setDeleted(schematronDto.isDeleted()); 
		
		if(schematronDto.getLastUpdateDate() != null) {
			output.setLastUpdateDate(schematronDto.getLastUpdateDate()); 
		} 

		return output;
	}

	@Override
	public List<ChangeSetDTO> getInsertions(Date lastUpdate) throws OperationException {

		List<SchematronETY> insertions;

		if (lastUpdate != null) {
			insertions = schematronRepo.getInsertions(lastUpdate);
		} else {
			insertions = schematronRepo.getEveryActiveSchematron();
		}

		return insertions.stream().map(ChangeSetUtility::schematronToChangeset).collect(Collectors.toList());

	}

    @Override
	public List<ChangeSetDTO> getDeletions(Date lastUpdate) throws OperationException {
    	List<ChangeSetDTO> deletions = new ArrayList<>();
		try {
			if (lastUpdate != null) {
				List<SchematronETY> deletionsETY = schematronRepo.getDeletions(lastUpdate);
				deletions = deletionsETY.stream().map(ChangeSetUtility::schematronToChangeset)
						.collect(Collectors.toList());
			}
		} catch (Exception e) {
			log.error("Error retrieving deletions", e); 
			throw new BusinessException("Error retrieving deletions", e); 
		}
		return deletions;
	}



	
}
