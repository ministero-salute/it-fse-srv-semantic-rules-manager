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
package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.config;

/**
 * 
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
		public static final String BASE = "it.finanze.sanita.fse2.ms.srvsemanticrulesmanager";

		/**
		 * Controller path.
		 */
		public static final String CONTROLLER = "it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.controller";

		/**
		 * Service path.
		 */
		public static final String SERVICE = "it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.service";

		/**
		 * Configuration path.
		 */
		public static final String CONFIG = "it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.config";

		/**
		 * Configuration mongo path.
		 */
		public static final String CONFIG_MONGO = "it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.config.mongo";

		/**
		 * Configuration mongo repository path.
		 */
		public static final String REPOSITORY_MONGO = "it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.repository";	 


		private ComponentScan() {
			//This method is intentionally left blank.
		}

	}

	public static final class Collections {

		/**
		 * The name of the MongoDB Collection 
		 */
		public static final String COLLECTION_SCHEMATRON = "schematron_eds";  

		private Collections() {

		}
	}


	public static final class Profile {

		public static final String TEST = "test"; 

		public static final String TEST_PREFIX = "test_"; 


		/** 
		 * Constructor.
		 */
		private Profile() {
			//This method is intentionally left blank.
		}

	}

	public static final class Logs {

		public static final String ERROR_DELETE_SCHEMATRON = "Error while deleting ety schematron :"; 

		public static final String ERROR_RETRIEVING_HOST_INFO = "Error while retrieving host informations"; 

		public static final String ERROR_RETRIEVING_INSERTIONS = "Error retrieving insertions"; 

		public static final String ERROR_RETRIEVING_MODIFICATIONS = "Error retrieving modifications"; 

		public static final String ERROR_UNABLE_FIND_INSERTIONS = "Unable to retrieve change-set insertions"; 

		public static final String ERROR_UNABLE_FIND_MODIFICATIONS = "Unable to retrieve change-set modifications"; 

		public static final String ERROR_UNABLE_FIND_DELETIONS = "Unable to retrieve change-set deletions";
		public static final String ERR_REP_COUNT_ACTIVE_DOC = "Impossibile conteggiare ogni estensione attiva";

		public static final String ERROR_UNABLE_RETRIEVE_AVAILABLE_DICTIONARY = "Unable to retrieve every available dictionary"; 

		public static final String ERROR_EXECUTE_EXIST_VERSION_QUERY = "Error while execute exists by version query "; 

		public static final String ERROR_COMPUTING_SHA = "Errore in fase di calcolo SHA-256"; 

		public static final String ERROR_JSON_HANDLING = "Errore gestione json :"; 

		public static final String ERROR_EMPTY_DOCUMENT = "Error: inserted document is empty"; 

		public static final String ERROR_DOCUMENT_NOT_FOUND = "Error: document not found in collection"; 

		public static final String ERROR_REQUESTED_DOCUMENT_DOES_NOT_EXIST = "The requested document does not exists"; 

		public static final String ERROR_INVALID_OBJECT_ID = "The following string is not a valid object id: ";

		public static final String ERR_VAL_UNABLE_CONVERT = "Impossibile convertire %s nel tipo %s";

		public static final String ERROR_INSERT_SCHEMATRON = "Error inserting all etys";
		
		public static final String ERROR_RETRIEVING_SCHEMATRON = "Error while retrieving schematron";
		public static final String ERR_VAL_INVALID_SCHEMATRON_IO = "Impossibile elaborare il contenuto dello schematron";

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
