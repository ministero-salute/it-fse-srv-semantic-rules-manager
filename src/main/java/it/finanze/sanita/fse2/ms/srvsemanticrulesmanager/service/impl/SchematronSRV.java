package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.bson.types.ObjectId;
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
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.ObjectIdNotValidException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.repository.entity.SchematronETY;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.repository.mongo.impl.SchematronRepo;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.service.ISchematronSRV;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.utility.ChangeSetUtility;
import lombok.extern.slf4j.Slf4j;


/**
 *	@author vincenzoingenito
 *  @author Guido Rocco
	@author Riccardo Bonesi
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
			
			SchematronETY schematronIfPresent = schematronRepo.findByTemplateIdRootAndTemplateIdExtension(
						ety.getTemplateIdRoot(), ety.getTemplateIdExtension()); 
			
			if(!ObjectUtils.isEmpty(schematronIfPresent.getId())) {
				log.error(Constants.Logs.ERROR_SCHEMATRON_ALREADY_PRESENT);
				throw new DocumentAlreadyPresentException(Constants.Logs.ERROR_SCHEMATRON_ALREADY_PRESENT); 
			}
			
			return schematronRepo.insert(ety); 
		} catch(MongoException ex) {
			log.error(Constants.Logs.ERROR_INSERT_SCHEMATRON , ex);
			throw new OperationException(Constants.Logs.ERROR_INSERT_SCHEMATRON , ex);
		}
		
	}
	
	@Override
	public boolean update(SchematronDTO dto) throws OperationException {
			SchematronETY ety = parseDtoToEty(dto); 
			return schematronRepo.update(ety); 
	} 
	
	@Override
	public SchematronDTO findByTemplateIdRootAndTemplateIdExtension(final String templateIdRoot, 
			final String templateIdExtension) throws DocumentNotFoundException, OperationException {
		
		SchematronETY output = schematronRepo.findByTemplateIdRootAndTemplateIdExtension(templateIdRoot, templateIdExtension); 
		
		if(ObjectUtils.isEmpty(output.getId())) {
			throw new DocumentNotFoundException(Constants.Logs.ERROR_DOCUMENT_NOT_FOUND); 
		} 
		
		return parseEtyToDto(output); 
	}

	@Override
	public SchematronDocumentDTO findById(String id) throws OperationException, DocumentNotFoundException, ObjectIdNotValidException {
		
		if(!ObjectId.isValid(id)) {
            throw new ObjectIdNotValidException(Constants.Logs.ERROR_INVALID_OBJECT_ID + id);
        }

		SchematronETY output = schematronRepo.findById(id);

        if (output == null) {
            throw new DocumentNotFoundException(Constants.Logs.ERROR_REQUESTED_DOCUMENT_DOES_NOT_EXIST);
        }
		
		return SchematronDocumentDTO.fromEntity(output);
	}
	
	@Override
	public boolean deleteSchematron(String templateIdRoot, String templateIdExtension) throws DocumentNotFoundException, OperationException {
		try {
			return schematronRepo.removeSchematron(templateIdRoot, templateIdExtension); 
		} 
		catch(MongoException e) {
			throw new OperationException(e.getMessage(), e); 
		}
	}
	
	@Override
	public List<SchematronDTO> getSchematrons() {
			List<SchematronETY> etyList = schematronRepo.findAll(); 
			return buildDtoFromEty(etyList); 
	}
	
	
	public List<SchematronDTO> buildDtoFromEty(List<SchematronETY> schematronEtyList) {
		List<SchematronDTO> output = new ArrayList<>();
		
		for(SchematronETY schematron : schematronEtyList) {
			output.add(parseEtyToDto(schematron));
		}
		
		return output;
	} 
	
	private List<SchematronETY> buildEtyFromDto(List<SchematronDTO> schematronDtoList) {
		List<SchematronETY> output = new ArrayList<>();
		
		for(SchematronDTO schematron : schematronDtoList) {
			output.add(parseDtoToEty(schematron)); 
		}
		
		return output;
	}
	
	public SchematronDTO parseEtyToDto(SchematronETY schematronEty) {
		SchematronDTO output = new SchematronDTO();
		output.setId(schematronEty.getId());
		output.setNameSchematron(schematronEty.getNameSchematron()); 
		output.setContentSchematron(schematronEty.getContentSchematron()); 
		output.setTemplateIdRoot(schematronEty.getTemplateIdRoot());
		output.setTemplateIdExtension(schematronEty.getTemplateIdExtension()); 
		output.setInsertionDate(schematronEty.getInsertionDate()); 
		
		if(schematronEty.getLastUpdateDate() != null) {
			output.setLastUpdateDate(schematronEty.getLastUpdateDate()); 
		} 
		if(schematronEty.getDeleted() != null) {
			output.setDeleted(schematronEty.getDeleted()); 
		}

		return output;
	}
	
	public SchematronETY parseDtoToEty(SchematronDTO schematronDto) {
		SchematronETY output = new SchematronETY();
		output.setId(schematronDto.getId());
		output.setNameSchematron(schematronDto.getNameSchematron()); 
		output.setContentSchematron(schematronDto.getContentSchematron()); 
		output.setTemplateIdRoot(schematronDto.getTemplateIdRoot());
		output.setTemplateIdExtension(schematronDto.getTemplateIdExtension()); 
		output.setInsertionDate(schematronDto.getInsertionDate()); 
		
		if(schematronDto.getLastUpdateDate() != null) {
			output.setLastUpdateDate(schematronDto.getLastUpdateDate()); 
		} 
		if(schematronDto.getDeleted() != null) {
			output.setDeleted(schematronDto.getDeleted()); 
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
		try {

			List<ChangeSetDTO> deletions = new ArrayList<>();

			if (lastUpdate != null) {
				List<SchematronETY> deletionsETY = schematronRepo.getDeletions(lastUpdate);
				deletions = deletionsETY.stream().map(ChangeSetUtility::schematronToChangeset)
						.collect(Collectors.toList());

			}

			return deletions;

		} catch (Exception e) {
			log.error(Constants.Logs.ERROR_RETRIEVING_DELETIONS, e); 
			throw new BusinessException(Constants.Logs.ERROR_RETRIEVING_DELETIONS, e); 
		}
	}

    @Override
	public List<ChangeSetDTO> getModifications(Date lastUpdate) throws OperationException {
		try {

			List<ChangeSetDTO> modifications = new ArrayList<>();

			if (lastUpdate != null) {
				List<SchematronETY> modificationsETY = schematronRepo.getModifications(lastUpdate);
				modifications = modificationsETY.stream().map(ChangeSetUtility::schematronToChangeset)
						.collect(Collectors.toList());

			} 
			
			return modifications; 

        } catch (Exception e) {
            log.error(Constants.Logs.ERROR_RETRIEVING_MODIFICATIONS, e);
            throw new BusinessException(Constants.Logs.ERROR_RETRIEVING_MODIFICATIONS, e);
        }
    }



	
}
