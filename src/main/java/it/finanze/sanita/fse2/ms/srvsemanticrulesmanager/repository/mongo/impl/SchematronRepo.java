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
package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.repository.mongo.impl;


import com.mongodb.MongoException;
import com.mongodb.client.result.UpdateResult;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.repository.ISchematronRepo;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.repository.entity.SchematronETY;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.config.Constants.Logs.ERR_REP_COUNT_ACTIVE_DOC;
import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.repository.entity.SchematronETY.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 *
 *	Schematron repository.
 */
@Slf4j
@Repository
public class SchematronRepo implements ISchematronRepo {

	@Autowired
	private MongoTemplate mongo;
	 
		
	@Override
	public SchematronETY insert(SchematronETY ety) throws OperationException {
		SchematronETY sch;
		try {
			sch = mongo.insert(ety);
		} catch(MongoException ex) {
			throw new OperationException(Constants.Logs.ERROR_INSERT_SCHEMATRON + ety, ex);
		}
		return sch;
	}
	
	@Override
	public int delete(final String root, final String system) throws OperationException {
		// Working var
		UpdateResult res;
		// Create query
		Query query = query(
			where(FIELD_TEMPLATE_ID_ROOT).is(root).
			and(FIELD_SYSTEM).is(system).
			and(FIELD_DELETED).ne(true)
		);
		// Define update operation
		Update update = new Update();
		update.set(FIELD_LAST_UPDATE, new Date());
		update.set(FIELD_DELETED, true);
		// Execute
		try {
			 res = mongo.updateMulti(query, update, SchematronETY.class);
		} catch(MongoException ex) {
			throw new OperationException(Constants.Logs.ERROR_DELETE_SCHEMATRON + getClass(), ex);
		}
		return (int) res.getModifiedCount();
	}
	@Override
	public SchematronETY findByTemplateIdRootAndVersion(String templateIdRoot, String version) throws OperationException {
		try {
			return mongo.findOne(query(where(FIELD_TEMPLATE_ID_ROOT).is(templateIdRoot)
					.and(FIELD_VERSION).is(version).and(FIELD_DELETED).ne(true)), SchematronETY.class);
		} 
		catch(MongoException e) {
			log.error(Constants.Logs.ERROR_RETRIEVING_SCHEMATRON, e);
            throw new OperationException(Constants.Logs.ERROR_RETRIEVING_SCHEMATRON, e);
		}
	}

    @Override
    public SchematronETY findById(String id) throws OperationException {
        try {
            return mongo.findOne(query(where(FIELD_ID).is(new ObjectId(id)).and(FIELD_DELETED).ne(true)), SchematronETY.class);
        } catch (MongoException e) {
        	log.error(Constants.Logs.ERROR_RETRIEVING_SCHEMATRON, e);
            throw new OperationException(Constants.Logs.ERROR_RETRIEVING_SCHEMATRON, e);
        }
    }

	
	@Override
	public List<SchematronETY> findDocs() throws OperationException {
		List<SchematronETY> entities;
		try {
			entities = mongo.findAll(SchematronETY.class);
		}catch (MongoException e) {
			throw new OperationException(Constants.Logs.ERROR_RETRIEVING_SCHEMATRON, e);
		}
		return entities;
	}

	/**
     * Retrieves the latest insertions according to the given timeframe
     *
     * @param lastUpdate The timeframe to consider while calculating
     * @return The missing insertions
     * @throws OperationException If a data-layer error occurs
     */
    @Override
    public List<SchematronETY> getInsertions(Date lastUpdate) throws OperationException {
        // Working var
        List<SchematronETY> objects;
        // Create query
        Query q = query(
            where(FIELD_INSERTION_DATE).gt(lastUpdate).and(FIELD_DELETED).ne(true)
        );
        try {
            // Execute
            objects = mongo.find(q, SchematronETY.class);
        } catch (MongoException e) {
            // Catch data-layer runtime exceptions and turn into a checked exception
            throw new OperationException(Constants.Logs.ERROR_UNABLE_FIND_INSERTIONS, e);
        }
        return objects;
    }

