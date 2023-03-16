/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.utility;

public class RouteUtility {
	
    /**
     * Private constructor to disallow to access from other classes
     */
    private RouteUtility() {}

	public static final String API_VERSION = "v1";
	public static final String API_CHANGESET = "changeset";
	public static final String API_SCHEMATRON = "schematron";
	public static final String API_QP_LAST_UPDATE = "lastUpdate";
	public static final String API_ID = "id";
	public static final String API_STATUS = "status";
	public static final String API_SCHEMATRON_MAPPER = "/" + API_VERSION + "/" + API_SCHEMATRON;
	
	public static final String API_CHANGESET_STATUS = "/" + API_VERSION + "/" + API_CHANGESET + "/" + API_SCHEMATRON + "/" + API_STATUS ;
    public static final String API_QP_INCLUDE_DELETED = "includeDeleted";
    public static final String API_QP_INCLUDE_BINARY = "includeBinary";
    public static final String API_PATH_ID_VAR = "id";
    public static final String API_PATH_TEMPLATEIDROOT_VAR = "templateIdRoot";
    public static final String API_PATH_VERSION_VAR = "version";
    public static final String API_PATH_SYSTEM_VAR = "system";
    public static final String API_PATH_FILE_VAR = "file";
    public static final String API_PATH_TEMPLATEIDROOT = "/{" + API_PATH_TEMPLATEIDROOT_VAR + "}";
    public static final String API_ID_EXTS = "/{" + API_PATH_ID_VAR + "}";
    public static final String API_GET_ONE_BY_ID = API_ID + API_ID_EXTS;
    public static final String API_DELETE_BY_TEMPLATEIDROOT = API_PATH_TEMPLATEIDROOT;
    public static final String API_GET_BY_TEMPLATEIDROOT = API_PATH_TEMPLATEIDROOT;
    public static final String API_SCHEMATRONS_TAG = "Schematron Controller";
}
