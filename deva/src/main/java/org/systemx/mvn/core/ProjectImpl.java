package org.systemx.mvn.core;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.systemx.mvn.manager.DevManagerChoiceMainProject;

public class ProjectImpl extends ArtifactImpl implements IProject {

	private File _FILE_DEV_PROJECT;

	public ProjectImpl(File projectFile) {
		super();

		this._FILE_DEV_PROJECT = projectFile;

	}

	@Override
	public Model getPomModel() {

		if (super.getPomModel() == null) {
			Model pom_model = getPomModel(getDirProject());

			super.setPomModel(pom_model);
		}

		if (super.getPomModel() == null)
			throw new RuntimeException(
					"Error initializing the model for project ["
							+ getDirProject() + "]");

		return super.getPomModel();
	}

	public static Model getPomModel(File projectDir) {

		File pomFile = new File(projectDir,
				DevManagerChoiceMainProject._FILE_POM);

		if (!pomFile.exists())
			throw new RuntimeException("File pom.xml not exist for project "
					+ projectDir.getName() + " file [" + pomFile + "]");

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

	public static void writePomModel(Model model) throws IOException {

		MavenXpp3Writer mavenreader = new MavenXpp3Writer();

		mavenreader.write(new FileWriter(model.getPomFile()), model);

	}

	public File getDirProject() {
		return _FILE_DEV_PROJECT;
	}

	@Override
	public String toString() {
		return getDirProject().getName();
	}
}
