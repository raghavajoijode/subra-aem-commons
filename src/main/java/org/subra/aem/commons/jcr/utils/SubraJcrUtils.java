package org.subra.aem.commons.jcr.utils;

import java.util.Optional;

import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.subra.aem.commons.jcr.constants.SubraJcrProperties;

public class SubraJcrUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(SubraJcrUtils.class);

	private SubraJcrUtils() {
		throw new UnsupportedOperationException();
	}

	public static Object addOrUpdateProperty(Resource resource, SubraJcrProperties property, Object value) {
		try {
			ModifiableValueMap mvp = resource.adaptTo(ModifiableValueMap.class);
			mvp.put(property.property(), value);
			resource.getResourceResolver().commit();
			return value;
		} catch (PersistenceException | NullPointerException e) {
			LOGGER.error("Error creating/updating property", e);
		}
		return null;
	}

	public static Object deleteProperty(Resource resource, SubraJcrProperties property) {
		try {
			ModifiableValueMap mvp = resource.adaptTo(ModifiableValueMap.class);
			Object value = mvp.remove(property.property());
			resource.getResourceResolver().commit();
			return value;
		} catch (PersistenceException | NullPointerException e) {
			LOGGER.error("Error deleting property", e);
		}
		return null;
	}

	public static <T> T getPropertyValue(Resource resource, SubraJcrProperties property, T defaultValue) {
		return Optional.ofNullable(resource).map(Resource::getValueMap)
				.map(vm -> vm.get(property.property(), defaultValue)).orElseGet(() -> defaultValue);
	}

}
