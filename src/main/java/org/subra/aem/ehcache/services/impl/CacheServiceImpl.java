package org.subra.aem.ehcache.services.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.subra.aem.commons.constants.SubraUserMapperService;
import org.subra.aem.ehcache.helpers.CacheHelper;
import org.subra.aem.ehcache.helpers.impl.CacheHelperImpl;
import org.subra.aem.ehcache.services.CacheService;

import com.day.cq.commons.jcr.JcrConstants;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;

@Component(service = CacheService.class, immediate = true)
@ServiceDescription("Subra - CacheService Service")
@Designate(ocd = CacheServiceImpl.Config.class)
public class CacheServiceImpl implements CacheService {

	public static final String CLASS_COUNTRY_FORM = "%s:%s";
	public static final String DEFAULT_CACHE_CONFIG = "/etc/subra/core/cache/ehcache.xml";

	private static final Logger LOGGER = LoggerFactory.getLogger(CacheServiceImpl.class);
	private static final Map<String, Object> AUTH_USER_INFO;

	static {
		AUTH_USER_INFO = new HashMap<>();
		AUTH_USER_INFO.put(ResourceResolverFactory.SUBSERVICE, SubraUserMapperService.ADMIN_SERVICE.value());
	}

	@Reference
	private ResourceResolverFactory resolverFactory;

	private Map<String, CacheHelper<?>> caches = new ConcurrentHashMap<>();
	protected CacheManager cacheManager;

	public CacheServiceImpl() throws IOException {
		super();
	}

	@Activate
	protected void activate(final Config config) {
		String configPath = config.cacheConfig();
		LOGGER.info("Activating using jcr config location {}", configPath);
		try (InputStream inputStream = buildConfigStream(configPath)) {
			cacheManager = CacheManager.create(inputStream);
		} catch (CacheException | IOException e) {
			LOGGER.error("Exception trying to build cacheManager from config", e);
			cacheManager = new CacheManager();
		}
	}

	@Modified
	protected void modified(final Config config) {
		deactivate();
		activate(config);
	}

	@Deactivate
	protected void deactivate() {
		if (cacheManager != null) {
			cacheManager.shutdown();
			cacheManager = null;
		}
	}

	@Override
	public List<Ehcache> getEhcacheInstances() {
		String[] cacheNames = cacheManager.getCacheNames();
		if (cacheNames == null) {
			return Collections.emptyList();
		}

		List<Ehcache> ehcacheList = new ArrayList<>();
		for (String cacheName : cacheNames) {
			Ehcache c = cacheManager.getEhcache(cacheName);
			if (c != null) {
				ehcacheList.add(c);
			}
		}

		return ehcacheList;
	}

	@Override
	public <V> CacheHelper<V> getInstanceCache(final String className, final String countryCode) {
		return getInstanceCache(name(className, countryCode));
	}

	@SuppressWarnings("unchecked")
	private <V> CacheHelper<V> getInstanceCache(final String name) {
		return (CacheHelper<V>) caches.computeIfAbsent(name, k -> new CacheHelperImpl<>(cacheManager, name));
	}

	public void clearByClassName(final String className) {
		Iterator<String> keyIt = caches.keySet().iterator();
		while (keyIt.hasNext()) {
			String cacheName = keyIt.next();
			if (StringUtils.startsWith(cacheName, className)) {
				caches.get(cacheName).clear();
			}
		}
	}

	protected Session getSession() throws LoginException {
		ResourceResolver resolver = resolverFactory.getServiceResourceResolver(AUTH_USER_INFO);
		return resolver.adaptTo(Session.class);
	}

	protected InputStream buildConfigStream(final String path) {
		try {
			Node configNode = getSession().getNode(path).getNode(JcrConstants.JCR_CONTENT);
			Property configData = configNode.getProperty(JcrConstants.JCR_DATA);
			LOGGER.debug("Activating EHCache with config: {}", configData.getString());
			return configData.getBinary().getStream();
		} catch (RepositoryException | LoginException e) {
			LOGGER.error("Exception while trying to read config node", e);
			return null;
		}
	}

	private String name(final String className, final String countryCode) {
		return String.format(CLASS_COUNTRY_FORM, className, countryCode);
	}

	@ObjectClassDefinition(name = "Subra EHCache Service", description = "Service for ehcache configuration")
	public @interface Config {
		@AttributeDefinition(name = "cache.config", description = "SubraCache ehcache.xml path")
		String cacheConfig() default DEFAULT_CACHE_CONFIG;
	}
}
