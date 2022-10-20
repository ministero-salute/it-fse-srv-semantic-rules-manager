/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
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