    /**
     * Retrieves the latest deletions according to the given timeframe
     *
     * @param lastUpdate The timeframe to consider while calculating
     * @return The missing deletions
     * @throws OperationException If a data-layer error occurs
     */
    @Override
    public List<SchematronETY> getDeletions(Date lastUpdate) throws OperationException {
        // Working var
        List<SchematronETY> objects;
        // Create query
        Query q = query(
            where(FIELD_LAST_UPDATE).gt(lastUpdate)
                .and(FIELD_INSERTION_DATE).lte(lastUpdate)
                .and(FIELD_DELETED).is(true)
        );
        try {
            objects = mongo.find(q, SchematronETY.class);
        } catch (MongoException e) {
            throw new OperationException(Constants.Logs.ERROR_UNABLE_FIND_DELETIONS, e);
        }
        return objects;
    }

	/**
	 * Count all the not-deleted extensions items
	 *
	 * @return Number of active documents
	 * @throws OperationException If a data-layer error occurs
	 */
	@Override
	public long getActiveDocumentCount() throws OperationException {
		// Working var
		long size;
		// Create query
		Query q = query(where(FIELD_DELETED).ne(true));
		try {
			// Execute count
			size = mongo.count(q, SchematronETY.class);
		}catch (MongoException e) {
			// Catch data-layer runtime exceptions and turn into a checked exception
			throw new OperationException(ERR_REP_COUNT_ACTIVE_DOC, e);
		}
		return size;
	}

	/**
     * Retrieves all the not-deleted extensions with their data
     *
     * @return Any available schematron
     * @throws OperationException If a data-layer error occurs
     */
    @Override
    public List<SchematronETY> getEveryActiveSchematron() throws OperationException {
        List<SchematronETY> objects;
        try {
        	Query q = query(where(FIELD_DELETED).ne(true));
            objects = mongo.find(q, SchematronETY.class);
        } catch (MongoException e) {
        	log.error("Unable to retrieve every available extension with their documents", e);
            throw new OperationException("Unable to retrieve every available extension with their documents", e);
        }
        return objects;
    }
	
    @Override
    public SchematronETY findLatestByTemplateIdRoot(String templateIdRoot, String system) throws OperationException {
        SchematronETY out;
		try {
            Query query = new Query();
            query.addCriteria(
				where(FIELD_TEMPLATE_ID_ROOT).is(templateIdRoot).
				and(FIELD_SYSTEM).is(system).
				and(FIELD_DELETED).ne(true)
			);
            query.with(Sort.by(Direction.DESC, FIELD_INSERTION_DATE));
			out = mongo.findOne(query, SchematronETY.class);
		} catch (MongoException e) {
			log.error("Error encountered while retrieving schematron with templateIdRoot: " + templateIdRoot, e);
			throw new OperationException("Error encountered while retrieving schematron with templateIdRoot: " + templateIdRoot, e);
		}
		return out;
    }

	@Override
	public List<SchematronETY> findByTemplateIdRoot(String templateIdRoot, boolean deleted) throws OperationException {
		List<SchematronETY> entities;
		// Search by template id
		Query q = query(where(FIELD_TEMPLATE_ID_ROOT).is(templateIdRoot));
		// Check if deleted are not allowed
		if(!deleted) q.addCriteria(where(FIELD_DELETED).ne(true));
		// Sort by insertion
		q = q.with(Sort.by(Direction.DESC, FIELD_INSERTION_DATE));
		try {
			entities = mongo.find(q, SchematronETY.class);
		} catch (MongoException e) {
			throw new OperationException("Error encountered while retrieving schematron with templateIdRoot: " + templateIdRoot, e);
		}
		return entities;
	}
}
