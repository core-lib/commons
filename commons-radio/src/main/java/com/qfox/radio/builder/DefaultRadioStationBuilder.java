package com.qfox.radio.builder;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.qfox.radio.annotation.Listener;
import com.qfox.radio.annotation.Radiogram;
import com.qfox.radio.common.Scaner;
import com.qfox.radio.core.Filter;
import com.qfox.radio.core.Receiver;

/**
 * 缺省的信号广播基站建造者
 * 
 * @author Payne
 * 
 */
public class DefaultRadioStationBuilder extends AbstractRadioStationBuilder implements RadioStationBuilder {

	private Map<String, Set<Receiver>> receivers = new HashMap<String, Set<Receiver>>();

	public DefaultRadioStationBuilder() throws Exception {
		Set<Class<?>> classes = Scaner.scan("", true);

		for (Class<?> clazz : classes) {
			if (!clazz.isAnnotationPresent(Radiogram.class)) {
				continue;
			}

			Object object = clazz.newInstance();
			for (Method method : clazz.getMethods()) {
				if (!method.isAnnotationPresent(Listener.class)) {
					continue;
				}
				Receiver receiver = new Receiver(method, object);
				if (!receivers.containsKey(receiver.frequency)) {
					receivers.put(receiver.frequency, new HashSet<Receiver>());
				}
				receivers.get(receiver.frequency).add(receiver);
			}
		}
	}

	@Override
	protected Object station(Class<?> _class) throws Exception {
		return _class.newInstance();
	}

	@Override
	protected Set<Receiver> receivers(String frequency) throws Exception {
		return receivers.get(frequency);
	}

	@Override
	protected List<Filter> filters(List<Class<? extends Filter>> classes) throws Exception {
		List<Filter> filters = new ArrayList<Filter>();
		for (Class<? extends Filter> _class : classes) {
			filters.add(_class.newInstance());
		}
		return filters;
	}
}
