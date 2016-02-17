package org.systemx.mvn.core;

public enum MavenPhase {
	clean, test, install, _package("package"), assembly("assembly:assembly"), eclipse_clean(
			"eclipse:clean"), eclipse_eclipse("eclipse:eclipse");

	private final String truePhase;

	private MavenPhase() {
		this(null);
	}

	private MavenPhase(String phase) {
		this.truePhase = phase;
	}

	public String getPhaseCommand() {
		return (this.truePhase == null) ? name() : this.truePhase;
	}

	public boolean isNeedInstallDevelopedDependencies() {

		switch (this) {

		case clean:
			return false;
		case test:
			return true;
		case install:
			return true;
		case assembly:
			return true;
		case _package:
			return true;
		case eclipse_clean:
			return false;
		case eclipse_eclipse:
			return false;

		default:
			return false;

		}
	}
}
