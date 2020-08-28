package org.subra.aem.commons.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Raghava Joijode
 *
 */
public class SubraStringUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(SubraStringUtils.class);
	public static final String COMMA = ",";
	public static final String HYPHEN = "-";
	public static final String SPACE = " ";
	public static final String SLASH = "/";
	public static final String COLON = ":";
	public static final String AT = "@";

	private SubraStringUtils() {
		throw new IllegalStateException(this.getClass().getSimpleName());
	}

	public static boolean isNoneBlank(String... strings) {
		return StringUtils.isNoneBlank(strings);
	}

	public static boolean isNoneEmpty(String string, String... strings) {
		return StringUtils.isNotEmpty(string) && Arrays.stream(strings).allMatch(StringUtils::isNotEmpty);
	}

	public static boolean isAllBlank(String string, String... strings) {
		return !(StringUtils.isNotBlank(string) || Arrays.stream(strings).anyMatch(StringUtils::isNotBlank));
	}

	public static List<String> getLookUpKeys(final String str) {
		return SubraCollectionUtils.getStreamFromArray(StringUtils.substringsBetween(str, "${", "}"))
				.map(i -> i.contains(":-") ? StringUtils.substringBefore(i, ":-") : i).collect(Collectors.toList());
	}

	public static String encode(final String value, final String charset) throws UnsupportedEncodingException {
		return URLEncoder.encode(value, StringUtils.defaultIfBlank(charset, "UTF-8"));
	}

}