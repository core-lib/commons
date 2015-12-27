package com.qfox.radio.core;

import java.util.Iterator;

/**
 * 滤波器
 * 
 * @author Payne
 * 
 */
public interface Filter {

	/**
	 * 对信号进行过滤,只有在该方法中执行过滤链的next方法信号才能继续过滤或广播
	 * 
	 * @param signal
	 *            信号
	 * @param chain
	 *            过滤链
	 */
	void filtrate(Signal signal, Chain chain);

	/**
	 * 过滤链
	 * 
	 * @author Payne
	 * 
	 */
	public static final class Chain {
		private Iterator<Filter> iterator;

		public Chain(Iterator<Filter> iterator) {
			this.iterator = iterator;
		}

		/**
		 * 进入下一个滤波器 当filter 不执行该方法那么信号将会取消广播也不会进入下一个滤波器
		 * 
		 * @param signal
		 *            信号
		 */
		public void next(Signal signal) {
			iterator.next().filtrate(signal, this);
		}

	}

}
