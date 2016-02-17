package org.systemx.happ;

public class AppPartNotFound extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AppPartNotFound() {
	}

	public AppPartNotFound(String message) {
		super(message);
	}

}
