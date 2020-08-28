package org.subra.aem.commons.exceptions;

/**
 * Exception for configuration issues. 
 * @author raghava
 *
 */
public class SubraConfigurationException extends RuntimeException {

    private static final long serialVersionUID = -6398099054005140577L;

    public SubraConfigurationException() {
        super();
    }

    public SubraConfigurationException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public SubraConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SubraConfigurationException(String message) {
        super(message);
    }

    public SubraConfigurationException(Throwable cause) {
        super(cause);
    }
    
}
