/**
 * utils[com.change.utils.transformer]
 * Change
 * 2014年1月9日 下午11:09:09
 */

package com.qfox.commons.xml2bean;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;

import org.dom4j.Element;

/**
 * 
 * 
 * @Description:变形器接口
 * 
 * 
 * @author Change
 * @date 2014年1月9日 下午9:27:28
 * 
 *       修订记录<br/>
 *       姓名:Change<br/>
 *       时间:2014年1月9日 下午9:27:28<br/>
 *       内容:初始<br/>
 * 
 */
public interface Transformer {

	String tag();

	Object transform(Element element, Class<?> _class, TransformerContext context) throws Exception;

	Object transform(Element element, ParameterizedType type, TransformerContext context) throws Exception;

	Object transform(Element element, GenericArrayType type, TransformerContext context) throws Exception;

	boolean support(Object object);

	Element transform(Object object, Element parent, TransformerContext context) throws Exception;

}
