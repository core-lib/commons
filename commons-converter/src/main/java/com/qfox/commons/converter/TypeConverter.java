package com.qfox.commons.converter;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.googlecode.openbeans.IntrospectionException;
import com.googlecode.openbeans.Introspector;
import com.googlecode.openbeans.PropertyDescriptor;
import com.qfox.commons.converter.reflection.ParameterizedTypeImpl;

/**
 * 
 * 
 * 
 * @Description: 转换器上下文,主要为了解开转换器与转换器之间的耦合,让每个转换器只关心自己能力范围内的工作,其他交给上下文去调度<br/>
 *               该转换器设计是为了简便和统一从 Domain 转换成 DTO 之间的过程,提供一个简单透明的转换方法,以下是使用该转换器的约束<br/>
 *               1.需要转换的属性名必须一致<br/>
 *               2.如果类型是简单类型或简单类型的数组即八大基本类型和String类型及其数组,那么类型必须一致<br/>
 *               3.集合/Map类型提倡面向接口,而且实现类必须提供只有一个Collection.class/Map.class参数的构造器<br/>
 *               4.泛型参数是简单类型需要一致,如果为普通类型可以一致或相似<br/>
 *               5.普通类型的属性或泛型参数不可以为抽象类或接口而且必须提供无参构造方法<br/>
 * @author Change
 * @date 2014年1月7日
 * 
 *       修订记录<br/>
 *       姓名:Change<br/>
 *       时间:2014年1月7日<br/>
 *       内容:初始<br/>
 * 
 */
public class TypeConverter {
	private List<Converter> converters = new ArrayList<Converter>();

	{
		converters.add(new SimpleConverter());
		converters.add(new EnumConverter());
		converters.add(new ArrayConverter());
		converters.add(new CollectionConverter());
		converters.add(new MapConverter());
		converters.add(new DateConverter());
		converters.add(new ComplexConverter());
	}

	private Set<String> excludes = new HashSet<String>();

	public TypeConverter() {
	}

	public TypeConverter(String... excludes) {
		this.excludes.addAll(excludes == null ? new HashSet<String>() : Arrays.asList(excludes));
	}

	public TypeConverter(Collection<String> excludes) {
		this.excludes.addAll(excludes == null ? new HashSet<String>() : excludes);
	}

	Object doConvert(Object source, String name, Type type) throws Exception {
		if (name == null) {
			throw new IllegalArgumentException("name can not be null");
		}
		if (source == null || source.getClass() == type) {
			return source;
		}
		if (source instanceof ExtendConverter) {
			ExtendConverter converter = ExtendConverter.class.cast(source);
			return converter.convert(type, this);
		}
		for (Converter converter : converters) {
			if (converter.support(type)) {
				if (type instanceof Class<?>) {
					return converter.convert(source, name, Class.class.cast(type), this);
				}
				if (type instanceof ParameterizedType) {
					return converter.convert(source, name, ParameterizedType.class.cast(type), this);
				}
				if (type instanceof GenericArrayType) {
					return converter.convert(source, name, GenericArrayType.class.cast(type), this);
				}
			}
		}
		return source;
	}

	public boolean isExcluded(String name) {
		return excludes.contains(name);
	}

	public <T> T convert(Object source, String name, Class<T> type, Type... typeArguments) throws Exception {
		return type.cast(doConvert(source, name, typeArguments == null || typeArguments.length == 0 ? type : new ParameterizedTypeImpl(type, null, typeArguments)));
	}

	public <T> List<T> list(Iterable<?> iterable, String name, Class<T> type, Type... typeArguments) throws Exception {
		List<T> result = new ArrayList<T>();
		for (Object element : iterable) {
			result.add(convert(element, name, type, typeArguments));
		}
		return result;
	}

	public <T> Set<T> set(Iterable<?> iterable, String name, Class<T> type, Type... typeArguments) throws Exception {
		Set<T> result = new LinkedHashSet<T>();
		for (Object element : iterable) {
			result.add(convert(element, name, type, typeArguments));
		}
		return result;
	}

	public void copy(Object source, String name, Object target) throws Exception {
		if (source == null || name == null || target == null) {
			throw new NullPointerException("source,name or target must not null");
		}
		for (Converter converter : converters) {
			if (converter.support(target.getClass()) == false) {
				continue;
			}
			if (converter instanceof ComplexConverter == false) {
				throw new IllegalArgumentException("only complex object can be copy");
			}

			source = converter.convert(source, name, target.getClass(), this);

			PropertyDescriptor[] descriptors = Introspector.getBeanInfo(target.getClass()).getPropertyDescriptors();
			for (PropertyDescriptor descriptor : descriptors) {
				String _name = name + "." + descriptor.getName();
				if (descriptor.getName().equals("class") || this.isExcluded(_name)) {
					continue;
				}
				try {
					descriptor = new PropertyDescriptor(descriptor.getName(), target.getClass());
					Object value = descriptor.getReadMethod().invoke(source);
					descriptor.getWriteMethod().invoke(target, value);
				} catch (IntrospectionException e) {
					continue;
				}
			}

			break;
		}
	}

	/**
	 * 转换
	 * 
	 * @param source
	 *            源对象
	 * @param type
	 *            最外层类型
	 * @param typeArguments
	 *            实际参数类型
	 * @return 转换之后的目标类型对象
	 */
	public static <T> T convert(Object source, Class<T> type, Type... typeArguments) throws Exception {
		return new TypeConverter().convert(source, "", type, typeArguments);
	}

	public static <E> List<E> list(Iterable<?> iterable, Class<E> elementType, Type... typeArguments) throws Exception {
		return new TypeConverter().list(iterable, "", elementType, typeArguments);
	}

	public static <E> Set<E> set(Iterable<?> iterable, Class<E> elementType, Type... typeArguments) throws Exception {
		return new TypeConverter().set(iterable, "", elementType, typeArguments);
	}

	public static void copy(Object source, Object target) throws Exception {
		new TypeConverter().copy(source, "", target);
	}

}
