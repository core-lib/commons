/**
 * utils[com.change.utils.transformer]
 * Change
 * 2014年1月10日 上午10:47:53
 */

package com.qfox.commons.xml2bean;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.dom4j.Element;

import com.googlecode.openbeans.IntrospectionException;
import com.googlecode.openbeans.Introspector;
import com.googlecode.openbeans.PropertyDescriptor;
import com.qfox.commons.xml2bean.reflection.ParameterizedTypeImpl;

/**
 * 
 * 
 * @Description:复合类型变形器
 * 
 * 
 * @author Change
 * @date 2014年1月10日 上午10:47:53
 * 
 *       修订记录<br/>
 *       姓名:Change<br/>
 *       时间:2014年1月10日 上午10:47:53<br/>
 *       内容:初始<br/>
 * 
 */
public class ComplexTransformer implements Transformer {

	private List<SpecialObjectTransformer<?>> specialObjectTransformers = new ArrayList<ComplexTransformer.SpecialObjectTransformer<?>>();

	{
		specialObjectTransformers.add(new FileTransformer());
	}

	public String tag() {
		return "complex";
	}

	public Object transform(Element element, Class<?> _class, TransformerContext context) throws Exception {
		for (SpecialObjectTransformer<?> specialObjectTransformer : specialObjectTransformers) {
			if (specialObjectTransformer.support(_class)) {
				return specialObjectTransformer.transform(element, context);
			}
		}
		return fill(element, _class, context);
	}

	public Object transform(Element element, ParameterizedType type, TransformerContext context) throws Exception {
		Class<?> _class = Class.class.cast(type.getRawType());

		for (SpecialObjectTransformer<?> specialObjectTransformer : specialObjectTransformers) {
			if (specialObjectTransformer.support(_class)) {
				return specialObjectTransformer.transform(element, context);
			}
		}

		return fill(element, type, context);
	}

	private static Object fill(Element element, Type type, TransformerContext context) throws InstantiationException, IllegalAccessException, IntrospectionException, InvocationTargetException, Exception {
		Class<?> _class = null;

		// 将 TypeVariable 映射到 实际参数类型
		Map<TypeVariable<?>, Type> map = new HashMap<TypeVariable<?>, Type>();

		if (type instanceof Class<?>) {
			_class = Class.class.cast(type);
		} else if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = ParameterizedType.class.cast(type);
			_class = Class.class.cast(parameterizedType.getRawType());
			TypeVariable<?>[] variables = _class.getTypeParameters();
			Type[] types = parameterizedType.getActualTypeArguments();
			for (int i = 0; i < variables.length; i++) {
				map.put(variables[i], types[i]);
			}
		} else {
			throw new RuntimeException("can not transform element : " + element + " to an object of type : " + type);
		}

