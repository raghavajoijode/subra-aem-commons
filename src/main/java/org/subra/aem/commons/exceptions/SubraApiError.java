package org.subra.aem.commons.exceptions;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubraApiError implements Serializable {

    private static final long serialVersionUID = -9145395656420154303L;

    private int httpStatusCode;
    private String httpReason;

    @JsonProperty("code")
    private String errorCode;

    @JsonProperty("message")
    private String errorMessage;

    public int getHttpStatusCode() {
        return httpStatusCode;
    }
    public void setHttpStatusCode(final int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }
    public String getHttpReason() {
        return httpReason;
    }
    public void setHttpReason(final String httpReason) {
        this.httpReason = httpReason;
    }
    public String getErrorCode() {
        return errorCode;
    }
    public void setErrorCode(final String errorCode) {
        this.errorCode = errorCode;
    }
    public String getErrorMessage() {
        return errorMessage;
    }
    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return new StringBuilder()
            .append("HttpStatus: [").append(httpStatusCode)
            .append("] :: httpReason [").append(httpReason)
            .append("] :: Api error code: [").append(errorCode)
            .append("] :: Api error message: [").append(errorMessage).append("]").toString();

    }
}
