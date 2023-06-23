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
package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.enums;

public enum OperationLogEnum implements ILogEnum {

	POST_SCHEMATRON("POST-SCHEMATRON", "Aggiunta Schematron"), 
	PUT_SCHEMATRON("PUT-SCHEMATRON", "Aggiornamento Schematron"), 
	DELETE_SCHEMATRON("DELETE-SCHEMATRON", "Cancellazione Schematron"), 
	QUERY_SCHEMATRON("QUERY-SCHEMATRON", "Query Schematron"), 
	QUERY_SCHEMATRON_BY_ID("QUERY-SCHEMATRON-BY-ID", "Query Schematron by ID");

	
	private String code;
	
	public String getCode() {
		return code;
	}

	private String description;

	private OperationLogEnum(String inCode, String inDescription) {
		code = inCode;
		description = inDescription;
	}

	public String getDescription() {
		return description;
	}

}

