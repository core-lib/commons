package com.qfox.radio.sample;

import com.qfox.radio.core.Filter;
import com.qfox.radio.core.Signal;

public class LogFilter implements Filter {

	public void filtrate(Signal signal, Chain chain) {
		System.out.println("filtrating signal : " + signal);
		chain.next(signal);
	}

}
