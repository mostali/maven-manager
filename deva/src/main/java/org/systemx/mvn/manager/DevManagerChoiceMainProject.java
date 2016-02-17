package org.systemx.mvn.manager;

import static org.systemx.util.string.USF.LINE;
import static org.systemx.util.string.USF.f;
import static org.systemx.util.string.USF.p;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.maven.settings.Settings;
import org.apache.maven.settings.io.xpp3.SettingsXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.systemx.happ.utils.SysProperty;
import org.systemx.mvn.core.IProject;
import org.systemx.mvn.core.IRepository;
import org.systemx.mvn.core.MavenPhase;
import org.systemx.mvn.core.ProjectImpl;
import org.systemx.mvn.core.RepositoryImpl;
import org.systemx.util.quest.EAnswer;
import org.systemx.util.string.US;
import org.systemx.util.validate.AbstractValidator;
import org.systemx.util.validate.IntegerValidator;
import org.systemx.util.validate.RangePair;

public class DevManagerChoiceMainProject {

	/*
	 * ----------------------------------NAMES--------------------------------
	 */
	public final static String _FILE_POM = "pom.xml";
	private final static String _FILE_M2_SETTINGS_NAME = "settings.xml";
	private final static String _DIR_REPOSITORY_NAME = "repository/";
	// private final static String _APP_NAME = "devManager1";

	/*
	 * ----------------------------------PATHS--------------------------------
	 */
	private final static String _DIR_M2_HOME = SysProperty.USER_HOME
			.getPropertyValue() + "/.m2/";

	private final static String _FILE_M2_SETTINGS = _DIR_M2_HOME
			+ _FILE_M2_SETTINGS_NAME;

	private final static String _FILE_DEFAULT_M2_REPOSITORY = _DIR_M2_HOME
			+ _DIR_REPOSITORY_NAME;

	/*
	 * ----------------------------------VARIABLE--------------------------
	 */

	private IRepository _REPO;

	private DevManagerSettings _DEV_MANAGER_SETTINGS;

	private File _DIR_PROJECTS;

	private IProject _PROJECT;

	private MavenPhase _PHASE;

	private Map<String, IProject> _MAP_PROJECT;

	public Map<String, IProject> getMapProjects() {
		return _MAP_PROJECT;
	}

	public IRepository getRepository() {
		return _REPO;
	}

	public IProject getProject() {
		return _PROJECT;
	}

	public MavenPhase getPhase() {
		return _PHASE;
	}

	public static String REPODIR() {
		return _FILE_DEFAULT_M2_REPOSITORY;
	}

	/*
	 * ----------------------------------HOME APP--------------------------
	 */
	private final DevManager _HOME_APP;

	public DevManager getApp() {
		return _HOME_APP;
	}

	/*
	 * ----------------------------------CONSTRUCTOR--------------------------
	 */

	public DevManagerChoiceMainProject(DevManager homeApp) {
		super();
		this._HOME_APP = homeApp;
	}

	public boolean DIA_CHOICE_PROJECT() {

		if (!CHECK_READY_DEV_MANAGER_SETTINGS()) {
			return false;
		}
		if (!CHECK_READY_REPO()) {
			return false;
		}
		if (!CHECK_DIR_PROJECTS()) {
			return false;
		}

		if (!CREATE_MAP_PROJECT()) {
			return false;
		}
		if (!CHOICE_PROJECT()) {
			return false;
		}

		if (!CHOICE_MAVEN_PHASE()) {
			return false;
		}

		return true;
	}

	private void P_EXIT() {
		DevManager.P_EXIT();
	}

	// ///////////////////////////////////////////////////
	// ----------------------------------CHOICE MAVEN PHASE
	// ///////////////////////////////////////////////////
	private boolean CHOICE_MAVEN_PHASE() {

		P_LIST_MAVEN_PHASES();

		EAnswer number = Q_CHOICE_MAVEN_PHASE(MavenPhase.values().length);

		_PHASE = MavenPhase.values()[Integer.valueOf(number.getAnswerObject()) - 1];

		return true;
	}

	private EAnswer Q_CHOICE_MAVEN_PHASE(int countPhases) {

		String quest = "Enter the number of maven phase:";

		EAnswer possible = EAnswer.NUMERIC.setPossibleAnswer("1 - "
				+ countPhases);

		possible.setValidator(new IntegerValidator()
				.setRangePair(new RangePair(1, countPhases)));

		EAnswer answer = EAnswer.QUEST(quest, possible);

		return answer;

	}

