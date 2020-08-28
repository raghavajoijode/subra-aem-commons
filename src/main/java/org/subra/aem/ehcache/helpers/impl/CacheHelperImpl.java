package org.subra.aem.ehcache.helpers.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.subra.aem.commons.exceptions.SubraRuntimeException;
import org.subra.aem.ehcache.helpers.CacheHelper;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 *
 */
public class CacheHelperImpl<V> implements CacheHelper<V> {

	private static final Logger LOGGER = LoggerFactory.getLogger(CacheHelperImpl.class);
	private String cacheName;
	private Cache cache;
	private long miss;
	private long hits;
	private long gets;
	private CacheManager cacheManager;

	/**
	 * @param manager
	 * @param name
	 */
	public CacheHelperImpl(final CacheManager manager, final String name) {
		if (name == null) {
			cacheName = "default";
		} else {
			cacheName = name;
		}
		this.cacheManager = manager;
		synchronized (cacheManager) {
			cache = cacheManager.getCache(cacheName);
			if (cache == null) {
				cacheManager.addCache(cacheName);
				cache = cacheManager.getCache(cacheName);
				if (cache == null) {
					throw new SubraRuntimeException("Failed to create SubraCache with name " + cacheName);
				}
			}
		}
	}

	/**
	 * {@inherit-doc}
	 * 
	 * @see org.apache.sling.commons.SubraCache.api.CacheHelper#clear()
	 */
	public void clear() {
		cache.removeAll();
	}

	/**
	 * {@inherit-doc}
	 * 
	 * @see org.apache.sling.commons.SubraCache.api.CacheHelper#containsKey(java.lang.String)
	 */
	public boolean containsKey(final String key) {
		return cache.isKeyInCache(key);
	}

	/**
	 * {@inherit-doc}
	 * 
	 * @see org.apache.sling.commons.SubraCache.api.CacheHelper#get(java.lang.String)
	 */
	public V get(final String key) {
		final Element e = cache.get(key);
		if (e == null) {
			return stats(null);
		}
		return stats(e.getObjectValue());
	}

	/**
	 * Records stats
	 * 
	 * @param objectValue
	 * @return the Object Value
	 */
	@SuppressWarnings("unchecked")
	private V stats(final Object objectValue) {
		if (objectValue == null) {
			miss++;
		} else {
			hits++;
		}
		gets++;
		if (gets % 1000 == 0) {
			final long hp = (100 * hits) / gets;
			final long mp = (100 * miss) / gets;
			LOGGER.info("{} SubraCache Stats hits {} ({}%), misses {} ({}%), calls {}", cacheName, hits, hp, miss, mp,
					gets);
		}
		return (V) objectValue;
	}

	/**
	 * {@inherit-doc}
	 * 
	 * @see org.apache.sling.commons.SubraCache.api.CacheHelper#put(java.lang.String,
	 *      java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public V put(final String key, final V payload) {
		V previous = null;
		if (cache.isKeyInCache(key)) {
			final Element e = cache.get(key);
			if (e != null) {
				previous = (V) e.getObjectValue();
			}
		}
		cache.put(new Element(key, payload));
		return previous;
	}

	/**
	 * {@inherit-doc}
	 * 
	 * @see org.apache.sling.commons.SubraCache.api.CacheHelper#remove(java.lang.String)
	 */
	public boolean remove(final String key) {
		return cache.remove(key);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.apache.sling.commons.SubraCache.api.CacheHelper#removeChildren(java.lang.String)
	 */
	public void removeChildren(String key) {
		cache.remove(key);
		if (!key.endsWith("/")) {
			key = key + "/";
		}
		final List<?> keys = cache.getKeys();
		for (final Object k : keys) {
			if (((String) k).startsWith(key)) {
				cache.remove(k);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.apache.sling.commons.SubraCache.api.CacheHelper#list()
	 */
	@SuppressWarnings("unchecked")
	public Collection<V> values() {
		final List<String> keys = cache.getKeys();
		final List<V> values = new ArrayList<>();
		for (final String k : keys) {
			final Element e = cache.get(k);
			if (e != null) {
				values.add((V) e.getObjectValue());
			}
		}
		return values;
	}

	@SuppressWarnings("unchecked")
	public Collection<String> keys() {
		return cache.getKeys();
	}

}