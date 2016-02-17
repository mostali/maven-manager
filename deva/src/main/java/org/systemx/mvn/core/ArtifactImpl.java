package org.systemx.mvn.core;

import org.apache.maven.model.Model;
import org.systemx.util.string.UA;

public class ArtifactImpl implements IArtifact {

	private Model _MODEL;

	private ArtifactImpl(String groupId, String artifactId, String version,
			String artifactType) {

		this.setGroupId(groupId);

		this.setArtifactId(artifactId);

		this.setVersion(version);

		this.setPackaging(artifactType);

	}

	protected ArtifactImpl() {
		super();
	}

	public String getRelativePath() {

		String artifact_part_dir = RepositoryImpl.toPath(getPomModel()
				.getGroupId(), getPomModel().getArtifactId(), getPomModel()
				.getVersion());

		String artifact_part_file = getArtifactFileName();

		return artifact_part_dir + artifact_part_file;

	}

	public String getPackaging() {

		String packaging = getPomModel().getPackaging();

		if (UA.isEmpty(packaging))
			return "jar";

		return packaging;
	}

	public Model getPomModel() {
		return _MODEL;
	}

	public void setPomModel(Model model) {
		this._MODEL = model;
	}

	public void setGroupId(String groupId) {

		UA.checkArg(groupId, "GroupId is epmty");

		getPomModel().setGroupId(groupId);

	}

	public void setArtifactId(String artifactId) {

		UA.checkArg(artifactId, "ArtifactId is epmty");

		getPomModel().setArtifactId(artifactId);

	}

	public void setVersion(String version) {

		UA.checkArg(version, "Version is epmty");

		getPomModel().setVersion(version);
	}

	public void setPackaging(String packaging) {

		if (UA.isEmpty(packaging))
			packaging = "jar";
		else
			UA.checkEnum(packaging, ArtifactFileType.class,
					"Artifact type is null");

		getPomModel().setPackaging(packaging);

	}

	public String getArtifactFileName() {
		return RepositoryImpl.getArtifactFileName(getPomModel());
	}

}
