/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions;

public class SchematronValidatorException extends Exception {

    /**
     * Message constructor.
     *
     * @param msg	Message to be shown.
     */
    public SchematronValidatorException(final String msg) {
        super(msg);
    }

    /**
     * Message constructor.
     *
     * @param msg	Message to be shown.
     */
    public SchematronValidatorException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
