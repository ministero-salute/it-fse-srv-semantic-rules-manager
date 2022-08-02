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

