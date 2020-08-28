package org.subra.aem.commons.jcr.utils;

import java.util.Locale;

import org.apache.sling.i18n.ResourceBundleProvider;
import org.osgi.service.cm.ConfigurationException;

import com.day.cq.i18n.I18n;

public final class SubraI18NUtil {

	private static I18n i18n;

	private SubraI18NUtil() {
		throw new IllegalStateException(this.getClass().getSimpleName());
	}

	public static void configure(ResourceBundleProvider resourceBundleProvider) throws ConfigurationException {
		if (resourceBundleProvider == null) {
			throw new ConfigurationException(null, "resourceBundleProvider is null");
		}
		i18n = new I18n(resourceBundleProvider.getResourceBundle(Locale.ENGLISH));
	}

	public static String getI18nValue(final String text) {
		return i18n.get(text);
	}

	public static I18n getI18n() {
		return i18n;
	}

}