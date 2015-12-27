package com.qfox.radio.core;

import java.util.Set;

/**
 * 收听
 * 
 * @author Payne
 * 
 */
public class Listening {
	public final String frequency;
	public final Broadcaster broadcaster;
	public final Set<Receiver> receivers;

	public Listening(String frequency, Broadcaster broadcaster, Set<Receiver> receivers) {
		super();
		this.frequency = frequency;
		this.broadcaster = broadcaster;
		this.receivers = receivers;
	}

}