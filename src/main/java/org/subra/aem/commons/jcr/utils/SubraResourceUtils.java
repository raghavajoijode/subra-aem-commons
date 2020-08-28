package org.subra.aem.commons.jcr.utils;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.subra.aem.commons.jcr.constants.SubraJcrProperties.PN_CREATED_BY;
import static org.subra.aem.commons.jcr.constants.SubraJcrProperties.PN_CREATED_ON;
import static org.subra.aem.commons.jcr.constants.SubraJcrProperties.PN_DESCRIPTION;
import static org.subra.aem.commons.jcr.constants.SubraJcrProperties.PN_HIDDEN;
import static org.subra.aem.commons.jcr.constants.SubraJcrProperties.PN_LOCKED;
import static org.subra.aem.commons.jcr.constants.SubraJcrProperties.PN_TITLE;
import static org.subra.aem.commons.jcr.constants.SubraJcrProperties.PN_TO_DELETE_ON;
import static org.subra.aem.commons.jcr.constants.SubraJcrProperties.PN_UPDATED_BY;
import static org.subra.aem.commons.jcr.constants.SubraJcrProperties.PN_UPDATED_ON;
import static org.subra.aem.commons.jcr.constants.SubraJcrProperties.PN_WF_APPROVED;
import static org.subra.aem.commons.jcr.constants.SubraJcrProperties.PN_WF_PENDING;
import static org.subra.aem.commons.jcr.constants.SubraJcrProperties.PN_WF_REJECTED;
import static org.subra.aem.commons.jcr.constants.SubraJcrProperties.PN_WF_STATUS;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.adapter.Adaptable;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.models.factory.ModelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.subra.aem.commons.constants.SubraHttpType;
import org.subra.aem.commons.constants.SubraUserMapperService;
import org.subra.aem.commons.helpers.SubraCommonHelper;
import org.subra.aem.commons.jcr.SubraResource;
import org.subra.aem.commons.jcr.constants.SubraJcrFileNames;
import org.subra.aem.commons.jcr.constants.SubraJcrPrimaryType;
import org.subra.aem.commons.jcr.constants.SubraJcrProperties;
import org.subra.aem.commons.utils.SubraDateTimeUtil;
import org.subra.aem.commons.utils.SubraStringUtils;

import com.adobe.cq.export.json.SlingModelFilter;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

/**
 * @author Raghava J
 *
 */
