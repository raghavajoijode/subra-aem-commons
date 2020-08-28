package org.subra.aem.ehcache.helpers;

import java.util.Collection;

/**
 * A SubraCache managed by the cache manager.
 */
public interface CacheHelper<V> {

	/**
	 * SubraCache an object
	 * 
	 * @param key      The key with which to find the object.
	 * @param payload  The object to cache.
	 * @param duration The time to cache the object (seconds).
	 */
	V put(String key, V payload);

	/**
	 * Test for a non expired entry in the cache.
	 * 
	 * @param key The cache key.
	 * @return true if the key maps to a non-expired cache entry, false if not.
	 */
	boolean containsKey(String key);

	/**
	 * Get the non expired entry, or null if not there (or expired)
	 * 
	 * @param key The cache key.
	 * @return The payload, or null if the payload is null, the key is not found, or
	 *         the entry has expired (Note: use containsKey() to remove this
	 *         ambiguity).
	 */
	V get(String key);

	/**
	 * Clear all entries.
	 */
	void clear();

	/**
	 * Remove this entry from the cache.
	 * 
	 * @param key The cache key.
	 */
	boolean remove(String key);

	/**
	 * Remove the key and any child keys from the cache, this is an expensive
	 * operation.
	 * 
	 * @param key
	 */
	void removeChildren(String key);

	/**
	 * @return
	 */
	Collection<V> values();

	/**
	 * @return
	 */
	Collection<String> keys();

}