/**
 * utils[com.change.utils.transformer]
 * Change
 * 2014年1月9日 下午11:27:58
 */

package com.qfox.commons.xml2bean;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.dom4j.Element;

/**
 * 
 * 
 * @Description:数组类型变形器
 * 
 * 
 * @author Change
 * @date 2014年1月9日 下午11:27:58
 * 
 *       修订记录<br/>
 *       姓名:Change<br/>
 *       时间:2014年1月9日 下午11:27:58<br/>
 *       内容:初始<br/>
 * 
 */
public class ArrayTransformer implements Transformer {

	public String tag() {
		return "array";
	}

	public Object transform(Element element, Class<?> _class, TransformerContext context) throws Exception {
		List<?> elements = element.elements();
		Object array = Array.newInstance(_class.getComponentType(), elements.size());
		for (int i = 0; i < elements.size(); i++) {
			Object object = context.toObject(Element.class.cast(elements.get(i)), _class.getComponentType());
			Array.set(array, i, object);
		}
		return array;
	}

	public Object transform(Element element, ParameterizedType type, TransformerContext context) throws Exception {
		return null;
	}

	public Object transform(Element element, GenericArrayType type, TransformerContext context) throws Exception {
		int dimension = 0;
		Type temp = type;
		while (temp instanceof GenericArrayType) {
			temp = GenericArrayType.class.cast(temp).getGenericComponentType();
			dimension += 1;
		}
		int[] dimensions = new int[dimension];
		List<?> elements = element.elements();
		dimensions[0] = elements.size();

		Object array = Array.newInstance(Class.class.cast(ParameterizedType.class.cast(temp).getRawType()), dimensions);
		for (int i = 0; i < elements.size(); i++) {
			Object object = context.toObject(Element.class.cast(elements.get(i)), type.getGenericComponentType());
			Array.set(array, i, object);
		}
		return array;
	}

	public boolean support(Object object) {
		return object.getClass().isArray();
	}

	public Element transform(Object object, Element parent, TransformerContext context) throws Exception {
		Element element = parent.addElement("array");
		int length = Array.getLength(object);
		for (int i = 0; i < length; i++) {
			context.toElement(Array.get(object, i), element);
		}
		return element;
	}

}
