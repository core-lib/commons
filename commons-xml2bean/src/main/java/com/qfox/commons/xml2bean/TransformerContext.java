/**
 * utils[com.change.utils.transformer]
 * Change
 * 2014年1月9日 下午11:09:09
 */

package com.qfox.commons.xml2bean;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.qfox.commons.xml2bean.reflection.GenericArrayTypeImpl;
import com.qfox.commons.xml2bean.reflection.ParameterizedTypeImpl;

/**
 * 
 * 
 * @Description: 变形器容器,该变形器设计用于将XML变换为一个java对象而设计,实现递归式转换,所用的标签有:<br/>
 *               simple,array,collection,map[entry],complex 无大标签和entry这个只在在map标签中使用的小标签<br/>
 *               1.该变形器能实现泛型类的转换,由于xml的限制 不能采用<>作为泛型参数的传入, 所以采用()代替 如HashMap<String,Integer> 则可以使用 HashMap(String,Integer)代替<br/>
 *               2.所有type属性均不能包含接口或抽象类,因为无法实例化<br/>
 *               3.可以通过context标签的aliases属性添加别名,如HashMap=java.util.HashMap<br/>
 *               4.对于属性的引用类型为接口或抽象类,需要用type属性指定实现类的类型<br/>
 *               5.父标签中指定的type或子标签的property能确定子标签的类型的情况下(子标签无需再指定type属性, 除接口和抽象类以外)
 * 
 * 
 * @author Change
 * @date 2014年1月9日 下午9:32:14
 * 
 *       修订记录<br/>
 *       姓名:Change<br/>
 *       时间:2014年1月9日 下午9:32:14<br/>
 *       内容:初始<br/>
 * 
 */
public class TransformerContext {

	private static SAXReader reader = new SAXReader();

	private static Pattern pattern = Pattern.compile("(\\w+([.$]\\w+)*)((\\((.+)\\))?)((\\[\\])*)");

	private static Map<String, Class<?>> primitives = new HashMap<String, Class<?>>();

	private static Map<Class<?>, Class<?>> wrappers = new HashMap<Class<?>, Class<?>>();

	private static List<Transformer> transformers = new ArrayList<Transformer>();

	static {
		// 基本类型别名
		primitives.put("byte", byte.class);
		primitives.put("char", char.class);
		primitives.put("short", short.class);
		primitives.put("int", int.class);
		primitives.put("float", float.class);
		primitives.put("long", long.class);
		primitives.put("double", double.class);
		primitives.put("boolean", boolean.class);

		// 封装类型
		wrappers.put(Byte.class, byte.class);
		wrappers.put(Character.class, char.class);
		wrappers.put(Short.class, short.class);
		wrappers.put(Integer.class, int.class);
		wrappers.put(Float.class, float.class);
		wrappers.put(Long.class, long.class);
		wrappers.put(Double.class, double.class);
		wrappers.put(Boolean.class, boolean.class);

		// 变形器
		transformers.add(new SimpleTransformer());
		transformers.add(new ArrayTransformer());
		transformers.add(new CollectionTransformer());
		transformers.add(new MapTransformer());
		transformers.add(new ComplexTransformer());
	}

	private Map<String, Class<?>> aliases = new HashMap<String, Class<?>>();

	{
		// 默认别名
		aliases.put("Byte", Byte.class);
		aliases.put("Character", Character.class);
		aliases.put("Short", Short.class);
		aliases.put("Integer", Integer.class);
		aliases.put("Float", Float.class);
		aliases.put("Long", Long.class);
		aliases.put("Double", Double.class);
		aliases.put("Boolean", Boolean.class);
		aliases.put("String", String.class);
		aliases.put("Object", Object.class);
		aliases.put("Date", Date.class);
		aliases.put("Class", Class.class);
	}

	private TransformerContext() {
	}

