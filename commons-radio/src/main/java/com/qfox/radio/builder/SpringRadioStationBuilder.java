package com.qfox.radio.builder;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.qfox.radio.annotation.Listener;
import com.qfox.radio.annotation.Radiogram;
import com.qfox.radio.annotation.Station;
import com.qfox.radio.core.Filter;
import com.qfox.radio.core.Receiver;

/**
 * spring 广播基站建造者,用于将广播基站架设在spring的容器之中,让无论是广播者还是收听者都让spring来管理
 * 
 * @author Payne
 * 
 */
public class SpringRadioStationBuilder extends AbstractRadioStationBuilder implements RadioStationBuilder, BeanPostProcessor, ApplicationContextAware {
	private ApplicationContext applicationContext;

	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean.getClass().isAnnotationPresent(Station.class)) {
			return build(bean.getClass());
		} else {
			return bean;
		}
	}

	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	protected Object station(Class<?> _class) throws Exception {
		return applicationContext.getBean(_class);
	}

	@Override
	protected Set<Receiver> receivers(String frequency) throws Exception {
		Map<String, Object> radiograms = applicationContext.getBeansWithAnnotation(Radiogram.class);
		Set<Receiver> receivers = new HashSet<Receiver>();
		for (Object object : radiograms.values()) {
			for (Method method : object.getClass().getMethods()) {
				if (!method.isAnnotationPresent(Listener.class)) {
					continue;
				}
				Listener listener = method.getAnnotation(Listener.class);
				if (listener.frequency().equals(frequency)) {
					receivers.add(new Receiver(method, object));
				}
			}
		}
		return receivers;
	}

	@Override
	protected List<Filter> filters(List<Class<? extends Filter>> classes) throws Exception {
		Map<String, Filter> filters = applicationContext.getBeansOfType(Filter.class);
		return new ArrayList<Filter>(filters.values());
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