public class SubraResourceUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(SubraResourceUtils.class);

	private static final String PN_POLICY_STYLE_DEFAULT_CLASSES = "cq:styleDefaultClasses";
	private static final String PN_POLICY_STYLE_CLASSES = "cq:styleClasses";
	private static final String PN_POLICY_STYLE_ID = "cq:styleId";
	private static final Map<String, Object> AUTH_INFO;
	static {
		AUTH_INFO = Collections.singletonMap(ResourceResolverFactory.SUBSERVICE,
				SubraUserMapperService.ADMIN_SERVICE.value());
	}

	private SubraResourceUtils() {
		throw new IllegalStateException(this.getClass().getSimpleName());
	}

	public static final Map<String, Object> getAuthInfo(SubraUserMapperService mapper) {
		return Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, mapper.value());
	}

	/**
	 * @param parentResource Parent resource whose children needs to be adopted
	 * @param clazz          Class to which children to be adopted
	 * @return List of Model Objects
	 */
	public static <T> List<T> adoptChildNodestoModel(final Resource parentResource, final Class<T> clazz) {
		final List<T> children = new ArrayList<>();
		if (null != parentResource) {
			final Iterable<Resource> childResources = parentResource.getChildren();
			for (Resource childResource : childResources) {
				children.add(childResource.adaptTo(clazz));
			}
		}
		return children;
	}

	public static InputStream getFileAsIS(final Resource resource) throws RepositoryException {
		final Node node = adoptToOrThrow(resource, Node.class);
		return node.getProperty(JcrConstants.JCR_CONTENT + SubraStringUtils.SLASH + JcrConstants.JCR_DATA).getBinary()
				.getStream();
	}

	public static String getFileJCRData(final Resource resource) {

		return Optional.ofNullable(resource).map(r -> {
			if (r.getChild(JcrConstants.JCR_CONTENT) != null) {
				return r;
			}
			return r.getChild(SubraJcrFileNames.DEFAULT_TEXT_FILE.value());
		}).map(file -> {
			try {
				return IOUtils.toString(getFileAsIS(file), SubraHttpType.CHARSET_UTF_8.value());
			} catch (IOException | RepositoryException e) {
				LOGGER.error("Error getting content from template...", e);
			}
			return null;
		}).orElseThrow();
	}

	public static <K extends Adaptable, T> Optional<T> adoptTo(final K adaptableClazz, final Class<T> adoptorClazz) {
		return Optional.of(adaptableClazz).map(r -> r.adaptTo(adoptorClazz));
	}

	public static <K extends Adaptable, T> T adoptToOrThrow(final K adaptableClazz, final Class<T> adoptorClazz) {
		return adoptTo(adaptableClazz, adoptorClazz).orElseThrow();
	}

	public static List<String> getAssociatedStyleSystem(Resource policy, List<String> styleIds) {
		List<String> styleSystemClasses = new LinkedList<>();
		if (policy != null) {
			if (CollectionUtils.isEmpty(styleIds)) {
				Optional.ofNullable(policy.getValueMap())
						.map(vm -> vm.get(PN_POLICY_STYLE_DEFAULT_CLASSES, String.class))
						.ifPresent(styleSystemClasses::add);
			} else {
				checkStyleIdAndSetClass(policy, styleIds, styleSystemClasses);
			}
		}
		return styleSystemClasses;
	}

	private static void checkStyleIdAndSetClass(Resource resource, List<String> styleIds,
			List<String> styleSystemClasses) {
		ValueMap vm = resource.getValueMap();
		if (styleIds.contains(vm.get(PN_POLICY_STYLE_ID, String.class))) {
			styleSystemClasses.add(vm.get(PN_POLICY_STYLE_CLASSES, String.class));
		}
		resource.getChildren().forEach(child -> checkStyleIdAndSetClass(child, styleIds, styleSystemClasses));
	}

	public static <T> Map<String, T> getChildModels(final SlingHttpServletRequest request,
			final SlingModelFilter modelFilter, final ModelFactory modelFactory, final Class<T> modelClass) {
		Map<String, T> itemWrappers = new LinkedHashMap<>();
		modelFilter.filterChildResources(request.getResource().getChildren()).forEach(child -> itemWrappers
				.put(child.getName(), modelFactory.getModelFromWrappedRequest(request, child, modelClass)));
		return itemWrappers;
	}

	/**
	 * @param resource     Parent resource whose children properties needs to be
	 *                     found
	 * @param childResName Name of specific resource
	 * @return Map of properties
	 */
	private static Map<String, Object> getChildenPropsMap(Resource resource, String childResName) {
		ValueMap vm = resource.getValueMap();
		Map<String, Object> result = new HashMap<>();
		if (childResName.equals(resource.getName()) || EMPTY.equals(childResName)) {
			result = vm.keySet().stream().map(vmKey -> vmKey).collect(Collectors.toMap(Function.identity(), vm::get));
		}
		for (Resource child : resource.getChildren()) {
			Map<String, Object> prop = getChildenPropsMap(child, childResName);
			result.putAll(prop);
		}
		return result;
	}

	/**
	 * @param resource Parent resource whose children properties needs to be found
	 * @return Consolidated Value Map
	 */
	public static ValueMap getChildrenValueMap(Resource resource) {
		return new ValueMapDecorator(getChildenPropsMap(resource, EMPTY));
	}

	/**
	 * @param <T>      Return Type Class
	 * @param resource Parent resource whose children properties needs to be found
	 * @param property Name of property
	 * @param type     Return Type Class
	 * @return Property value at last child, if available
	 */
	public static <T> T getChildProperty(Resource resource, String property, Class<T> type) {
		return getChildrenValueMap(resource).get(property, type);
	}

	/**
	 * @param resource     Parent resource whose children properties needs to be
	 *                     found
	 * @param childResName Name of specific resource
	 * @return ValueMap of specific resource
	 */
	public static ValueMap getChildValueMap(Resource resource, String childResName) {
		return new ValueMapDecorator(getChildenPropsMap(resource, childResName));
	}

	/**
	 * @param <T>          Return Type Class
	 * @param resource     Parent resource whose children properties needs to be
	 *                     found
	 * @param childResName Name of specific resource
	 * @param property     Name of property
	 * @param type         Return Type Class
	 * @return Property value at specific resource
	 */
	public static <T> T getChildPropertyAtResource(Resource resource, String childResName, String property,
			Class<T> type) {
		return getChildValueMap(resource, childResName).get(property, type);
	}

	public static ResourceResolver getResourceResolverFromRequest(final SlingHttpServletRequest request) {
		return request.getResourceResolver();
	}

	public static <T> T adaptResolverToClassUsingRequest(final SlingHttpServletRequest request, final Class<T> clazz) {
		return getResourceResolverFromRequest(request).adaptTo(clazz);
	}

	public static ResourceResolver getAdminServiceResourceResolver(
			final ResourceResolverFactory resourceResolverFactory) throws LoginException {
		return resourceResolverFactory.getServiceResourceResolver(AUTH_INFO);
	}

	public static Resource getResource(final SlingHttpServletRequest request, final String path) {
		return getResourceResolverFromRequest(request).getResource(path);
	}

	public static Resource getResource(final ResourceResolver resolver, final String path) {
		final String resourcePath = path.startsWith(SubraStringUtils.SLASH) ? path : SubraStringUtils.SLASH + path;
		return resolver.getResource(resourcePath);
	}

	public static Resource createResource(ResourceResolver resolver, final Resource base, final String title,
			final SubraJcrPrimaryType type) {
		Resource resource = null;
		try {
			Map<String, Object> properties = new HashMap<>();
			properties.put(type.property(), type.value());
			properties.put(SubraJcrProperties.PN_TITLE.property(), title);
			properties.put(SubraJcrProperties.PN_CREATED_ON.property(), SubraDateTimeUtil.nowInCalendar());
			properties.put(SubraJcrProperties.PN_UPDATED_ON.property(), SubraDateTimeUtil.nowInCalendar());

			resource = resolver.create(base, SubraCommonHelper.createNameFromTitle(title), properties);
			resolver.commit();
		} catch (PersistenceException e) {
			LOGGER.error("Error creating root resource..", e);
		}
		return resource;
	}

	public static Resource getOrCreateResource(ResourceResolver resolver, final String path,
			final SubraJcrPrimaryType type) {
		Resource resource = getResource(resolver, path);
		if (resource == null) {
			final String parentPath = StringUtils.substringBeforeLast(path, "/");
			final Resource parent = getOrCreateResource(resolver, parentPath, type);
			final String name = StringUtils.substringAfterLast(path, "/");
			resource = createResource(resolver, parent, SubraCommonHelper.decodeTitleFromName(name), type);
		}
		return resource;
	}

	public static void deleteResource(ResourceResolver resolver, Resource resource) throws PersistenceException {
		resolver.delete(resource);
		resolver.commit();
	}

	public static Resource getChildResource(final Resource base, final String childTitle) {
		return Optional.of(base).map(r -> r.getChild(SubraCommonHelper.createNameFromTitle(childTitle)))
				.orElseGet(() -> null);
	}

	public static Page getPageFromPath(final SlingHttpServletRequest request, final String path) {
		Resource targetResource = getResource(request, path);
		PageManager pageManager = adaptResolverToClassUsingRequest(request, PageManager.class);
		return (pageManager != null && targetResource != null) ? pageManager.getContainingPage(targetResource) : null;
	}

	public static String getPageURL(final SlingHttpServletRequest request, final Page page) {
		final String vanityURL = page.getVanityUrl();
		return StringUtils.isEmpty(vanityURL) ? (request.getContextPath() + page.getPath() + ".html")
				: (request.getContextPath() + vanityURL);
	}

	public static void updateSubraResource(final Resource resource, final SubraResource subraResource) {
		subraResource.setResource(resource);
		subraResource.setName(resource.getName());
		subraResource.setPath(resource.getPath());
		subraResource.setTitle(SubraJcrUtils.getPropertyValue(resource, PN_TITLE, EMPTY));
		subraResource.setDescription(SubraJcrUtils.getPropertyValue(resource, PN_DESCRIPTION, EMPTY));
		subraResource.setLocked(SubraJcrUtils.getPropertyValue(resource, PN_LOCKED, false));
		subraResource.setHidden(SubraJcrUtils.getPropertyValue(resource, PN_HIDDEN, false));
		subraResource.setWfPending(SubraJcrUtils.getPropertyValue(resource, PN_WF_PENDING, EMPTY));
		subraResource.setWfApproved(SubraJcrUtils.getPropertyValue(resource, PN_WF_APPROVED, EMPTY));
		subraResource.setWfRejected(SubraJcrUtils.getPropertyValue(resource, PN_WF_REJECTED, EMPTY));
		subraResource.setWfStatus(SubraJcrUtils.getPropertyValue(resource, PN_WF_STATUS, EMPTY));
		subraResource.setToDeleteOn(SubraJcrUtils.getPropertyValue(resource, PN_TO_DELETE_ON, null));
		subraResource.setCreatedOn(SubraJcrUtils.getPropertyValue(resource, PN_CREATED_ON, null));
		subraResource.setUpdatedby(SubraJcrUtils.getPropertyValue(resource, PN_UPDATED_BY, EMPTY));
		subraResource.setUpdatedOn(SubraJcrUtils.getPropertyValue(resource, PN_UPDATED_ON, null));
		subraResource.setCreatedBy(SubraJcrUtils.getPropertyValue(resource, PN_CREATED_BY, EMPTY));
	}

}
