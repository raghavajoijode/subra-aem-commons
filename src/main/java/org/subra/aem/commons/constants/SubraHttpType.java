package org.subra.aem.commons.constants;

/**
 * Convenience enum for defining Http constants
 * @author raghava
 *
 */
public enum SubraHttpType {

    MEDIA_TYPE_JSON("application/json"),
    MEDIA_TYPE_XML("application/xml"),
    MEDIA_TYPE_FORM_URLENCODED("application/x-www-form-urlencoded"),
    MEDIA_TYPE_PNG("image/png"),
    MEDIA_TYPE_JPEG("image/jpeg"),
    MEDIA_TYPE_GIF("image/gif"),
    MEDIA_TYPE_OCTET_STREAM("application/octet-stream"),
    MEDIA_TYPE_TEXT("text/plain"),
    MEDIA_TYPE_HTML("text/html"),
    CHARSET_UTF_8("UTF-8"),
    ACCEPT("Accept"),
    AUTHORIZATION("Authorization"),
    AUTHORIZATION_PREFIX("Bearer "),
    X_ETAG("X-ETag"),
    X_AUTH_TOKEN("X-AUTH-TOKEN"),
    X_MEMBER_NUMBER("X-MEMBER-NUMBER"),
    X_IP_ADDRESS("x-ip-address"),
    X_USER_AGENT_TEXT("x-user-agent-text"),
    X_APPLICATION_CODE("x-application-code"),
    X_APPLICATION_VERSION("x-application-version"),
    X_REFERER_URL_TEXT("x-referer-url-text"),
    X_DOWNTIME_FLAG("x-downtime-flag"),
    X_APPLICATION_CODE_VALUE("SUBRA"),
    X_APPLICATION_VERSION_VALUE("1.0"),
    PAGE_ID("WEB"),
    PLATFORM("WCS"),
    HTTP_PROTOCOL("http://"),
    HTTPS_PROTOCOL("https://"),
    GET("GET"),
    POST("POST");

    private String value;
    
    private SubraHttpType(final String value) {
        this.value = value;
	}

	public String value() {
		return this.value;
	}
}