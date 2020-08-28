package org.subra.aem.commons.constants;

public enum Defaults {

	UTF_CHARSET("UTF-8"),

	DEFAULT_PAGE_DESC("Default page description goes here"),

	DEFAULT_SITE_NAME("XYZ Foods, Inc."),

	DEFAULT_IMAGE("/content/dam/xyz-foods/defaults/image.jpg"),

	EXTERNALIZER_SCHEMA_HTTPS("https"), EXTERNALIZER_SCHEMA_HTTP("http"),

	EXTERNALIZER_DOMAIN_PUBLISH("publish"), EXTERNALIZER_DOMAIN_AUTHOR("author"),

	TWITTER_HANDLE("@Raghava_joijode");

	private String value;
	
	private Defaults(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

}
