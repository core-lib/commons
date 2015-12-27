package com.qfox.commons.converter;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 
 * 
 * @Description:简单类型转换器,实现对八种基本类型和String类型的转换
 * @author Change
 * @date 2014年1月9日
 * 
 *       修订记录<br/>
 *       姓名:Change<br/>
 *       时间:2014年1月9日<br/>
 *       内容:初始<br/>
 * 
 */
public class SimpleConverter implements Converter {

	private static Map<Class<?>, Class<?>> map = new HashMap<Class<?>, Class<?>>();

	static {
		map.put(byte.class, Byte.class);
		map.put(char.class, Character.class);
		map.put(short.class, Short.class);
		map.put(int.class, Integer.class);
		map.put(float.class, Float.class);
		map.put(long.class, Long.class);
		map.put(double.class, Double.class);
		map.put(boolean.class, Boolean.class);
		map.put(String.class, String.class);
	}

	public boolean support(Type type) {
		return map.containsKey(type) || map.containsValue(type);
	}

	public Object convert(Object source, String name, Class<?> _class, TypeConverter context) throws Exception {
		if (source == null) {
			return null;
		}
		if (map.containsKey(_class)) {
			_class = map.get(_class);
		}
		String value = String.valueOf(source);
		if (_class == Character.class) {
			if (value.length() != 1) {
				throw new IllegalStateException("can not convert value '" + value + "' to " + _class);
			}
			return value.charAt(0);
		}
		Method method = _class.getMethod("valueOf", String.class);
		return method.invoke(_class, value);
	}

	public Object convert(Object source, String name, ParameterizedType type, TypeConverter context) throws Exception {
		return null;
	}

	public Object convert(Object source, String name, GenericArrayType type, TypeConverter context) throws Exception {
		return null;
	}

}
