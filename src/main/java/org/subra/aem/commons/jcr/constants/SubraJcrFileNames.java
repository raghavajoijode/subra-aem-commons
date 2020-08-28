package org.subra.aem.commons.jcr.constants;

public enum SubraJcrFileNames {

	DEFAULT_TEXT_FILE("file.txt"), CONFIG_NODE("config");

	private String value;

	private SubraJcrFileNames(final String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

}
