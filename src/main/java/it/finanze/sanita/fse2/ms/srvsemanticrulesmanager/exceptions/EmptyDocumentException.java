/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions;


public class EmptyDocumentException extends Exception {

    /**
     * Serial version uid
	 */
	private static final long serialVersionUID = -1312834002328361384L;

	
    /**
     * Message constructor.
     *
     * @param msg	Message to be shown.
     */
    public EmptyDocumentException(final String msg) {
        super(msg);
    }
    
}
