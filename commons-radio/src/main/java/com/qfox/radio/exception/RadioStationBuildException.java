package com.qfox.radio.exception;

/**
 * 广播基站建造异常
 * 
 * @author Payne
 * 
 */
public class RadioStationBuildException extends RuntimeException {
	private static final long serialVersionUID = -1559883307745242075L;

	public RadioStationBuildException(String message, Throwable cause) {
		super(message, cause);
	}

	public RadioStationBuildException(String message) {
		super(message);
	}

	public RadioStationBuildException(Throwable cause) {
		super(cause);
	}

}
