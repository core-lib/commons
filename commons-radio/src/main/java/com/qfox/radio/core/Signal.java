package com.qfox.radio.core;

import java.util.HashMap;
import java.util.Map;

/**
 * 信号
 * 
 * @author Payne
 * 
 */
public final class Signal {
	public final String frequency; // 信号频率
	public final boolean successful;
	private Map<String, Object> messages = new HashMap<String, Object>(); // 信号携带消息

	public Signal(String frequency, boolean successful, Map<String, Object> messages) {
		super();
		this.frequency = frequency;
		this.successful = successful;
		this.messages = messages;
	}

	public Map<String, Object> getMessages() {
		return messages;
	}

	public void setMessages(Map<String, Object> messages) {
		this.messages = messages;
	}

}
