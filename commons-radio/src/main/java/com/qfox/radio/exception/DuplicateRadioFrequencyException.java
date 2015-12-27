package com.qfox.radio.exception;

/**
 * 重复的广播频率异常
 * 
 * @author Payne
 * 
 */
public class DuplicateRadioFrequencyException extends RuntimeException {
	private static final long serialVersionUID = -2668026983221098320L;

	public DuplicateRadioFrequencyException(String message, Throwable cause) {
		super(message, cause);
	}

	public DuplicateRadioFrequencyException(String message) {
		super(message);
	}

}
