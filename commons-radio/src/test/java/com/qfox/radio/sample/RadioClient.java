package com.qfox.radio.sample;

import com.qfox.radio.annotation.Listener;
import com.qfox.radio.annotation.Message;
import com.qfox.radio.annotation.Radiogram;

@Radiogram
public class RadioClient {

	@Listener(frequency = "music")
	public void listen(@Message("song") String song) {
		System.out.println("radio client listening music:" + song + " from radio station");
	}

	@Listener(frequency = "sing")
	public void listen(@Message("song") String song, @Message("result") int result) {
		System.out.println("radio client listening song:" + song + " of channel named [sing] from radio station");
	}

	@Listener(frequency = "test")
	public void test(@Message("result") Object result) {
		System.out.println(result);
	}

}
