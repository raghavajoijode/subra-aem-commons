package org.subra.aem.commons.helpers;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.subra.aem.commons.constants.SubraHttpType;

public final class RequestParser {

	private RequestParser() {
		throw new IllegalStateException(this.getClass().getSimpleName());
	}

	public static Optional<String> getSelector(SlingHttpServletRequest request, Pattern pattern) {
		return getSelectors(request).stream().filter(s -> pattern.matcher(s).matches()).findFirst();
	}

	public static Optional<String> getFirstSelector(SlingHttpServletRequest request) {
		return getSelectors(request).stream().findFirst();
	}

	public static List<String> getSelectors(SlingHttpServletRequest request) {
		return Arrays.asList(request.getRequestPathInfo().getSelectors());
	}

	public static String getParameter(final SlingHttpServletRequest request, final String param) {
		return StringUtils.trimToNull(request.getParameter(param));
	}

	public static String getBody(final SlingHttpServletRequest request) throws IOException {
		return IOUtils.toString(request.getInputStream(), SubraHttpType.CHARSET_UTF_8.value());
	}

	public static <T> T getBody(final SlingHttpServletRequest request, final Class<T> clazz) throws IOException {
		return SubraCommonHelper
				.convertToClass(IOUtils.toString(request.getInputStream(), SubraHttpType.CHARSET_UTF_8.value()), clazz);
	}

}
