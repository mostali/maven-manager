package org.systemx.mvn.manager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import org.systemx.happ.HomeApp;
import org.systemx.util.string.USF;

public class DevManagerSettings {

	private File _APP_DIR;

	private Properties _PROPS;

	public final static String PN__PROJECT_DIR = "project.dir";

	private final static String _FILE_DEV_MANAGER_SETTINGS = "dev_manager.properties";

	public DevManagerSettings(File appDir) {
		this._APP_DIR = appDir;

	}

	public String getProperty(String propertyName) {

		Properties p = getProperties();

		if (p == null)
			throw new RuntimeException("File with settings is null");

		return p.getProperty(propertyName);
	}

	public void setProperty(String propertyName, String propertyValue) {

		Properties p = getProperties();

		if (p == null)
			p = new Properties();

		p.setProperty(propertyName, propertyValue);

		saveProperties();
	}

	private Properties getProperties() {

		if (_PROPS == null)
			readProperties();

		if (_PROPS == null)
			throw new RuntimeException("Could't create properties");

		return _PROPS;
	}

	private void readProperties() {

		File settings = getFileSettings();

		if (!settings.exists())
			throw new RuntimeException("File with settings not exists");

		this._PROPS = new Properties();

		try {

			_PROPS.load(new InputStreamReader(new BufferedInputStream(
					new FileInputStream(settings)), "UTF-8"));

		} catch (IOException e) {
			e.printStackTrace();
			_PROPS = null;
		}

	}

	private void saveProperties() {

		try {

			FileOutputStream fos = new FileOutputStream(getFileSettings());

			_PROPS.store(fos, null);

			fos.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	File getFileSettings() {
		return new File(new File(_APP_DIR, HomeApp.CONF),
				_FILE_DEV_MANAGER_SETTINGS);

	}

	public String toString(boolean needProperties) {

		Properties p = getProperties();

		if (p == null)
			return "";

		StringBuilder sb = new StringBuilder();

		for (String k : p.stringPropertyNames())
			sb.append(USF.f("Key[%s]/Value[%s]\n", k, p.getProperty(k)));
		return sb.toString();
	}

	public void create() {

		File f = getFileSettings();

		if (!f.exists())
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}

		if (!f.exists())
			throw new RuntimeException("Could't create file ["
					+ _FILE_DEV_MANAGER_SETTINGS + "]");
	}
}
