package org.systemx.mvn.core;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

public class ModelRW {

	public static final String FILE_POM = "pom.xml";

	public static Model readModel(File project_dir) {

		File pomFile = new File(project_dir, FILE_POM);

		if (!pomFile.exists())
			throw new RuntimeException("File pom.xml not exist for project "
					+ project_dir);

		Model model = null;

		FileReader reader = null;

		MavenXpp3Reader mavenreader = new MavenXpp3Reader();

		try {

			reader = new FileReader(pomFile);

			model = mavenreader.read(reader);

			model.setPomFile(pomFile);

			return model;

		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean writePomModel(Model model) {

		MavenXpp3Writer mavenreader = new MavenXpp3Writer();

		try {
			mavenreader.write(new FileWriter(model.getPomFile()), model);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;

	}
}
