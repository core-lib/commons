package com.qfox.radio.sample;

import com.qfox.radio.annotation.Channel;
import com.qfox.radio.annotation.Message;
import com.qfox.radio.annotation.Station;
import com.qfox.radio.core.Broadcaster;

@Station(filters = { LogFilter.class })
public class RadioStation {

	@Channel(frequency = "music", broadcaster = Broadcaster.SYNCHRONOUS)
	public void play(@Message("song") String song, @Message("next") String next) throws Exception {
		System.out.println("radio station playing song : " + song);
		sing(song);
	}

	@Channel(frequency = "sing", broadcaster = Broadcaster.SYNCHRONOUS)
	public Integer sing(@Message("song") String song) {
		System.out.println("radio station singing song : " + song);
		return 2;
	}

}
