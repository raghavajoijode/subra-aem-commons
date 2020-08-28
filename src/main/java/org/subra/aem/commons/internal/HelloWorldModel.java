package org.subra.aem.commons.internal;

import static org.apache.sling.api.resource.ResourceResolver.PROPERTY_RESOURCE_TYPE;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.settings.SlingSettingsService;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

@Model(adaptables = Resource.class)
public class HelloWorldModel {

	@ValueMapValue(name = PROPERTY_RESOURCE_TYPE, injectionStrategy = InjectionStrategy.OPTIONAL)
	@Default(values = "No resourceType")
	protected String resourceType;

	@OSGiService
	private SlingSettingsService settings;
	@SlingObject
	private Resource currentResource;
	@SlingObject
	private ResourceResolver resourceResolver;

	private String message;

	@PostConstruct
	protected void init() {
		PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
		Page currentPage = pageManager.getContainingPage(currentResource);

		message = "\tHello World!\n" + "\tThis is instance: " + settings.getSlingId() + "\n" + "\tResource type is: "
				+ resourceType + "\n" + "\tCurrent page is: " + (currentPage != null ? currentPage.getPath() : "")
				+ "\n";
	}

	public String getMessage() {
		return message;
	}

}
