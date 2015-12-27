package com.qfox.radio.core;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 信号广播器
 * 
 * @author Payne
 * 
 */
public enum Broadcaster {
	/**
	 * 同步广播器
	 */
	SYNCHRONOUS {
		@Override
		public void broadcast(Signal signal, Set<Receiver> receivers) {
			List<Receiver> _receivers = new LinkedList<Receiver>(receivers);
			Collections.sort(_receivers, new Comparator<Receiver>() {
				public int compare(Receiver a, Receiver b) {
					return a.intensity > b.intensity ? -1 : a.intensity < b.intensity ? 1 : 0;
				}
			});

			for (Receiver receiver : _receivers) {
				receiver.receive(signal);
			}
		}
	},

	/**
	 * 异步广播器
	 */
	ASYNCHRONOUS {
		private final ExecutorService POOL = Executors.newFixedThreadPool(24);

		@Override
		public void broadcast(final Signal signal, Set<Receiver> receivers) {
			for (final Receiver receiver : receivers) {
				POOL.execute(new Runnable() {

					public void run() {
						Thread.currentThread().setPriority(receiver.intensity);
						receiver.receive(signal);
					}

				});
			}
		}
	};

	public abstract void broadcast(Signal signal, Set<Receiver> receivers);

}