	public static <K, V> Map<K, V> transform(String path, Class<K> k, Class<V> v) throws Exception {
		Map<?, ?> temp = (Map<?, ?>) transform(path);
		Map<K, V> map = new HashMap<K, V>();
		for (Entry<?, ?> entry : temp.entrySet()) {
			map.put(k.cast(entry.getKey()), v.cast(entry.getValue()));
		}
		return map;
	}

	public static <E> List<E> transform(String path, Class<E> e) throws Exception {
		List<?> temp = (List<?>) transform(path);
		List<E> list = new ArrayList<E>();
		for (Object object : temp) {
			list.add(e.cast(object));
		}
		return list;
	}

	public static Object transform(String path) throws Exception {
		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
		Document document = reader.read(in);
		return transform(document);
	}

	public static Object transform(Document document) throws Exception {
		TransformerContext context = new TransformerContext();

		Element root = document.getRootElement();
		// 如果不是 context 标签
		if (!root.getName().equals("context")) {
			throw new RuntimeException("root element must be \"context\"");
		}

		// 处理别名
		String aliases = root.attributeValue("aliases") == null ? null : root.attributeValue("aliases").replaceAll("[\\s\\t\\r\\n]", "");
		if (aliases != null && !aliases.equals("")) {
			String[] _aliases = aliases.split(",");
			for (String alias : _aliases) {
				String[] pair = alias.split("=");
				if (pair.length != 2) {
					throw new RuntimeException("grammar error[" + alias + "] please use \"alias=classFullName\" to map and \",\" to separate them");
				}
				try {
					context.aliases.put(pair[0], Class.forName(pair[1]));
				} catch (ClassNotFoundException e) {
					throw new RuntimeException(e);
				}
			}
		}

		List<?> elements = root.elements();
		// 没有子标签
		if (elements.isEmpty()) {
			return null;
		}
		// 一个子标签 返回一个对象
		else if (elements.size() == 1) {
			Element element = Element.class.cast(elements.get(0));
			return context.toObject(element, null);
		}
		// 多个子标签 返回一个数组
		else {
			Object[] objects = new Object[elements.size()];
			for (int i = 0; i < elements.size(); i++) {
				Element element = Element.class.cast(elements.get(i));
				objects[i] = context.toObject(element, null);
			}
			return objects;
		}
	}

	public static Document transform(Object object) throws Exception {
		Document document = DocumentFactory.getInstance().createDocument();
		Element element = document.addElement("context");
		new TransformerContext().toElement(object, element);
		return document;
	}

	public Object toObject(Element element, Type type) throws Exception {
		if (element.attributeValue("type") != null) {
			Type _type = convert(element.attributeValue("type"));
			if (type != null && !validate(type, _type)) {
				throw new RuntimeException("can not make " + type + " reference to an instance of type " + _type + "[" + element.asXML() + "]");
			}
			type = _type;
		}
		if (element.getName().equals("null")) {
			return null;
		}
		for (Transformer transformer : transformers) {
			if (element.getName().equals(transformer.tag())) {
				if (type instanceof Class<?>) {
					return transformer.transform(element, Class.class.cast(type), this);
				}
				if (type instanceof ParameterizedType) {
					return transformer.transform(element, ParameterizedType.class.cast(type), this);
				}
				if (type instanceof GenericArrayType) {
					return transformer.transform(element, GenericArrayType.class.cast(type), this);
				}
			}
		}
		throw new RuntimeException("unhandle element [" + element.asXML() + "]");
	}

	public Element toElement(Object object, Element parent) throws Exception {
		if (object == null) {
			return parent.addElement("null");
		}
		for (Transformer transformer : transformers) {
			if (transformer.support(object)) {
				Element element = transformer.transform(object, parent, this);
				if (object != null) {
					String value = "";
					Class<?> _class = object.getClass();
					while (_class.isArray()) {
						value += "[]";
						_class = _class.getComponentType();
					}
					value = _class.getName() + value;
					element.addAttribute("type", value);
				}
				return element;
			}
		}
		throw new RuntimeException("unhandle object [" + object.toString() + "]");
	}

