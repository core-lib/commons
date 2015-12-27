/**
 * utils[com.change.utils.transformer]
 * Change
 * 2014年1月9日 下午11:43:05
 */

package com.qfox.commons.xml2bean;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.dom4j.Element;

/**
 * 
 * 
 * @Description:单列集合类型变形器
 * 
 * 
 * @author Change
 * @date 2014年1月9日 下午11:43:05
 * 
 *       修订记录<br/>
 *       姓名:Change<br/>
 *       时间:2014年1月9日 下午11:43:05<br/>
 *       内容:初始<br/>
 * 
 */
public class CollectionTransformer implements Transformer {
	private static Map<Class<?>, Class<?>> implementations = new LinkedHashMap<Class<?>, Class<?>>();

	static {
		implementations.put(SortedSet.class, TreeSet.class);
		implementations.put(Set.class, HashSet.class);
		implementations.put(List.class, ArrayList.class);
		implementations.put(Queue.class, PriorityQueue.class);
		implementations.put(Deque.class, ArrayDeque.class);
	}

	public String tag() {
		return "collection";
	}

	public Object transform(Element element, Class<?> _class, TransformerContext context) throws Exception {
		List<?> elements = element.elements();
		Collection<Object> collection = new ArrayList<Object>();
		for (Object _element : elements) {
			collection.add(context.toObject(Element.class.cast(_element), null));
		}
		try {
			return _class.getConstructor(Collection.class).newInstance(collection);
		} catch (Exception e) {
			for (Entry<Class<?>, Class<?>> entry : implementations.entrySet()) {
				if (entry.getKey().isAssignableFrom(_class)) {
					return entry.getValue().getConstructor(Collection.class).newInstance(collection);
				}
			}
			throw new RuntimeException(e);
		}
	}

	public Object transform(Element element, ParameterizedType type, TransformerContext context) throws Exception {
		List<?> elements = element.elements();
		Collection<Object> collection = new ArrayList<Object>();
		for (Object _element : elements) {
			collection.add(context.toObject(Element.class.cast(_element), type.getActualTypeArguments()[0]));
		}
		Class<?> _class = Class.class.cast(type.getRawType());
		try {
			return _class.getConstructor(Collection.class).newInstance(collection);
		} catch (Exception e) {
			for (Entry<Class<?>, Class<?>> entry : implementations.entrySet()) {
				if (entry.getKey().isAssignableFrom(_class)) {
					return entry.getValue().getConstructor(Collection.class).newInstance(collection);
				}
			}
			throw new RuntimeException(e);
		}
	}

	public Object transform(Element element, GenericArrayType type, TransformerContext context) throws Exception {
		return null;
	}

	public boolean support(Object object) {
		return object instanceof Collection<?>;
	}

	public Element transform(Object object, Element parent, TransformerContext context) throws Exception {
		Element element = parent.addElement("collection");
		Collection<?> collection = Collection.class.cast(object);
		for (Object o : collection) {
			context.toElement(o, element);
		}
		return element;
	}

}
