package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.repository.mongo.impl;



import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.MongoException;

import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.BusinessException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.repository.ISchematronRepo;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.repository.entity.SchematronETY;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.utility.ProfileUtility;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Guido Rocco
 * @author Riccardo Bonesi
 *
 *	Schematron repository.
 */
@Slf4j
@Repository
public class SchematronRepo implements ISchematronRepo, Serializable {
	
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8314432048647769361L; 
	
	@Autowired
	private transient MongoTemplate mongoTemplate; 
	
	@Autowired
	private transient ProfileUtility profileUtility; 
		
	String collection = Constants.ComponentScan.COLLECTION_NAME; 
	
	
	@Override
	public SchematronETY insert(SchematronETY ety) throws OperationException {
		try {
			return mongoTemplate.insert(ety, getCollectionName());
		} catch(MongoException ex) {
			log.error(Constants.Logs.ERROR_INSERTING_ETYS + ety , ex);
			throw new OperationException(Constants.Logs.ERROR_INSERTING_ETYS + ety , ex);
		} catch(Exception ex) {
			log.error(Constants.Logs.ERROR_INSERTING_ETYS, ex);
			throw new BusinessException(Constants.Logs.ERROR_INSERTING_ETYS, ex);
		}
	}
	
	@Override
	public boolean update(SchematronETY ety) throws OperationException {

        boolean removed = removeSchematron(ety.getTemplateIdRoot(), ety.getTemplateIdExtension());

        if(removed){
            SchematronETY inserted = insert(ety);
            return inserted!=null;
        } else {
            return false;
        }
        
	}
	
	@Override
	public boolean removeSchematron(final String templateIdRoot, final String templateIdExtension) throws OperationException {
		Query query = Query.query(Criteria.where(Constants.App.TEMPLATE_ID_ROOT).is(templateIdRoot)
				.and(Constants.App.TEMPLATE_ID_EXTENSION).is(templateIdExtension)); 
		
		// Template ID Root and Extension uniquely determine a Schematron, we can then use findOne and take the first element s
		SchematronETY schematron = mongoTemplate.findOne(query, SchematronETY.class, getCollectionName()); 
		
		if(schematron != null) {
			Update update = new Update(); 
			update.set(FIELD_DELETED, true);
            update.set(FIELD_LAST_UPDATE, new Date());
			
			try {
				mongoTemplate.updateFirst(Query.query(Criteria.where(Constants.App.TEMPLATE_ID_ROOT).is(schematron.getTemplateIdRoot())
						.and(Constants.App.TEMPLATE_ID_EXTENSION).is(schematron.getTemplateIdExtension())
						.and(Constants.App.DELETED).ne(true)), update, getCollectionName()); 
			} catch(MongoException ex) {
				log.error(Constants.Logs.ERROR_DELETE_SCHEMATRON + getClass() , ex);
				throw new OperationException(Constants.Logs.ERROR_DELETE_SCHEMATRON + getClass(), ex);
			}

			return true; 
		}
		return false;
	}
	
	@Override
	public SchematronETY findByTemplateIdRootAndTemplateIdExtension(String templateIdRoot, String templateIdExtension) throws OperationException, DocumentNotFoundException {
		try {
			List<SchematronETY>  etyList = mongoTemplate.find(Query.query(Criteria.where(Constants.App.TEMPLATE_ID_ROOT).is(templateIdRoot)
					.and(Constants.App.TEMPLATE_ID_EXTENSION).is(templateIdExtension).and(Constants.App.DELETED).ne(true)), SchematronETY.class, getCollectionName()); 
			
			return etyList.isEmpty() ? new SchematronETY() : etyList.get(0); 
		} 
		catch(MongoException e) {
            throw new OperationException(Constants.Logs.ERROR_RETRIEVING_SCHEMATRON, e); 
		}

	}

    @Override
    public SchematronETY findById(String id) throws OperationException {
        SchematronETY object = null;

        try {
            object =  mongoTemplate.findOne(Query.query(Criteria.where(Constants.App.MONGO_ID).is(new ObjectId(id)).and(Constants.App.DELETED).ne(true)), SchematronETY.class, getCollectionName()); 
        } catch (MongoException e) {
            throw new OperationException(Constants.Logs.ERROR_RETRIEVING_SCHEMATRON, e);
        }
        return object;
    }

	
	@Override
	public List<SchematronETY> findAll() {
		List<SchematronETY> etyList = mongoTemplate.findAll(SchematronETY.class, getCollectionName()); 
				
		return etyList.stream()
				.filter(i -> !i.isDeleted())
		        .collect(Collectors.toList()); 
	}

	@Override
	public boolean existByTemplateIdRoot(final String templateIdRoot) throws OperationException {
		boolean output = false;
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where(Constants.App.TEMPLATE_ID_ROOT).is(templateIdRoot));
			query.addCriteria(Criteria.where(Constants.App.DELETED).ne(true)); 
			output = mongoTemplate.exists(query, getCollectionName()); 
		} catch(MongoException ex) {
			log.error(Constants.Logs.ERROR_EXECUTE_EXIST_VERSION_QUERY + getClass() , ex);
			throw new OperationException(Constants.Logs.ERROR_EXECUTE_EXIST_VERSION_QUERY + getClass(), ex);
		}
		return output;
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
        Query q = Query.query(
            Criteria.where(FIELD_INSERTION_DATE).gt(lastUpdate).and(FIELD_DELETED).ne(true)
        );
        try {
            // Execute
            objects = mongoTemplate.find(q, SchematronETY.class, getCollectionName());
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
        Query q = Query.query(
            Criteria.where(FIELD_LAST_UPDATE).gt(lastUpdate)
                .and(FIELD_INSERTION_DATE).lte(lastUpdate)
                .and(FIELD_DELETED).is(true)
        );
        try {
            objects = mongoTemplate.find(q, SchematronETY.class, getCollectionName());
        } catch (MongoException e) {
            throw new OperationException(Constants.Logs.ERROR_UNABLE_FIND_DELETIONS, e);
        }
        return objects;
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
        Query q = Query.query(Criteria.where(FIELD_DELETED).ne(true)); 
        
        try {
            objects = mongoTemplate.find(q, SchematronETY.class, getCollectionName()); 
        } catch (MongoException e) {
            throw new OperationException(Constants.Logs.ERROR_UNABLE_RETRIEVE_EXTENSIONS, e);
        }
        return objects;
    }

	public String getCollectionName() {
		return profileUtility.isTestProfile() ?  Constants.Profile.TEST_PREFIX + '-' + Constants.ComponentScan.COLLECTION_NAME : Constants.ComponentScan.COLLECTION_NAME; 
	} 
	
	
	
}
