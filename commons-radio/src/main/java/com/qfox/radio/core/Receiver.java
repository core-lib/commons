package com.qfox.radio.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qfox.radio.annotation.Listener;
import com.qfox.radio.annotation.Message;
import com.qfox.radio.exception.RadioStationBuildException;

/**
 * 对收听者的封装
 * 
 * @author Payne
 * 
 */
public final class Receiver {
	private static final Map<Class<?>, Object> primaries = new HashMap<Class<?>, Object>();

	static {
		primaries.put(byte.class, 0);
		primaries.put(boolean.class, false);
		primaries.put(char.class, 0);
		primaries.put(short.class, 0);
		primaries.put(int.class, 0);
		primaries.put(long.class, 0l);
		primaries.put(float.class, 0f);
		primaries.put(double.class, 0d);
	}
	
	public final String frequency;
	public final int intensity;
	private Method listener;
	private Object radiogram;

	public Receiver(Method method, Object object) throws RadioStationBuildException {
		super();

		Listener listener = method.getAnnotation(Listener.class);
		if (listener.intensity() < 1 || listener.intensity() > 10) {
			throw new RadioStationBuildException("signal intensity must be between 1 and 10");
		}

		this.listener = method;
		this.radiogram = object;

		frequency = method.getAnnotation(Listener.class).frequency();
		intensity = method.getAnnotation(Listener.class).intensity();
	}

	public void receive(Signal signal) {
		List<Object> arguments = new ArrayList<Object>();
		Annotation[][] annotations = listener.getParameterAnnotations();
		flag: for (int i = 0; i < annotations.length; i++) {
			for (Annotation annotation : annotations[i]) {
				if (annotation instanceof Message) {
					Message message = (Message) annotation;

					Object argument = signal.getMessages().get(message.value());
					Class<?> type = listener.getParameterTypes()[i];
					if (argument != null && !primaries.containsKey(type) && !type.isInstance(argument)) {
						throw new ClassCastException("can not cast argument of key:[" + message.value() + "] from type:[" + argument.getClass() + "] to type:[" + type + "] in frequency:[" + frequency + "]");
					}

					if (argument == null && primaries.containsKey(type)) {
						argument = primaries.get(type);
					}

					arguments.add(argument);
					continue flag;
				}
			}
			if (listener.getParameterTypes()[i] == Signal.class) {
				arguments.add(signal);
			} else {
				arguments.add(null);
			}
		}
		try {
			listener.invoke(radiogram, arguments.toArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Method getListener() {
		return listener;
	}

	public void setListener(Method listener) {
		this.listener = listener;
	}

	public Object getRadiogram() {
		return radiogram;
	}

	public void setRadiogram(Object radiogram) {
		this.radiogram = radiogram;
	}

}
