package com.qfox.radio.builder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.cglib.proxy.Enhancer;

import com.qfox.radio.annotation.Channel;
import com.qfox.radio.annotation.Message;
import com.qfox.radio.annotation.Radiogram;
import com.qfox.radio.annotation.Station;
import com.qfox.radio.core.Broadcaster;
import com.qfox.radio.core.Filter;
import com.qfox.radio.core.Listening;
import com.qfox.radio.core.Modem;
import com.qfox.radio.core.Receiver;
import com.qfox.radio.exception.RadioStationBuildException;

public abstract class AbstractRadioStationBuilder implements RadioStationBuilder {

	private Map<Class<?>, Object> cache = new HashMap<Class<?>, Object>();

	public Object build(Class<?> _class) {
		if (cache.containsKey(_class)) {
			return cache.get(_class);
		}

		if (!_class.isAnnotationPresent(Station.class)) {
			throw new RadioStationBuildException("can not build radio station without Station annotation");
		}
		if (_class.isAnnotationPresent(Radiogram.class)) {
			throw new RadioStationBuildException("radio station must not be a radiogram");
		}

		List<Filter> filters = null;
		try {
			filters = filters(Arrays.asList(_class.getAnnotation(Station.class).filters()));
		} catch (Exception e) {
			throw new RadioStationBuildException(e);
		}

		Object station = null;
		try {
			station = station(_class);
		} catch (Exception e) {
			throw new RadioStationBuildException(e);
		}

		Map<String, Listening> listenings = new HashMap<String, Listening>();

		Class<?> c = _class;
		while (Enhancer.isEnhanced(c)) {
			c = c.getSuperclass();
		}

		for (Method method : c.getMethods()) {
			if (!method.isAnnotationPresent(Channel.class)) {
				continue;
			}

			Channel channel = method.getAnnotation(Channel.class);

			String frequency = channel.frequency();
			Broadcaster broadcaster = channel.broadcaster();

			List<String> names = new ArrayList<String>();
			names.add(channel.result().value().equals("") ? null : channel.result().value());
			names.add(channel.exception().value().equals("") ? null : channel.exception().value());

			Annotation[][] annotations = method.getParameterAnnotations();
			for (int i = 0; i < annotations.length; i++) {
				for (int j = 0; j < annotations[i].length; j++) {
					if (annotations[i][j] instanceof Message) {
						Message message = (Message) annotations[i][j];
						if (names.contains(message.value())) {
							throw new RadioStationBuildException("can not mappered a same value[" + message.value() + "] to different messages in channel[" + channel.frequency() + "] of method[" + method + "]");
						}
						names.add(message.value());
						break;
					}
				}
			}

			if (listenings.containsKey(frequency)) {
				break;
			}
			
			Set<Receiver> receivers = null;
			try {
				receivers = receivers(frequency);
			} catch (Exception e) {
				throw new RadioStationBuildException(e);
			}
			Listening listening = new Listening(frequency, broadcaster, receivers != null ? receivers : new HashSet<Receiver>());
			listenings.put(frequency, listening);
		}

		Object object = Enhancer.create(c, new Modem(station, filters, listenings));

		cache.put(_class, object);

		return object;
	}

	protected abstract Object station(Class<?> _class) throws Exception;

	protected abstract Set<Receiver> receivers(String frequency) throws Exception;

	protected abstract List<Filter> filters(List<Class<? extends Filter>> classes) throws Exception;

}
