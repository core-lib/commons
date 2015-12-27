package com.qfox.radio.common;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 反射工具类
 * 
 * @author Change
 * 
 */
public class Reflector {

	/**
	 * 得到指定注解的所有fields 包括所有超类的
	 * 
	 * @param _class
	 *            类
	 * @param annotation
	 *            注解类型
	 * @return 本身类以及所有超类的标注了该注解的所有fields
	 */
	public static List<Field> getAnnotatedFields(Class<?> _class, Class<? extends Annotation> annotation) {
		List<Field> result = new ArrayList<Field>();

		while (_class != Object.class) {
			Field[] fields = _class.getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				if (field.isAnnotationPresent(annotation)) {
					result.add(field);
				}
			}
			_class = _class.getSuperclass();
		}

		return result;
	}

	public static Set<Field> getAllDeclaredFields(Class<?> _class) {
		Set<Field> fields = new HashSet<Field>();

		while (_class != Object.class) {
			fields.addAll(Arrays.asList(_class.getDeclaredFields()));
			_class = _class.getSuperclass();
		}

		return fields;
	}

	/**
	 * 得到指定注解的所有setters 包括所有超类的
	 * 
	 * @param _class
	 *            类
	 * @param annotation
	 *            注解类型
	 * @return 本身类以及所有超类的标注了该注解的所有setters
	 */
	public static List<Method> getAnnotatedSetters(Class<?> _class, Class<? extends Annotation> annotation) {
		List<Method> result = new ArrayList<Method>();

		try {
			while (_class != Object.class) {
				PropertyDescriptor[] descriptors = Introspector.getBeanInfo(_class).getPropertyDescriptors();
				for (PropertyDescriptor descriptor : descriptors) {
					if (descriptor.getName().equals("class")) {
						continue;
					}
					Method setter = descriptor.getWriteMethod();
					if (setter != null && setter.isAnnotationPresent(annotation)) {
						result.add(setter);
					}
				}
				_class = _class.getSuperclass();
			}
		} catch (IntrospectionException e) {
			throw new RuntimeException(e);
		}

		return result;
	}

}
