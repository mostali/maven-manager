package org.systemx.mvn.core;

import java.io.File;

import org.apache.maven.model.Dependency;

public interface IRepository {

	File getArtifactFile(IArtifact artifact);

	File getArtifactFile(Dependency d);
}
