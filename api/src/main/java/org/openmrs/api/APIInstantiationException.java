package org.openmrs.api;

/**
 * Thrown when unable to instantiate a Collection or Map return type of a service
 * layer method.
 */
public class APIInstantiationException extends APIException {
	
	public static final long serialVersionUID = 1L;
	
	/**
	 * Default empty constructor. It is more common to use the
	 * {@link #APIInstantiationException(String)} constructor to provide some context to the user
	 * as to where/why the instantiation has failed
	 */
	public APIInstantiationException() {
	}
	
	/**
	 * Common constructor taking in a message to give the user some context as to where/why the
	 * instantiation failed.
	 * 
	 * @param message String describing where/why the instantiation failed
	 */
	public APIInstantiationException(String message) {
		super(message);
	}
	
	/**
	 * Common constructor taking in a message to give the user some context as to where/why the
	 * instantiation failed.
	 * 
	 * @param message String describing where/why the instantiation failed
	 * @param cause error further up the stream that caused this failure
	 */
	public APIInstantiationException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Constructor giving the user a further cause exception reason that caused this instantiation
	 * failure
	 * 
	 * @param cause error further up the stream that caused this failure
	 */
	public APIInstantiationException(Throwable cause) {
		super(cause);
	}
	
}