		Object object = _class.newInstance();
		List<?> elements = element.elements();
		for (Object _element : elements) {
			Element __element = Element.class.cast(_element);
			String property = __element.attributeValue("property");
			PropertyDescriptor descriptor = new PropertyDescriptor(property, _class);
			descriptor.getWriteMethod().invoke(object, context.toObject(__element, replace(descriptor.getReadMethod().getGenericReturnType(), map)));
		}
		return object;
	}

	public Object transform(Element element, GenericArrayType type, TransformerContext context) throws Exception {
		return null;
	}

	public boolean support(Object object) {
		return true;
	}

	public Element transform(Object object, Element parent, TransformerContext context) throws Exception {
		for (SpecialObjectTransformer<?> specialObjectTransformer : specialObjectTransformers) {
			if (specialObjectTransformer.support(object.getClass())) {
				return specialObjectTransformer.transform(object, parent, context);
			}
		}
		Element element = parent.addElement("complex");
		PropertyDescriptor[] descriptors = Introspector.getBeanInfo(object.getClass()).getPropertyDescriptors();
		for (PropertyDescriptor descriptor : descriptors) {
			if (descriptor.getName().equals("class")) {
				continue;
			}
			Element _element = context.toElement(descriptor.getReadMethod().invoke(object), element);
			_element.addAttribute("property", descriptor.getName());
		}
		return element;
	}

	/**
	 * 特殊对象转换器
	 * 
	 * @author Change
	 * 
	 */
	public static interface SpecialObjectTransformer<T> {

		boolean support(Class<?> _class);

		T transform(Element element, TransformerContext context) throws Exception;

		Element transform(Object object, Element parent, TransformerContext context) throws Exception;

	}

	public static class FileTransformer implements SpecialObjectTransformer<File> {

		public boolean support(Class<?> _class) {
			return File.class.isAssignableFrom(_class);
		}

		public File transform(Element element, TransformerContext context) throws Exception {
			FileWrapper wrapper = FileWrapper.class.cast(fill(element, FileWrapper.class, context));
			String path = System.getProperty("user.dir") + File.separator + "temp" + File.separator + UUID.randomUUID() + File.separator + wrapper.getName();
			File file = new File(path);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			OutputStream fileOutputStream = null;
			BufferedOutputStream bufferedOutputStream = null;
			try {
				fileOutputStream = new FileOutputStream(file);
				bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
				String[] _bytes = wrapper.getContent().split(" ");
				byte[] bytes = new byte[_bytes.length];
				for (int i = 0; i < _bytes.length; i++) {
					bytes[i] = Byte.parseByte(_bytes[i]);
				}
				bufferedOutputStream.write(bytes);
				return file;
			} catch (Exception e) {
				throw e;
			} finally {
				if (bufferedOutputStream != null) {
					bufferedOutputStream.close();
				}
				if (fileOutputStream != null) {
					fileOutputStream.close();
				}
			}
		}

		public Element transform(Object object, Element parent, TransformerContext context) throws Exception {
			File file = File.class.cast(object);
			FileWrapper wrapper = new FileWrapper();
			wrapper.setName(file.getName());
			FileInputStream fileInputStream = null;
			BufferedInputStream bufferedInputStream = null;
			byte[] bytes = null;
			try {
				fileInputStream = new FileInputStream(file);
				bufferedInputStream = new BufferedInputStream(fileInputStream);
				bytes = new byte[Long.valueOf(file.length()).intValue()];
				bufferedInputStream.read(bytes);
				StringBuilder builder = new StringBuilder();
				for (byte b : bytes) {
					builder.append(b).append(" ");
				}
				wrapper.setContent(builder.toString());
			} catch (Exception e) {
				throw e;
			} finally {
				if (fileInputStream != null) {
					fileInputStream.close();
				}
				if (bufferedInputStream != null) {
					bufferedInputStream.close();
				}
			}

			return context.toElement(wrapper, parent);
		}

		public static class FileWrapper {
			private String name;
			private String content;

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public String getContent() {
				return content;
			}

			public void setContent(String content) {
				this.content = content;
			}

		}

	}

	private static Type replace(Type type, Map<TypeVariable<?>, Type> map) {
		Type actual = null;
		// 普通类型
		if (type instanceof Class<?>) {
			actual = type;
		}
		// T;
		else if (type instanceof TypeVariable<?>) {
			actual = map.get(type);
		}
		// T[]
		else if (type instanceof GenericArrayType) {
			Type temp = type;
			int dimension = 0;
			while (temp instanceof GenericArrayType) {
				temp = GenericArrayType.class.cast(temp).getGenericComponentType();
				dimension = dimension + 1;
			}
			actual = Array.newInstance(Class.class.cast(map.get(temp)), new int[dimension]).getClass();
		}
		// AnyType<T> || AnyType<T[]>
		else if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = ParameterizedType.class.cast(type);
			Type[] typeArguments = parameterizedType.getActualTypeArguments();
			final Type rawType = parameterizedType.getRawType();
			final Type ownerType = parameterizedType.getOwnerType();
			final Type[] actualTypeArguments = new Type[typeArguments.length];
			for (int i = 0; i < typeArguments.length; i++) {
				actualTypeArguments[i] = replace(typeArguments[i], map);
			}
			actual = new ParameterizedTypeImpl(rawType, ownerType, actualTypeArguments);
		}
		return actual;
	}

}
