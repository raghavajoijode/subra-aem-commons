package org.subra.aem.commons.exceptions;

public class SubraRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 664704840828592278L;

	public SubraRuntimeException() {
		super();
	}

	public SubraRuntimeException(final String message) {
		super(message);
	}

	public SubraRuntimeException(final Throwable cause) {
		super(cause);
	}

	public SubraRuntimeException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
