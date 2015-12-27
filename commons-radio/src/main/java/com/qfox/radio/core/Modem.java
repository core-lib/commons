package com.qfox.radio.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import com.qfox.radio.annotation.Channel;
import com.qfox.radio.annotation.Message;
import com.qfox.radio.exception.RadioStationBuildException;

/**
 * 信号广播modem
 * 
 * @author Payne
 * 
 */
public final class Modem implements MethodInterceptor, Filter {
	private final Object station;
	private final List<Filter> filters;
	private final Map<String, Listening> listenings;

	public Modem(Object station, List<Filter> filters, Map<String, Listening> listenings) {
		this.station = station;
		this.filters = filters;
		this.listenings = listenings;
	}

	public Object intercept(Object enhanced, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		if (!method.isAnnotationPresent(Channel.class)) {
			return method.invoke(station, args);
		}

		Channel channel = method.getAnnotation(Channel.class);
		String frequency = channel.frequency();

		Map<String, Object> messages = new HashMap<String, Object>();
		Annotation[][] annotations = method.getParameterAnnotations();
		for (int i = 0; i < annotations.length; i++) {
			for (Annotation annotation : annotations[i]) {
				if (!(annotation instanceof Message)) {
					continue;
				}
				Message message = (Message) annotation;
				messages.put(message.value(), args[i]);
			}
		}

		Boolean successful = null;

		try {
			Object result = method.invoke(station, args);
			successful = true;
			Message message = channel.result();
			if (!message.value().equals("")) {
				if (messages.containsKey(message.value())) {
					throw new RadioStationBuildException("can not mapping duplicate key : [" + message.value() + "] for signal message in channel : " + method.getName() + " of station : " + enhanced.getClass());
				}
				messages.put(message.value(), result);
			}

			return result;
		} catch (Exception e) {
			successful = false;
			Message message = channel.exception();
			if (!message.value().equals("")) {
				if (messages.containsKey(message.value())) {
					throw new RadioStationBuildException("can not mapping duplicate key : [" + message.value() + "] for signal message in channel : " + method.getName() + " of station : " + enhanced.getClass());
				}
				messages.put(message.value(), e.getCause());
			}

			throw e;
		} finally {
			Signal signal = new Signal(frequency, successful, messages);
			List<Filter> filters = new ArrayList<Filter>(this.filters);
			filters.add(this);
			Chain chain = new Chain(filters.iterator());
			chain.next(signal);
		}
	}

	public void filtrate(Signal signal, Chain chain) {
		Listening listening = listenings.get(signal.frequency);
		listening.broadcaster.broadcast(signal, listening.receivers);
	}

}
