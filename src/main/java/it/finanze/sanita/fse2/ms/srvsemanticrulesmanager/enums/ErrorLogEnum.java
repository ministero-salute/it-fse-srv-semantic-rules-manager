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

public enum ErrorLogEnum implements ILogEnum {

	KO_SCHEMATRON_CREATE("KO-SCHEMATRON-CREATE", "Error while creating Schematron"),
	KO_SCHEMATRON_UPDATE("KO-SCHEMATRON-UPDATE", "Error while updating Schematron"),
	KO_SCHEMATRON_QUERY("KO-SCHEMATRON-QUERY", "Error while querying Schematron"),
	KO_SCHEMATRON_DELETE("KO-SCHEMATRON-CDELETE", "Error while deleting Schematron"),
	KO_SCHEMATRON_GET("KO-SCHEMATRON-GET", "Error while getting Schematron");

	private String code;
	
	public String getCode() {
		return code;
	}

	private String description;

	private ErrorLogEnum(String inCode, String inDescription) {
		code = inCode;
		description = inDescription;
	}

	public String getDescription() {
		return description;
	}

}

