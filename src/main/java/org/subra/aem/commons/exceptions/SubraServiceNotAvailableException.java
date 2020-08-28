package org.subra.aem.commons.exceptions;

/**
 * Service Not Available Exception 
 * @author raghava
 *
 */
public class SubraServiceNotAvailableException extends SubraApiException {

    private static final long serialVersionUID = -7005308022960499722L;

    public SubraServiceNotAvailableException() {
        super();
    }

    public SubraServiceNotAvailableException(SubraApiError subraApiError) {
        super(subraApiError);
    }

    public SubraServiceNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public SubraServiceNotAvailableException(String message) {
        super(message);
    }

    public SubraServiceNotAvailableException(Throwable cause) {
        super(cause);
    }
    
}
