/**
 * utils[com.change.utils.transformer]
 * Change
 * 2014年1月9日 下午11:09:09
 */

package com.qfox.commons.xml2bean;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.dom4j.Element;

/**
 * 
 * 
 * @Description: 简单类型变形器
 * 
 * 
 * @author Change
 * @date 2014年1月9日 下午11:09:09
 * 
 *       修订记录<br/>
 *       姓名:Change<br/>
 *       时间:2014年1月9日 下午11:09:09<br/>
 *       内容:初始<br/>
 * 
 */
public class SimpleTransformer implements Transformer {

	private static List<Handler> handlers = new ArrayList<Handler>();

	static {
		handlers.add(new ByteHandler());
		handlers.add(new CharacterHandler());
		handlers.add(new ShortHandler());
		handlers.add(new IntegerHandler());
		handlers.add(new FloatHandler());
		handlers.add(new LongHandler());
		handlers.add(new DoubleHandler());
		handlers.add(new BooleanHandler());
		handlers.add(new StringHandler());
		handlers.add(new DateHandler());
		handlers.add(new EnumHandler());
		handlers.add(new ClassHandler());
	}

	public String tag() {
		return "simple";
	}

	public Object transform(Element element, Class<?> _class, TransformerContext context) throws Exception {
		String text = element.getText();
		for (Handler handler : handlers) {
			if (handler.support(_class)) {
				return handler.handle(text, _class);
			}
		}
		throw new RuntimeException("class do not match simple element");
	}

	public Object transform(Element element, ParameterizedType type, TransformerContext context) throws Exception {
		return transform(element, Class.class.cast(type.getRawType()), context);
	}

	public Object transform(Element element, GenericArrayType type, TransformerContext context) throws Exception {
		return null;
	}

	public boolean support(Object object) {
		for (Handler handler : handlers) {
			if (handler.support(object.getClass())) {
				return true;
			}
		}
		return false;
	}

	public Element transform(Object object, Element parent, TransformerContext context) throws Exception {
		for (Handler handler : handlers) {
			if (handler.support(object.getClass())) {
				Element element = parent.addElement("simple");
				element.addText(handler.handle(object));
				return element;
			}
		}
		throw new RuntimeException("object class do not match simple element");
	}

	private interface Handler {

		boolean support(Class<?> _class);

		Object handle(String text, Class<?> _class) throws Exception;

		String handle(Object object) throws Exception;

	}

	private static class ByteHandler implements Handler {

		public boolean support(Class<?> _class) {
			return _class == byte.class || _class == Byte.class;
		}

		public Object handle(String text, Class<?> _class) throws Exception {
			return Byte.parseByte(text);
		}

		public String handle(Object object) throws Exception {
			return object.toString();
		}

	}

	private static class CharacterHandler implements Handler {

		public boolean support(Class<?> _class) {
			return _class == char.class || _class == Character.class;
		}

		public Object handle(String text, Class<?> _class) throws Exception {
			if (text.length() != 1) {
				throw new RuntimeException("can not transform " + text + " to a character");
			}
			return text.charAt(0);
		}

		public String handle(Object object) throws Exception {
			return object.toString();
		}

	}

	private static class ShortHandler implements Handler {

		public boolean support(Class<?> _class) {
			return _class == short.class || _class == Short.class;
		}

		public Object handle(String text, Class<?> _class) throws Exception {
			return Short.parseShort(text);
		}

		public String handle(Object object) throws Exception {
			return object.toString();
		}

	}

	private static class IntegerHandler implements Handler {

		public boolean support(Class<?> _class) {
			return _class == int.class || _class == Integer.class;
		}

		public Object handle(String text, Class<?> _class) throws Exception {
			return Integer.parseInt(text);
		}

		public String handle(Object object) throws Exception {
			return object.toString();
		}

	}

	private static class FloatHandler implements Handler {

		public boolean support(Class<?> _class) {
			return _class == float.class || _class == Float.class;
		}

		public Object handle(String text, Class<?> _class) throws Exception {
			return Float.parseFloat(text);
		}

		public String handle(Object object) throws Exception {
			return object.toString();
		}

	}

	private static class LongHandler implements Handler {

		public boolean support(Class<?> _class) {
			return _class == long.class || _class == Long.class;
		}

		public Object handle(String text, Class<?> _class) throws Exception {
			return Long.parseLong(text);
		}

		public String handle(Object object) throws Exception {
			return object.toString();
		}

	}

	private static class DoubleHandler implements Handler {

		public boolean support(Class<?> _class) {
			return _class == double.class || _class == Double.class;
		}

		public Object handle(String text, Class<?> _class) throws Exception {
			return Double.parseDouble(text);
		}

		public String handle(Object object) throws Exception {
			return object.toString();
		}

	}

	private static class BooleanHandler implements Handler {

		public boolean support(Class<?> _class) {
			return _class == boolean.class || _class == Boolean.class;
		}

		public Object handle(String text, Class<?> _class) throws Exception {
			return Boolean.valueOf(text);
		}

		public String handle(Object object) throws Exception {
			return object.toString();
		}

	}

	private static class StringHandler implements Handler {

		public boolean support(Class<?> _class) {
			return _class == String.class;
		}

		public Object handle(String text, Class<?> _class) throws Exception {
			return text;
		}

		public String handle(Object object) throws Exception {
			return object.toString();
		}

	}

	private static class DateHandler implements Handler {

		private DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		public boolean support(Class<?> _class) {
			return _class == Date.class;
		}

		public Object handle(String text, Class<?> _class) throws Exception {
			return format.parse(text);
		}

		public String handle(Object object) throws Exception {
			return format.format(Date.class.cast(object));
		}

	}

	private static class EnumHandler implements Handler {

		public boolean support(Class<?> _class) {
			return _class.isEnum();
		}

		public Object handle(String text, Class<?> _class) throws Exception {
			return Enum.class.cast(_class.getMethod("valueOf", String.class).invoke(null, text));
		}

		public String handle(Object object) throws Exception {
			return Enum.class.cast(object).name();
		}

	}

	private static class ClassHandler implements Handler {

		public boolean support(Class<?> _class) {
			return _class == Class.class;
		}

		public Object handle(String text, Class<?> _class) throws Exception {
			return Class.forName(text);
		}

		public String handle(Object object) throws Exception {
			return Class.class.cast(object).getName();
		}

	}

}
