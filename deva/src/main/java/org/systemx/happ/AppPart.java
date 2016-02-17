package org.systemx.happ;

import java.io.File;

import org.systemx.happ.utils.LFileName;

public class AppPart {

	private final String partName;

	private final File partFile;

	public AppPart(String rootDir, String partName) {
		this(new File(rootDir), partName);
	}

	public AppPart(File dir, String partName) {

		RE_CHECK_FILE_NAME(partName);

		this.partName = partName;

		this.partFile = new File(dir, partName);

		this.createAppPart(partFile);
	}

	public AppPart(File homeDir) {
		this(homeDir.getParentFile(), homeDir.getName());
	}

	private void RE_CHECK_FILE_NAME(String fileName) {
		if (!LFileName.FS.matches(fileName))
			throw new RuntimeException(
					f("Invalid app part name [%s]", partName));
	}

	protected void createAppPart(String partName) {

		this.RE_CHECK_FILE_NAME(partName);

		this.createAppPart(new File(partFile, partName));

	}

	/**
	 * 
	 * @param partName
	 * @return
	 * @throws AppPartNotFound
	 *             if directory not found
	 * 
	 */
	public File getAppPart(String partName) {

		if (LFileName.FS.matches(partName)) {

			File f = new File(partFile, partName);

			if (f.isDirectory())
				return f;
		}

		throw new AppPartNotFound("File not found ["
				+ new File(partFile, partName) + "]");
	}

	protected void createAppPart(File f) {

		if (f.exists()) {
			if (f.isFile())
				throw new FileExistAsFileException(
						f("File [%s] exists as file! Could't create directory with same name.",
								f), f.getAbsolutePath());
		} else
			f.mkdir();

	}

	/**
	 * @return the partName
	 */
	public String getPartName() {
		return partName;
	}

	public File getPartFile() {
		return partFile;
	};

	/*
	 * --------------------UTILS-----------------------
	 */
	protected static void p(String s, Object... o) {
		System.out.println(f("[" + s + "]", o));
	};

	protected static String f(String s, Object... o) {
		return String.format(s, o);
	};
}
