package org.systemx.happ.utils;

public enum SysProperty {
	JAVA_IO_TEMP("java.io.tmpdir"), USER_HOME("user.home");

	private final String prop;

	private SysProperty(String prop) {
		this.prop = prop;
	}

	public String getPropertyName() {
		return prop;
	}

	public String getPropertyValue() {
		return System.getProperty(getPropertyName());
	}

}
