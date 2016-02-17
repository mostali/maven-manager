package org.systemx.happ;

public class FileExistAsFileException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final String fileName;

	public FileExistAsFileException(String m, String filePath) {
		super(m);
		this.fileName = filePath;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

}
