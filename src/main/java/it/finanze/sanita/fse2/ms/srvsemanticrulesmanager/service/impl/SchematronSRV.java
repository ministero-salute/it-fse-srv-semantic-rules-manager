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
package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.service.impl;

import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.SchematronDocumentDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.changes.ChangeSetDTO;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 *
 *	Schematron service.
 */
@Service
@Slf4j
public class SchematronSRV implements ISchematronSRV {

	@Autowired
	private SchematronRepo repository;
	
	@Override
	public SchematronETY insert(final SchematronETY ety) throws OperationException, DocumentAlreadyPresentException {

		SchematronETY schematronIfPresent = repository.findLatestByTemplateIdRoot(ety.getTemplateIdRoot());

		if (schematronIfPresent != null) {
			throw new DocumentAlreadyPresentException("Error: schematron already present in the database");
		}

		return repository.insert(ety);
	}
	
	@Override
	public void update(SchematronETY dto) throws OperationException, InvalidVersionException, DocumentNotFoundException, DocumentAlreadyPresentException {

		SchematronETY lastSchematron = repository.findLatestByTemplateIdRoot(dto.getTemplateIdRoot());

		if (lastSchematron != null) {
			if (ValidationUtility.isMajorVersion(dto.getVersion(), lastSchematron.getVersion())) {
				if(!repository.checkExist(dto.getTemplateIdRoot(), dto.getVersion())) {
					repository.deleteByTemplateIdRoot(lastSchematron.getTemplateIdRoot());
					repository.insert(dto);
				} else {
					throw new DocumentAlreadyPresentException("File già presente");
				}

			} else {
				throw new InvalidVersionException(String.format("Invalid version: %s. The version must be greater than %s", dto.getVersion(), lastSchematron.getVersion()));
			}
		} else {
			throw new DocumentNotFoundException(String.format("Document with templateIdRoot: %s not found", dto.getTemplateIdRoot()));
		}
	}
	
	@Override
	public SchematronDocumentDTO findByTemplateIdRootAndVersion(final String templateIdRoot, final String version) throws DocumentNotFoundException, OperationException {
		SchematronETY output = repository.findByTemplateIdRootAndVersion(templateIdRoot, version);
		if (output == null) {
			throw new DocumentNotFoundException(Constants.Logs.ERROR_DOCUMENT_NOT_FOUND); 
		} 
		
		return SchematronDocumentDTO.fromEntity(output);
	}

	@Override
	public List<SchematronDocumentDTO> findByTemplateIdRoot(String templateIdRoot, boolean deleted) throws OperationException, DocumentNotFoundException {
		List<SchematronETY> entities = repository.findByTemplateIdRoot(templateIdRoot, deleted);
		if(entities.isEmpty()) throw new DocumentNotFoundException(Constants.Logs.ERROR_DOCUMENT_NOT_FOUND);
		return entities.stream().map(SchematronDocumentDTO::fromEntity).collect(Collectors.toList());
	}

	@Override
	public SchematronDocumentDTO findById(String id) throws OperationException, DocumentNotFoundException {
		SchematronETY output = repository.findById(id);

        if (output == null) {
            throw new DocumentNotFoundException(Constants.Logs.ERROR_REQUESTED_DOCUMENT_DOES_NOT_EXIST);
        }
		
		return SchematronDocumentDTO.fromEntity(output);
	}
	
	@Override
	public int deleteSchematron(String templateIdRoot) throws DocumentNotFoundException, OperationException {
		// Check existence
		SchematronETY root = repository.findLatestByTemplateIdRoot(templateIdRoot);
		if(root == null) throw new DocumentNotFoundException(Constants.Logs.ERROR_DOCUMENT_NOT_FOUND);
		// Execute query
		return repository.deleteByTemplateIdRoot(templateIdRoot);
	}
	
	@Override
	public List<SchematronDocumentDTO> getSchematrons(boolean deleted) throws OperationException {
		List<SchematronETY> entities;
		if (deleted) {
			entities = repository.findDocs();
		} else {
			entities = repository.getEveryActiveSchematron();
		}
		return entities.stream().map(SchematronDocumentDTO::fromEntity).collect(Collectors.toList());
	}

	@Override
	public List<ChangeSetDTO> getInsertions(Date lastUpdate) throws OperationException {

		List<SchematronETY> insertions;

		if (lastUpdate != null) {
			insertions = repository.getInsertions(lastUpdate);
		} else {
			insertions = repository.getEveryActiveSchematron();
		}

		return insertions.stream().map(ChangeSetUtility::schematronToChangeset).collect(Collectors.toList());

	}

    @Override
	public List<ChangeSetDTO> getDeletions(Date lastUpdate) throws OperationException {

    	List<ChangeSetDTO> deletions = new ArrayList<>();

		if (lastUpdate != null) {
			List<SchematronETY> deletionsETY = repository.getDeletions(lastUpdate);
			deletions = deletionsETY.stream().map(ChangeSetUtility::schematronToChangeset).collect(Collectors.toList());
		}

		return deletions;
	}

	@Override
	public long getCollectionSize() throws OperationException {
		return repository.getActiveDocumentCount();
	}


}
