package org.systemx.mvn.core;

import org.apache.maven.model.Model;

public interface IArtifact {

	Model getPomModel();

	String getRelativePath();

	String getArtifactFileName();

}
