package org.systemx.mvn.manager;

import java.io.File;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.maven.cli.MavenCli;
import org.systemx.mvn.core.IProject;
//http://stackoverflow.com/questions/4838591/is-there-a-library-for-reading-maven2-3-pom-xml-files
//http://svn.apache.org/viewvc/archiva/trunk/archiva-modules/plugins/maven2-repository/src/main/java/org/apache/archiva/metadata/repository/storage/maven2/Maven2RepositoryStorage.java?revision=1053542&view=markup
//http://www.apache-maven.ru/plugins/maven-checkstyle-plugin.html
import org.systemx.mvn.core.MavenPhase;

public class MavenRun {

	private static Logger log = LogManager.getLogger(MavenRun.class);

	public static final int FORCE_EXIT = -100500;

	public static boolean isTrueResult(int result) {
		return result == 0;
	}

	public static void main(String[] args) {

		// String _projectName = "dictServer";
		// String _projectVersion = "0.1.0";
		// String _projectPath = "C:\\test\\";
		// info();
		// saveModel(new DevaX(_projectName, _projectVersion, _projectPath) {
		// });

		mvn(new IProject() {

			public File getDirProject() {
				return new File("C:\\test");
			}
		}, MavenPhase.clean);
	}

	public static int mvn(IProject project, MavenPhase mavenPhase) {
		INFO("Processing...");

		MavenCli cli = new MavenCli();

		int result = cli.doMain(new String[] { mavenPhase.getPhaseCommand() },
				project.getDirProject().getAbsolutePath(), System.out,
				System.out);

		INFO("Maven processing project[%s], goal[%s], result[%s]", project,
				mavenPhase, getResultMessage(result));

		return result;
	}

	public static String getResultMessage(int result) {

		String RESULT_MES = result == 0 ? f("SUCCESS/%s", result) : f(
				"FAIL/%s", result);

		return RESULT_MES;
	}

	public static int mvn(String projectDir, MavenPhase mavenPhase) {

		MavenCli cli = new MavenCli();

		int result = cli.doMain(new String[] { mavenPhase.getPhaseCommand() },
				projectDir, System.out, System.out);

		return result;
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

	protected static void p(String msg, Object... args) {
		System.out.println(String.format(msg, args));
	}

}