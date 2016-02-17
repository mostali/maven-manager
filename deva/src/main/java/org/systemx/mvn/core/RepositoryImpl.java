package org.systemx.mvn.core;

import java.io.File;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;

public class RepositoryImpl implements IRepository {

	private File _FILE_REPOSITORY;

	public RepositoryImpl(File file_rep) {
		this._FILE_REPOSITORY = file_rep;

		if (!file_rep.isDirectory())
			throw new RuntimeException("Repository [" + file_rep
					+ "] is not exist!");
	}

	public File getRepositoryFile() {
		return _FILE_REPOSITORY;
	}

	public File getArtifactFile(IArtifact artifact) {

		File artifact_file = new File(getRepositoryFile(),
				artifact.getRelativePath());

		return artifact_file.exists() ? artifact_file : null;

	}

	public File getArtifactFile(Dependency d) {

		File artifact_file = new File(getRepositoryFile(), getRelativePath(d));

		return artifact_file.exists() ? artifact_file : null;

	}

	private static String getRelativePath(Dependency d) {

		String artifact_part_dir = toPath(d.getGroupId(), d.getArtifactId(),
				d.getVersion());

		String artifact_part_file = getArtifactFileName(d);

		return artifact_part_dir + artifact_part_file;
	}

	public static String toPath(String groupId, String artifactId,
			String version) {

		String part_group = groupId.replaceAll("\\.", "/");

		return part_group + "/" + artifactId + "/" + version + "/";
	}

	public static String getArtifactFileName(Model m) {
		String artifact_part_file = createArtifactName(m.getArtifactId(),
				m.getVersion(), m.getPackaging());

		return artifact_part_file;
	}

	public static String getArtifactFileName(Dependency d) {
		String artifact_part_file = createArtifactName(d.getArtifactId(),
				d.getVersion(), d.getType());

		return artifact_part_file;
	}

	private static String createArtifactName(String artifactId, String version,
			String artifactFileType) {

		return artifactId + "-" + version + "." + artifactFileType;
	}

	@Override
	public String toString() {
		return _FILE_REPOSITORY.getAbsoluteFile().toString();
	}

}
