package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.config;

/**
 * 
 * @author vincenzoingenito
 *
 * Constants application.
 */
public final class Constants {

	/**
	 *	Path scan.
	 */
	public static final class ComponentScan {

		/**
		 * Base path.
		 */
		public static final String BASE = "it.sanita.srvsemanticrulesmanager";

		/**
		 * Controller path.
		 */
		public static final String CONTROLLER = "it.sanita.srvsemanticrulesmanager.controller";

		/**
		 * Service path.
		 */
		public static final String SERVICE = "it.sanita.srvsemanticrulesmanager.service";

		/**
		 * Configuration path.
		 */
		public static final String CONFIG = "it.sanita.srvsemanticrulesmanager.config";
		
		/**
		 * Configuration mongo path.
		 */
		public static final String CONFIG_MONGO = "it.sanita.srvsemanticrulesmanager.config.mongo";
		
		/**
		 * Configuration mongo repository path.
		 */
		public static final String REPOSITORY_MONGO = "it.sanita.srvsemanticrulesmanager.repository";	 
		
		/**
		 * The name of the MongoDB Collection 
		 */
		public static final String COLLECTION_NAME = "schematron";  

		
		
		private ComponentScan() {
			//This method is intentionally left blank.
		}

	}
	
	public static final class Headers {
		
		/**
		 * JWT header field.
		 */
		public static final String JWT_HEADER = "Authorization";

		private Headers() {
			//This method is intentionally left blank.
		}

	}
 
	public static final class Profile {
		
		public static final String TEST = "test"; 
		
		public static final String TEST_PREFIX = "test"; 


		/** 
		 * Constructor.
		 */
		private Profile() {
			//This method is intentionally left blank.
		}

	}

	public static final class App {
		
		public static final String JWT_TOKEN_AUDIENCE = "eds-semantic";

		public static final String JWT_TOKEN_TYPE = "JWT";

		public static final String BEARER_PREFIX = "Bearer "; 

		public static final String BODY = "body";

		public static final String MONGO_ID = "_id"; 
		
		public static final String FILENAME = "filename";
		
		public static final String VERSION = "version";

		public static final String CONTENT_FILE = "content_file"; 
		
		public static final String CONTENT_SCHEMATRON = "content_schematron"; 
		
		public static final String LAST_UPDATE = "last_update_date"; 
		
		public static final String TEMPLATE_ID_ROOT = "template_id_root"; 
		
		public static final String DELETED = "deleted"; 
		
		public static final String SHA_256 = "SHA-256"; 


		private App() {
			//This method is intentionally left blank.
		}
	}
	
	public static final class Logs {
		 		 
		 public static final String CALLED_API_POST_SCHEMATRON = "Called POST /schematron"; 
		 
		 public static final String CALLED_API_PUT_SCHEMATRON = "Called PUT /schematron"; 
		 
		 public static final String CALLED_API_GET_SCHEMATRON = "Called GET /schematron"; 

		 public static final String CALLED_API_GET_SCHEMATRON_BY_ID = "Called GET /schematron/{id}"; 

		 public static final String CALLED_API_DELETE_SCHEMATRON = "Called DELETE /schematron"; 
		 
		 public static final String CALLED_API_QUERY_ROOT_EXTENSION = "Called GET /schematron by ID Root and Extension"; 

		 public static final String CALLED_API_QUERY_ID = "Called GET /schematron by ID";

		 public static final String ELASTIC_LOGGER_APP_NAME = "application"; 
		 
		 public static final String ELASTIC_LOGGER_OP_NAME = "operation"; 

		 public static final String ELASTIC_LOGGER_OP_TIMESTAMP = "op-log-timestamp"; 

		 public static final String ELASTIC_LOGGER_OP_RESULT = "op-result"; 
		 
		 public static final String ELASTIC_LOGGER_OP_TIMESTAMP_START = "op-timestamp-start"; 
		 
		 public static final String ELASTIC_LOGGER_OP_TIMESTAMP_END = "op-timestamp-end"; 
		 
		 public static final String ELASTIC_LOGGER_OP_ERROR_CODE = "op-error"; 
		 
		 public static final String ELASTIC_LOGGER_OP_ERROR_DESCRIPTION = "op-error-description"; 
		 
		 public static final String ERROR_INSERT_SCHEMATRON = "Error inserting all ety schematron :"; 
		 
		 public static final String ERROR_FIND_SCHEMATRON = "Error find by id ety schematron :"; 
		 
		 public static final String ERROR_UPDATING_SCHEMATRON = "Error while updating Schematron "; 
		 
		 public static final String ERROR_SCHEMATRON_ALREADY_PRESENT = "Error: schematron already present in the database"; 
		 
		 public static final String ERROR_FINDALL_SCHEMATRON = "Error while getting schematrons :"; 
		 
		 public static final String ERROR_RETRIEVING_SCHEMATRON = "Error while retrieving schematron"; 
		 
		 public static final String ERROR_DELETE_SCHEMATRON = "Error while deleting ety schematron :"; 
		 
		 public static final String ERROR_RETRIEVING_HOST_INFO = "Error while retrieving host informations"; 
		 
		 public static final String ERROR_RETRIEVING_INSERTIONS = "Error retrieving insertions"; 
		 
		 public static final String ERROR_RETRIEVING_MODIFICATIONS = "Error retrieving modifications"; 

		 public static final String ERROR_RETRIEVING_DELETIONS = "Error retrieving deletions"; 
		 
		 public static final String ERROR_UNABLE_FIND_INSERTIONS = "Unable to retrieve change-set insertions"; 
		 
		 public static final String ERROR_UNABLE_FIND_MODIFICATIONS = "Unable to retrieve change-set modifications"; 
		 
		 public static final String ERROR_UNABLE_FIND_DELETIONS = "Unable to retrieve change-set deletions"; 

		 public static final String ERROR_UNABLE_RETRIEVE_AVAILABLE_DICTIONARY = "Unable to retrieve every available dictionary"; 
		 
		 public static final String ERROR_EXECUTE_EXIST_VERSION_QUERY = "Error while execute exists by version query "; 
		 
		 public static final String ERROR_INSERTING_ETYS = "Error inserting all etys "; 

		 public static final String ERROR_COMPUTING_SHA = "Errore in fase di calcolo SHA-256"; 
		 
		 public static final String ERROR_JSON_HANDLING = "Errore gestione json :"; 
		 
		 public static final String ERROR_EMPTY_DOCUMENT = "Error: inserted document is empty"; 
		 
		 public static final String ERROR_DOCUMENT_NOT_FOUND = "Error: document not found in collection"; 
		 
		 public static final String ERROR_UNABLE_RETRIEVE_EXTENSIONS = "Unable to retrieve every available extension with their documents"; 

		 public static final String ERROR_REQUESTED_DOCUMENT_DOES_NOT_EXIST = "The requested document does not exists"; 

		 public static final String ERROR_INVALID_OBJECT_ID = "The following string is not a valid object id: "; 


			
		 private Logs() {
				//This method is intentionally left blank. 
			}

	}
  
	/**
	 *	Constants.
	 */
	private Constants() {

	}

}