	private void P_LIST_MAVEN_PHASES() {

		p("CHOICE MAVEN GOAL");
		p(LINE().toString());

		StringBuilder sb = new StringBuilder();

		int i = 0;

		for (MavenPhase ph : MavenPhase.values()) {
			sb.append(++i).append(". ").append(ph.getPhaseCommand())
					.append("\n");
		}
		p(sb.toString());
		// p(LINE().toString());
	}

	// ///////////////////////////////////////////////////
	// ----------------------------------CREATE MAP PROJECTS
	// ///////////////////////////////////////////////////
	private boolean CREATE_MAP_PROJECT() {

		String pathProjects = _DEV_MANAGER_SETTINGS
				.getProperty(DevManagerSettings.PN__PROJECT_DIR);

		File[] listProjects = getListProjects(new File(pathProjects));

		if (listProjects == null || listProjects.length == 0) {
			p("Directory [%s] not contain projects.\n");
			return false;
		}

		_MAP_PROJECT = new HashMap<String, IProject>();

		for (File project : listProjects)
			_MAP_PROJECT.put(project.getName(), new ProjectImpl(project));

		return true;
	}

	// ///////////////////////////////////////////////////
	// ----------------------------------CHOICE PROJECT
	// ///////////////////////////////////////////////////
	private boolean CHOICE_PROJECT() {

		Set<String> projects = getMapProjects().keySet();

		String[] projectsArray = projects.toArray(new String[projects.size()]);

		P_LIST_PROJECTS(projects);

		EAnswer choice_project_Q = Q_CHOICE_PROJECT(projectsArray.length);

		if (choice_project_Q == null) {// IF ANSWER NOT RECIEVED

			p(DevManager._MESSAGE_EXIT_ERROR);

			return false;
		}

		int choice_project = Integer
				.valueOf(choice_project_Q.getAnswerObject());

		String keyProjectName = projectsArray[choice_project - 1];

		_PROJECT = getMapProjects().get(keyProjectName);

		return true;

	}

	private File[] getListProjects(File dir) {
		return dir.listFiles(new FileFilter() {

			public boolean accept(File pathname) {
				return (pathname.isDirectory() && new File(pathname, _FILE_POM)
						.isFile());
			}
		});
	}

	private void P_LIST_PROJECTS(Set<String> projects) {

		String pathProjects = _DEV_MANAGER_SETTINGS
				.getProperty(DevManagerSettings.PN__PROJECT_DIR);

		p("This projects contain in folder [%s]", pathProjects);
		p(LINE().toString());

		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (String projectName : projects) {
			sb.append(++i).append(". ").append(projectName).append("\n");
		}
		p(sb.toString());
		// p(LINE().toString());
	}

	private EAnswer Q_CHOICE_PROJECT(int countProjects) {

		String quest = "Enter the number of project:";

		EAnswer possible = EAnswer.NUMERIC.setPossibleAnswer("1 - "
				+ countProjects);

		possible.setValidator(new IntegerValidator()
				.setRangePair(new RangePair(1, countProjects)));

		EAnswer answer = EAnswer.QUEST(quest, possible);

		return answer;

	}

	// ////////////////////////////////////////////////////
	// ----------------------------------CHECK DIR PROJECTS
	// ////////////////////////////////////////////////////
	private boolean CHECK_DIR_PROJECTS() {

		if (!_DEV_MANAGER_SETTINGS.getFileSettings().exists()) {

			EAnswer ans_create_settings = Q_WANT_CREATE_SETTINGS();

			switch (ans_create_settings) {

			case YES: {

				EAnswer ans_input_path = Q_ADD_DIR_WITH_PROJECTS__CHARS();

				switch (ans_input_path) {

				case CHARS: {

					String dir_with_projects = ans_input_path.getAnswerObject();

					// _HOME_APP.createAppPart(HomeApp.CONF);

					_DEV_MANAGER_SETTINGS.create();

					_DEV_MANAGER_SETTINGS.setProperty(
							_DEV_MANAGER_SETTINGS.PN__PROJECT_DIR,
							dir_with_projects);

					return true;
				}
				case STOP: {
					return false;
				}
				}
			}
			case NO: {
				return false;
			}
			}

		}

		String pathProjects = _DEV_MANAGER_SETTINGS
				.getProperty(DevManagerSettings.PN__PROJECT_DIR);

		if (pathProjects == null)
			throw new RuntimeException(f(
					"Property [%s] equals null, all properties\n %s",
					DevManagerSettings.PN__PROJECT_DIR,
					_DEV_MANAGER_SETTINGS.toString(true)));

		_DIR_PROJECTS = new File(pathProjects);

		if (!_DIR_PROJECTS.isDirectory()) {

			String mes = f(
					"An error occurred. Directory with projects  [%s] not found!\n"
							+ "Check property '%s' in file ~/.m2/%s/%s, format [%s:/path/to/dir ;]\n",
					_DIR_PROJECTS, _DEV_MANAGER_SETTINGS.PN__PROJECT_DIR,
					DevManager._APP_NAME, _DEV_MANAGER_SETTINGS
							.getFileSettings().getAbsoluteFile(),
					_DEV_MANAGER_SETTINGS.PN__PROJECT_DIR);

			p(mes);

			return false;
		}

		return true;
	}

