/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions;

public class DocumentAlreadyPresentException extends Exception {

    /**
	 * Serial version UID 
	 */
	private static final long serialVersionUID = 7925016596669482549L;

	
	/**
     * Message constructor.
     *
     * @param msg	Message to be shown.
     */
    public DocumentAlreadyPresentException(final String msg) {
        super(msg);
    }
}
