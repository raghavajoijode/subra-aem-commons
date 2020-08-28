package org.subra.aem.commons.helpers;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.function.Supplier;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.subra.aem.commons.exceptions.SubraCustomException;
import org.subra.aem.commons.exceptions.SubraRuntimeException;
import org.subra.aem.commons.utils.SubraStringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class SubraCommonHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(SubraCommonHelper.class);
	private static final String EXCEPTION = "exception";
	private static final String EXCEPTION_SUFFIX = SubraStringUtils.HYPHEN + EXCEPTION;
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	private SubraCommonHelper() {
		throw new UnsupportedOperationException();
	}

	public static ObjectMapper getObjectMapper() {
		OBJECT_MAPPER.registerModule(new JavaTimeModule());
		OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		OBJECT_MAPPER.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
		return OBJECT_MAPPER;
	}

	public static <T> T convertToClass(Object objValue, Class<T> clazz) throws IOException {
		T classValue = null;
		try {
			classValue = getObjectMapper()
					.readValue((objValue instanceof String) ? (String) objValue : writeValueAsString(objValue), clazz);
		} catch (IllegalArgumentException ie) {
			LOGGER.error("Error occured converting obj to class", ie);
		}
		return classValue;
	}

	public static String writeValueAsString(Object objValue) throws JsonProcessingException {
		return SubraCommonHelper.getObjectMapper().writeValueAsString(objValue);
	}

	public static <T> T readValueFromResource(final String resourceFileName, final Class<T> clazz) throws IOException {
		return getObjectMapper().readValue(SubraCommonHelper.class.getResourceAsStream(
				resourceFileName.startsWith("/") ? resourceFileName : String.format("/%s", resourceFileName.trim())),
				clazz);
	}

	public static <T> T getCacheData(SlingHttpServletRequest request, String cacheKey, Supplier<T> supplier) {
		return getOptionalCachedData(request, cacheKey, supplier).orElse(null);
	}

	public static <T> T getCachedDataOrThrow(final HttpServletRequest request, final String cacheKey,
			final Supplier<T> supplier) throws SubraCustomException {
		return getOptionalCacheDataOrThrow(request, cacheKey, supplier).orElse(null);
	}

	public static <T> Optional<T> getOptionalCachedData(final HttpServletRequest request, final String cacheKey,
			final Supplier<T> supplier) {
		try {
			return getOptionalCacheDataOrThrow(request, cacheKey, supplier);
		} catch (final Exception e) {
			throw new SubraRuntimeException("Caught unexpected exception", e);
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> Optional<T> getOptionalCacheDataOrThrow(final HttpServletRequest request, final String cacheKey,
			final Supplier<T> supplier) throws SubraCustomException {
		final Optional<T> cachedData = (Optional<T>) request.getAttribute(cacheKey);
		if (cachedData.isPresent())
			return cachedData;

		final SubraCustomException exception = (SubraCustomException) request.getAttribute(cacheKey + EXCEPTION_SUFFIX);
		if (exception != null)
			throw exception;

		try {
			final Optional<T> data = Optional.ofNullable(supplier.get());
			request.setAttribute(cacheKey, data);
			return data;
		} catch (Exception e) {
			request.setAttribute(cacheKey + EXCEPTION_SUFFIX, new SubraCustomException(e));
			throw new SubraCustomException(e);
		}
	}

	public static void clearLogFiles(final String slingHomePath) {
		final String logsFolderPath = slingHomePath.concat("/logs");
		try {
			FileUtils.cleanDirectory(new File(logsFolderPath));
		} catch (final IOException e) {
			throw new SubraRuntimeException("Caught Expected exception : ", e);
		}
	}

	/*
	 * Creates new instance of a given class
	 * 
	 * throws SubraRuntimeException if unable to create class instance.
	 */
	public static <T> T createClassInstance(final Class<T> clazz) throws SubraCustomException {
		try {
			return clazz.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new SubraCustomException(e);
		}
	}

	/*
	 * @param title - String which needs to be converted to name
	 * 
	 * @return name in standard form from title
	 */
	public static String createNameFromTitle(final String title) {
		return StringUtils.replace(StringUtils.lowerCase(title), SubraStringUtils.SPACE, SubraStringUtils.HYPHEN);
	}

	/*
	 * @param name - String which needs to be converted to title
	 * 
	 * @return title in standard form from name
	 */
	public static String decodeTitleFromName(final String name) {
		return StringUtils.replace(StringUtils.capitalize(name), SubraStringUtils.HYPHEN, SubraStringUtils.SPACE);
	}

}