	private EAnswer Q_ADD_DIR_WITH_PROJECTS__CHARS() {

		String quest = "Enter the path of directory with projects. Directory must be exist.\n";

		EAnswer possible = EAnswer.CHARS.setPossibleAnswer("FILE PATH");

		possible.setValidator(new AbstractValidator<String>() {
			@Override
			public boolean validate(String object) {

				if (object == null)
					return false;

				return new File(object).exists();
			}
		});

		EAnswer answer = EAnswer.QUEST(quest, possible, EAnswer.STOP);

		return answer;

	}

	private EAnswer Q_WANT_CREATE_SETTINGS() {

		String quest = f(
				"File with settings not found in dir [%s]. Create settings?\n",
				getApp().getPartFile());

		EAnswer answer = EAnswer.QUEST(quest, EAnswer.YES, EAnswer.NO);

		return answer;

	}

	// ////////////////////////////////////////////////////////
	// ----------------------------------CHECK DEV MANAGER SETTINGS
	// ////////////////////////////////////////////////////////
	private boolean CHECK_READY_DEV_MANAGER_SETTINGS() {

		_DEV_MANAGER_SETTINGS = new DevManagerSettings(getApp().getPartFile());

		// _DEV_MANAGER_SETTINGS.setFileSettings(settings);

		return true;
	}

	// ///////////////////////////////////////////////////
	// ----------------------------------CHECK_READY_REPO
	// ///////////////////////////////////////////////////
	private boolean CHECK_READY_REPO() {

		_REPO = createUserRepository();

		if (_REPO == null) {// IF USER REPO NOT EXIST

			EAnswer use_local_repo_Q = Q_USE_LOCAL_REPO_YN();

			if (use_local_repo_Q == null) {// IF ANSWER NOT RECIEVED

				p(DevManager._MESSAGE_EXIT_ERROR);

				return false;
			}

			if (use_local_repo_Q == EAnswer.YES)// IF USE LOCAL REPO

				_REPO = createDefaultRepository(); // CREATE_LOCAL_REPO|FILE/user.home/.m2/setting.xml

			else {
				// IF NOT USE LOCAL REPO
				// p(_MESSAGE_EXIT_STOP);
				return false;
			}

		}

		return true;
	}

	private EAnswer Q_USE_LOCAL_REPO_YN() {

		String quest = f(
				"Users repository not found in file [~/settings.xml]. Use default repository?\n"
						+ "Location default repository[%s]?",
				_FILE_DEFAULT_M2_REPOSITORY);

		EAnswer answer = EAnswer.QUEST(quest, EAnswer.YES, EAnswer.NO);

		return answer;

	}

	private RepositoryImpl createUserRepository() {

		try {
			File file_settings = new File(_FILE_M2_SETTINGS);

			if (!file_settings.exists()) {

				return null;

			} else {

				SettingsXpp3Reader settingsReader = new SettingsXpp3Reader();

				BufferedReader in = new BufferedReader(new InputStreamReader(
						new FileInputStream(file_settings), "UTF-8"));

				Settings settings = settingsReader.read(in);

				File file_repo = getFileRepositoty(settings);

				return (file_repo == null || !file_repo.exists()) ? null
						: new RepositoryImpl(file_repo);

			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}

		return null;
	}

	private File getFileRepositoty(Settings settings_xml) {

		String path = settings_xml.getLocalRepository();

		if (US.isEmpty(path))

			return null;

		return new File(path);
	}

	private RepositoryImpl createDefaultRepository() {

		File file_rep = new File(_FILE_DEFAULT_M2_REPOSITORY);

		RepositoryImpl repImpl = new RepositoryImpl(file_rep);

		return repImpl;
	}

}
