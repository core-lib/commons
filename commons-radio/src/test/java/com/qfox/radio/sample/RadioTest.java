package com.qfox.radio.sample;

import com.qfox.radio.builder.DefaultRadioStationBuilder;
import com.qfox.radio.builder.RadioStationBuilder;

public class RadioTest {

	public static void main(String[] args) throws Exception {
		RadioStationBuilder builder = new DefaultRadioStationBuilder();
		RadioStation station = (RadioStation) builder.build(RadioStation.class);
		station.play("", "sdf");
	}

}
