package org.subra.aem.ehcache.services;

import java.util.List;

import org.subra.aem.ehcache.helpers.CacheHelper;

import net.sf.ehcache.Ehcache;

public interface CacheService {

	<V> CacheHelper<V> getInstanceCache(String className, String countryCode);

	List<Ehcache> getEhcacheInstances();

	/**
	 * Clear all caches that start with the given className
	 * 
	 * @param className
	 */
	void clearByClassName(String className);

}
