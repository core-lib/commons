package com.qfox.radio.sample;

import com.qfox.radio.annotation.Channel;
import com.qfox.radio.annotation.Station;

@Station
public class RadioStationSub extends RadioStation {

	@Channel(frequency = "test")
	public void test() throws Exception {
		play("test", "test2");
	}

}
