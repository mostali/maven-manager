package org.systemx.happ;

import java.io.File;

import org.systemx.happ.utils.SysProperty;

public class HomeApp extends AppPart {

	public final static String CONF = "conf";

	public final static String LOG = "log";

	public HomeApp(String partName) {
		this(SysProperty.USER_HOME.getPropertyValue(), partName);
	}

	public HomeApp(String rootDir, String partName) {
		this(new File(rootDir, partName));
	}

	public HomeApp(File homeDir) {
		super(homeDir);

		super.createAppPart(CONF);
		super.createAppPart(LOG);
	}

	public static void main(String[] args) {

		HomeApp happ = new HomeApp("d:\\", "testApp1.txt");

	}
}
