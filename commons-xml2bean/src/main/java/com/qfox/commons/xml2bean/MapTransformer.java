/**
 * utils[com.change.utils.transformer]
 * Change
 * 2014年1月9日 下午11:59:35
 */

package com.qfox.commons.xml2bean;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

/**
 * 
 * 
 * @Description:映射集合类型变形器
 * 
 * 
 * @author Change
 * @date 2014年1月9日 下午11:59:35
 * 
 *       修订记录<br/>
 *       姓名:Change<br/>
 *       时间:2014年1月9日 下午11:59:35<br/>
 *       内容:初始<br/>
 * 
 */
public class MapTransformer implements Transformer {

	public String tag() {
		return "map";
	}

	public Object transform(Element element, Class<?> _class, TransformerContext context) throws Exception {
		List<?> elements = element.elements("entry");
		Map<Object, Object> map = new HashMap<Object, Object>();
		for (Object object : elements) {
			Element entry = Element.class.cast(object);
			Element key = Element.class.cast(entry.elements().get(0));
			Element value = Element.class.cast(entry.elements().get(1));
			map.put(context.toObject(key, null), context.toObject(value, null));
		}
		return _class.getConstructor(Map.class).newInstance(map);
	}

	public Object transform(Element element, ParameterizedType type, TransformerContext context) throws Exception {
		List<?> elements = element.elements("entry");
		Map<Object, Object> map = new HashMap<Object, Object>();
		for (Object object : elements) {
			Element entry = Element.class.cast(object);
			Element key = Element.class.cast(entry.elements().get(0));
			Element value = Element.class.cast(entry.elements().get(1));
			map.put(context.toObject(key, type.getActualTypeArguments()[0]), context.toObject(value, type.getActualTypeArguments()[1]));
		}
		Class<?> _class = Class.class.cast(type.getRawType());
		return _class.getConstructor(Map.class).newInstance(map);
	}

	public Object transform(Element element, GenericArrayType type, TransformerContext context) throws Exception {
		return null;
	}

	public boolean support(Object object) {
		return object instanceof Map<?, ?>;
	}

	public Element transform(Object object, Element parent, TransformerContext context) throws Exception {
		Element element = parent.addElement("map");
		Map<?, ?> map = Map.class.cast(object);
		for (Map.Entry<?, ?> entry : map.entrySet()) {
			Element _element = element.addElement("entry");
			context.toElement(entry.getKey(), _element);
			context.toElement(entry.getValue(), _element);
		}
		return element;
	}

}
