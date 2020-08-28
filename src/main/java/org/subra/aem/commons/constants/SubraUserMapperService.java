package org.subra.aem.commons.constants;

public enum SubraUserMapperService {
	
	EMAIL_SERVICE("subra-email-service"),
	ADMIN_SERVICE("subra-admin-service");
	
	private String value;
	
	private SubraUserMapperService(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

}
