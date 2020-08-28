package org.subra.aem.solr.services.impl;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.subra.aem.solr.services.SolrServerConfiguration;
import org.subra.aem.solr.services.impl.SolrServerConfigurationImpl.Config;

@Component(service = SolrServerConfiguration.class, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = Config.class)
public class SolrServerConfigurationImpl implements SolrServerConfiguration {

	private String solrProtocol;

	private String solrServerName;

	private String solrServerPort;

	private String solrCoreName;

	private String contentPagePath;

	@Activate
	public void activate(Config config) {
		this.solrProtocol = config.protocolValue();
		this.solrServerName = config.serverName();
		this.solrServerPort = config.serverPort();
		this.solrCoreName = config.serverCollection();
		this.contentPagePath = config.serverPath();
	}

	public String getSolrProtocol() {
		return this.solrProtocol;
	}

	public String getSolrServerName() {
		return this.solrServerName;
	}

	public String getSolrServerPort() {
		return this.solrServerPort;
	}

	public String getSolrCoreName() {
		return this.solrCoreName;
	}

	public String getContentPagePath() {
		return this.contentPagePath;
	}

	@ObjectClassDefinition(name = "AEM Solr Search - Solr Configuration Service", description = "Service Configuration")
	public @interface Config {

		@AttributeDefinition(name = "Protocol", defaultValue = "http", description = "Configuration value")
		String protocolValue();

		@AttributeDefinition(name = "Solr Server Name", defaultValue = "localhost", description = "Server name or IP address")
		String serverName();

		@AttributeDefinition(name = "Solr Server Port", defaultValue = "8983", description = "Server port")
		String serverPort();

		@AttributeDefinition(name = "Solr Core Name", defaultValue = "collection", description = "Core name in solr server")
		String serverCollection();

		@AttributeDefinition(name = "Content page path", defaultValue = "/content/we-retail", description = "Content page path from where solr has to index the pages")
		String serverPath();

	}
}