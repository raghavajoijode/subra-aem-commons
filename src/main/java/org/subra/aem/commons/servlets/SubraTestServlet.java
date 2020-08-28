package org.subra.aem.commons.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.subra.aem.commons.utils.SubraDateTimeUtil;
import org.subra.aem.commons.utils.SubraDateTimeUtil.ZoneIdUtil;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=To test SubraJcrConstants",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/subra/dates",
		"sling.auth.requirements=" + "-/bin/subra/dates" })
public class SubraTestServlet extends SlingAllMethodsServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(SubraTestServlet.class);

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		LOGGER.info("In Do Get Request");
		response.setContentType("text/html");
		String s = "<h3>LDT: " + SubraDateTimeUtil.localDateTimeString() + "</h3>";

		s = s + "<h3>LDT getZonedDateTime at India: "
				+ SubraDateTimeUtil.localDateTimeStringAtZone(ZoneIdUtil.IST.zone()) + "</h3>";

		s = s + "<h3>LDT getLocalDateTime at Tokyo: " + SubraDateTimeUtil.localDateTimeAtZone(ZoneIdUtil.JST.zone())
				+ "</h3>";

		s = s + "<h3>LDT getZonedDateTime at Tokyo: " + SubraDateTimeUtil.zonedDateTime(ZoneIdUtil.JST.zone())
				+ "</h3>";

		s = s + "<h3>LDT getLocalDateTime at UTC: " + SubraDateTimeUtil.localDateTimeAtUTC() + "</h3>";

		s = s + "<h3>LDT getZonedDateTime at UTC: " + SubraDateTimeUtil.zonedDateTimeAtUTC() + "</h3>";

		response.getWriter().write(s);
	}

}
