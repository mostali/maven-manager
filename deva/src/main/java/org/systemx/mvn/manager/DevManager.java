package org.systemx.mvn.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.maven.cli.MavenCli;
import org.systemx.happ.HomeApp;
import org.systemx.mvn.core.IProject;
import org.systemx.mvn.core.IRepository;
import org.systemx.mvn.core.MavenPhase;
import org.systemx.util.quest.EAnswer;
import org.systemx.util.string.USF;

public class DevManager extends HomeApp {

	private static Logger log = LogManager.getLogger(DevManager.class);

	private final USF F = new USF();
	/*
	 * ----------------------------------APP NAME-----------------y---------
	 */
	public final static String _APP_NAME = "devManager-0.1.1";

	/*
	 * ----------------------------------MESSAGES--------------------------
	 */
	public static String _MESSAGE_EXIT_STOP = "The Program [Maven Manager] has been stopped!";
	public static String _MESSAGE_EXIT_ERROR = "An error occurred.";

	private DevManagerChoiceMainProject _MAN_CHOICE_PROJECT;

	public DevManagerChoiceMainProject getManagerProject() {
		return _MAN_CHOICE_PROJECT;
	}

	private void clearData() {
		_MAN_CHOICE_PROJECT = null;
	}

	/*
	 * ----------------------------------CONSTRUCTOR--------------------------
	 */
	private DevManager() {
		super(_APP_NAME);

	}

	private static void runTest() {
		MavenCli cli = new MavenCli();

		int result = cli.doMain(new String[] { "clean" }, "D:\\p4f\\test-111",
				System.out, System.out);

		INFO("RUN[%s]", result);
	}

	public static void main(String[] args) {

		log.info("Initializing [Maven Manager]..");

		DevManager devMan = new DevManager();

		devMan.run();
	}

	private void run() {

		if (!this.DIA_START_MAVEN()) {
			P_EXIT();
			return;
		}

		boolean exit = false;

		do {
			// ===================================CHOICE PROJECT&REPO&AMULET
			if (!this.DIA_CHOICE_PROJECT_AND_MAVEN_GOAL()) {

				P_EXIT();
				return;
			}

			// ===================================CHOICE PROJECT&REPO&AMULET
			// ===================================CYCLE ALREADY
			if (EAnswer.YES != this.Q_START_MAVEN_ALREADY())
				exit = true;

			else
				this.clearData();

		} while (!exit);

		P_EXIT();

	}

	private int runMavenPhase() {

		IProject project = getManagerProject().getProject();

		MavenPhase phase = getManagerProject().getPhase();

		Map<String, IProject> allProjects = getManagerProject()
				.getMapProjects();
		IRepository repository = getManagerProject().getRepository();

		List<IProject> installedProjects = new ArrayList<IProject>();

		int result = new DevManagerRun(project, phase, allProjects, repository,
				installedProjects).setHostProject(true).run();

		return result;
	}

	// ///////////////////////////////////////////////////
	// ----------------------------------DIALOG CHOICE PROJECT
	// ///////////////////////////////////////////////////
	private boolean DIA_CHOICE_PROJECT_AND_MAVEN_GOAL() {

		_MAN_CHOICE_PROJECT = new DevManagerChoiceMainProject(this);

		boolean result = _MAN_CHOICE_PROJECT.DIA_CHOICE_PROJECT();

		return result;
	}

	// ///////////////////////////////////////////////////
	// ----------------------------------PRINT RESULT
	// ///////////////////////////////////////////////////
	private void P_RESULT_MAVEN_RUN1(int result) {

		String RESULT_MES = result == 0 ? "SUCCESS" : f("FAIL, ERROR CODE[%s]",
				result);

		String message = f("Project [%s], goal [%s] is %s !",
				getManagerProject().getProject().getDirProject().getName(),
				getManagerProject().getPhase().getPhaseCommand(), RESULT_MES);
		F.PLINE();
		INFO(message);
	}

	// ///////////////////////////////////////////////////
	// ----------------------------------START ALREADY
	// ///////////////////////////////////////////////////
	private EAnswer Q_START_MAVEN_ALREADY() {

		String quest = "Continue the work [Maven Manager]?";

		EAnswer answer = EAnswer.QUEST(quest, EAnswer.YES, EAnswer.NO);

		return answer;

	}

	// ///////////////////////////////////////////////////
	// ----------------------------------START MAVEN MANAGER
	// ///////////////////////////////////////////////////

	private boolean DIA_START_MAVEN() {

		EAnswer start = Q_START_MAVEN();

		if (start == null || start == EAnswer.NO) {

			return false;

		}
		return true;
	}

	private EAnswer Q_START_MAVEN() {

		String repo = DevManagerChoiceMainProject.REPODIR();

		// String ps =getManagerProject().(DevManagerSettings.PN__PROJECT_DIR);

		String quest = "Continue?";

		EAnswer answer = EAnswer.QUEST(quest, EAnswer.YES, EAnswer.NO);

		return answer;

	}

	public static void INFO(String message, Object... args) {
		log.info(f(message, args));
	}

	public static void DEBUG(String message, Object... args) {
		log.debug(f(message, args));
	}

	public static String f(String s, Object... args) {
		return String.format(s, args);
	}

	public static void P_EXIT() {
		// p(_MESSAGE_EXIT_STOP);
		INFO(_MESSAGE_EXIT_STOP);
	}
}
