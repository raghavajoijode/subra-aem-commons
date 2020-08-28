package org.subra.aem.commons.exceptions;

import java.net.URI;

import org.apache.commons.lang3.StringUtils;

public class SubraApiException extends RuntimeException {

	private static final long serialVersionUID = 664704840828592278L;
	private final String serviceUrl;
	private final String errorResponse;

	private final SubraApiError subraApiError;

	public SubraApiException(final String message) {
		super(message);
		this.serviceUrl = StringUtils.EMPTY;
		this.errorResponse = StringUtils.EMPTY;
		this.subraApiError = new SubraApiError();
	}

	public SubraApiException(final String url, final String message) {
		super(message);
		this.serviceUrl = url;
		this.errorResponse = message;
		this.subraApiError = new SubraApiError();
	}

	public SubraApiException() {
		super();
		this.serviceUrl = StringUtils.EMPTY;
		this.errorResponse = StringUtils.EMPTY;
		this.subraApiError = new SubraApiError();
	}

	public SubraApiException(final String message, final Throwable cause) {
		super(message, cause);
		this.serviceUrl = StringUtils.EMPTY;
		this.errorResponse = StringUtils.EMPTY;
		this.subraApiError = new SubraApiError();
	}

	public SubraApiException(final Throwable cause) {
		super(cause);
		this.serviceUrl = StringUtils.EMPTY;
		this.errorResponse = StringUtils.EMPTY;
		this.subraApiError = new SubraApiError();
	}

	public SubraApiException(final SubraApiError subraApiError) {
		super(subraApiError != null ? subraApiError.toString() : null);
		this.serviceUrl = StringUtils.EMPTY;
		this.errorResponse = StringUtils.EMPTY;
		this.subraApiError = subraApiError;
	}

	public SubraApiException(final URI uri, final SubraApiError subraApiError) {
		super(String.format("URL: %s%nError: %s", uri, subraApiError));
		this.serviceUrl = StringUtils.EMPTY;
		this.errorResponse = StringUtils.EMPTY;
		this.subraApiError = subraApiError;
	}

	public SubraApiError getSubraApiError() {
		return subraApiError;
	}

	public String getServiceUrl() {
		return serviceUrl;
	}

	public String getErrorResponse() {
		return errorResponse;
	}

}
