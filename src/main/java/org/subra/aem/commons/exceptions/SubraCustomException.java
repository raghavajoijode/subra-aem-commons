package org.subra.aem.commons.exceptions;

public class SubraCustomException extends Exception {

	private static final long serialVersionUID = 664704840828592278L;

	public SubraCustomException() {
		super();
	}

	public SubraCustomException(final String message) {
		super(message);
	}

	public SubraCustomException(final Throwable cause) {
		super(cause);
	}

	public SubraCustomException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