	private Type convert(String value) throws Exception {
		Matcher matcher = pattern.matcher(value);

		if (!matcher.matches()) {
			throw new RuntimeException("can not convert \"" + value + "\" to a type");
		}

		String _class = matcher.group(1);
		String _argument = matcher.group(4);
		String _arguments = matcher.group(5);
		String _array = matcher.group(6);

		Type type = null;
		if (primitives.containsKey(_class)) {
			type = primitives.get(_class);
		} else if (aliases.containsKey(_class)) {
			type = aliases.get(_class);
		} else {
			type = Class.forName(_class);
		}

		Class<?> __class = Class.class.cast(type);

		int typeParametersLength = __class.getTypeParameters().length;
		// 如果不是泛型类 但是有泛型参数...
		if (typeParametersLength == 0 && _argument != null) {
			throw new RuntimeException(type + " did not have type parameters [" + value + "]");
		}

		// 泛型
		if (_arguments != null) {
			String[] arguments = _arguments.split(",");

			// 类型参数个数不匹配
			if (typeParametersLength != arguments.length) {
				throw new RuntimeException(type + " expect " + typeParametersLength + " type parameters but it's " + arguments.length + " actually [" + value + "]");
			}

			final Type[] types = new Type[arguments.length];
			for (int i = 0; i < arguments.length; i++) {
				Type parameter = convert(arguments[i]);
				// 基本类型不能作为类型参数
				if (parameter instanceof Class<?> && Class.class.cast(parameter).isPrimitive()) {
					throw new RuntimeException("type parameter can not be primitive [" + value + "]");
				}
				types[i] = parameter;
			}
			type = new ParameterizedTypeImpl(type, null, types);
		}

		// 数组
		if (_array != null && _array.length() > 0) {
			int dimension = _array.length() / 2;
			if (type instanceof Class<?>) {
				type = Array.newInstance(__class, new int[dimension]).getClass();
			} else if (type instanceof ParameterizedType) {
				while (dimension != 0) {
					type = new GenericArrayTypeImpl(type);
					dimension -= 1;
				}
			}
		}

		return type;
	}

	private boolean validate(Type reference, Type instance) throws Exception {
		if (reference instanceof Class<?>) {
			Class<?> _reference = Class.class.cast(reference);
			if (instance instanceof Class<?>) {
				Class<?> _instance = Class.class.cast(instance);
				return _reference.isAssignableFrom(_instance) || wrappers.get(instance) == _reference;
			} else if (instance instanceof ParameterizedType) {
				Class<?> _instance = Class.class.cast(ParameterizedType.class.cast(instance).getRawType());
				return _reference.isAssignableFrom(_instance);
			} else if (instance instanceof GenericArrayType) {
				return validate(_reference.getComponentType(), GenericArrayType.class.cast(instance).getGenericComponentType());
			}
		} else if (reference instanceof ParameterizedType) {
			ParameterizedType type = ParameterizedType.class.cast(reference);
			Class<?> _reference = Class.class.cast(type.getRawType());
			if (instance instanceof Class<?>) {
				Class<?> _instance = Class.class.cast(instance);
				return _reference.isAssignableFrom(_instance);
			} else if (instance instanceof ParameterizedType) {
				ParameterizedType _type = ParameterizedType.class.cast(instance);
				Class<?> _instance = Class.class.cast(_type.getRawType());
				return _reference.isAssignableFrom(_instance) && Arrays.equals(type.getActualTypeArguments(), _type.getActualTypeArguments());
			}
		} else if (reference instanceof GenericArrayType) {
			GenericArrayType _reference = GenericArrayType.class.cast(reference);
			if (instance instanceof Class<?>) {
				Class<?> _instance = Class.class.cast(instance);
				return validate(_reference.getGenericComponentType(), _instance.getComponentType());
			} else if (instance instanceof GenericArrayType) {
				return validate(_reference.getGenericComponentType(), GenericArrayType.class.cast(instance).getGenericComponentType());
			}
		}
		return false;
	}

}
