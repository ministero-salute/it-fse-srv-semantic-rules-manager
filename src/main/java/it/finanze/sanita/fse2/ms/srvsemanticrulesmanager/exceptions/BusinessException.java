package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions;

/**
 * @author AndreaPerquoti
 * 
 *         Generic business exception.
 *
 */
public class BusinessException extends RuntimeException {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 7486707617222118970L;

	/**
	 * Message constructor.
	 * 
	 * @param msg Message to be shown.
	 */
	public BusinessException(final String msg) {
		super(msg);
	}

	/**
	 * Complete constructor.
	 * 
	 * @param msg Message to be shown.
	 * @param e   Exception to be shown.
	 */
	public BusinessException(final String msg, final Exception e) {
		super(msg, e);
	}

	/**
	 * Exception constructor.
	 * 
	 * @param e Exception to be shown.
	 */
	public BusinessException(final Exception e) {
		super(e);
	}

}
