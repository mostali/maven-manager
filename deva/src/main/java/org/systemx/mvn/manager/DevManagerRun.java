package org.systemx.mvn.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.systemx.mvn.core.IArtifact;
import org.systemx.mvn.core.IProject;
import org.systemx.mvn.core.IRepository;
import org.systemx.mvn.core.MavenPhase;
import org.systemx.util.quest.EAnswer;
import org.systemx.util.string.UA;
import org.systemx.util.string.USF;

public class DevManagerRun {

	private final USF F = new USF("Have inner dependencies?".length());

	/*
	 * ----------------------------------VARIABLE--------------------------
	 */
	private boolean isHostProject;

	private final IProject _PROJECT;

	private final IRepository _REPO;

	private final MavenPhase _PHASE;

	private final Map<String, IProject> _ALL_PROJECTS;

	private final List<IProject> installedProjects;

	private final List<IProject> developedDependecies;

	public boolean isHostProject() {
		return this.isHostProject;
	}

	public DevManagerRun setHostProject(boolean isHostProject) {
		this.isHostProject = isHostProject;
		return this;
	}

	private IProject getInstallProject() {
		return _PROJECT;
	}

	private Map<String, IProject> getMapProjects() {
		return _ALL_PROJECTS;
	}

	private IRepository getRepository() {
		return _REPO;
	}

	private MavenPhase getPhase() {
		return _PHASE;
	}

	private List<IProject> getInstalledProjects() {
		return installedProjects;
	}

	private List<IProject> getInnerDependecies() {
		return developedDependecies;
	}

	/*
	 * ----------------------------------CONSTRUCTOR--------------------------
	 */

	public DevManagerRun(IProject project, MavenPhase phase,
			Map<String, IProject> allProjects, IRepository repository,
			List<IProject> installedProjects) {
		super();

		// this._HOME_APP = homeApp;

		this._PROJECT = project;

		this._PHASE = phase;

		this._ALL_PROJECTS = allProjects;

		this._REPO = repository;

		this.installedProjects = installedProjects;

		this.developedDependecies = phase.isNeedInstallDevelopedDependencies() ? getInnerProjects(_PROJECT)
				: new ArrayList<IProject>();

	}

	private List<IProject> getInnerProjects(IProject iproject) {

		List<IProject> _innerDependecies = new ArrayList<IProject>();

		Model pom = ((IArtifact) iproject).getPomModel();

		List<Dependency> dependencies = pom.getDependencies();

		if (dependencies.isEmpty())
			return new ArrayList<IProject>();

		Iterator<Dependency> it = dependencies.iterator();

		while (it.hasNext()) {

			Dependency dep = it.next();

			IProject innerProject = getInnerProject(dep);

			if (innerProject == null)
				it.remove();
			else
				_innerDependecies.add(innerProject);

		}

		return _innerDependecies;
	}

	private IProject getInnerProject(Dependency dep) {

		String artifact = dep.getArtifactId();
		String group = dep.getGroupId();
		String version = dep.getVersion();

		Iterator<IProject> it = getMapProjects().values().iterator();

		while (it.hasNext()) {

			IArtifact project = (IArtifact) it.next();

			Model model = project.getPomModel();

			if (UA.isEquals(artifact, model.getArtifactId())
					&& UA.isEquals(group, model.getGroupId())
					&& UA.isEquals(version, model.getVersion()))

				return (IProject) project;
		}
		return null;
	}

	public int run() {

		next: for (IProject project : getInnerDependecies()) {

			boolean repo_contain = getRepository().getArtifactFile(
					(IArtifact) project) != null;

			EAnswer answer = null;

			if (repo_contain) {// IF_REPOSITORY_CONTAIN_THIS_PROJECT

				answer = Q_REINSTALL_DEPENDENCY_PROJECT(project);

				if (EAnswer.NO == answer) {
					getInstalledProjects().add(project);
					continue next;
				}

			} else {// IF_REPOSITORY_NOT_CONTAIN_THIS_PROJECT

				answer = Q_INSTALL_DEPENDENCY_PROJECT(project);

				if (EAnswer.NO == answer)
					return MavenRun.FORCE_EXIT;

			}

			if (EAnswer.YES != answer)
				return MavenRun.FORCE_EXIT;

			int result = new DevManagerRun(project, MavenPhase.install,
					getMapProjects(), getRepository(), getInstalledProjects())
					.run();

			if (!MavenRun.isTrueResult(result))
				return result;

			else
				getInstalledProjects().add(project);

		}

		EAnswer start_phase = Q_START_MAVEN_PHASE();

		if (EAnswer.YES == start_phase)
			return MavenRun.mvn(getInstallProject(), getPhase());

		return MavenRun.FORCE_EXIT;

	}

	public EAnswer Q_INSTALL_DEPENDENCY_PROJECT(IProject dependsProject) {

		String quest = USF
				.f("Project [%s] requires <<< artifact [%s]. Install this project?",
						getInstallProject(), dependsProject);

		EAnswer answer = EAnswer.QUEST(quest, EAnswer.YES, EAnswer.NO);

		return answer;
	}

	public EAnswer Q_REINSTALL_DEPENDENCY_PROJECT(IProject dependsProject) {

		String quest = USF
				.f("Project [%s] requires <<< project [%s], wich already installed. Reinstall this project?",
						getInstallProject(), dependsProject);

		EAnswer answer = EAnswer.QUEST(quest, EAnswer.YES, EAnswer.NO);

		return answer;
	}

	private EAnswer Q_START_MAVEN_PHASE() {

		// p("USE DIR WITH PROJECTS [%s]",
		// _DEV_MANAGER_SETTINGS
		// .getProperty(DevManagerSettings.PN__PROJECT_DIR));

		// INFO("PROJECT %s[%s]", F.aw("PROJECT"), getApp().getManagerProject()
		// .getProject().getDirProject().getName());

		// INFO("PHASE %s[%s]", F.aw("PHASE"), getApp().getManagerProject()
		// .getPhase());

		// INFO("REPOSITORY %s[%s]", F.aw("REPOSITORY"), getApp()
		// .getManagerProject().getRepository().toString());

		// INFO("Have inner dependencies? %s[%s]",
		// F.aw("Have inner dependencies?"), getApp().getManagerProject()
		// .getPhase().isNeedInstallDevelopedDependencies());

		// INFO("Processing project [%s], goal[%s]. Run Maven?",
		// getInstallProject(),
		// getPhase());

		// if (getInnerDependecies().isEmpty())

		EAnswer answer = null;

		if (isHostProject()) {

			String quest = USF
					.f("Processing project [%s], goal[%s], repository[%s].\nRun goal [%s]?",
							getInstallProject(), getPhase(), getRepository(),
							getPhase());

			answer = EAnswer.QUEST(quest, EAnswer.YES, EAnswer.NO);
		} else
			answer = EAnswer.YES;

		return answer;

	}

	private void INFO(String mes, Object... args) {
		DevManager.INFO(mes, args);
	}

}
