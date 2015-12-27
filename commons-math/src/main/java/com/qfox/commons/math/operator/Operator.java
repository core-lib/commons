/**
 * utils[com.change.utils.math]
 * Change
 * 2014年1月18日 下午2:44:56
 */

package com.qfox.commons.math.operator;

import com.qfox.commons.math.Calculator;

/**
 * 
 * 
 * @Description: 运算的接口,通过拓展该接口可以实现各种各样的运算
 * 
 * 
 * @author Change
 * @date 2014年1月18日 下午2:44:56
 * 
 *       修订记录<br/>
 *       姓名:Change<br/>
 *       时间:2014年1月18日 下午2:44:56<br/>
 *       内容:初始<br/>
 * 
 */
public interface Operator {

	// 数字
	String NUMBER = "(?:\\-?(?:\\d+\\.)?\\d+(?:E\\-?\\d+)?)";

	// boolean 类型
	String BOOLEAN = "(?:true|false)";

	// 集合
	String COLLECTION = "(?:\\{(?:\\-?(?:\\d+\\.)?\\d+(?:E\\-?\\d+)?)(?:\\,(?:\\-?(?:\\d+\\.)?\\d+(?:E\\-?\\d+)?))*\\})";

	// 区间
	String RANGE = "(?:\\[(?:\\-?(?:\\d+\\.)?\\d+(?:E\\-?\\d+)?)\\,(?:\\-?(?:\\d+\\.)?\\d+(?:E\\-?\\d+)?)\\])";

	/**
	 * 该运算的表达式的定义模板
	 * 
	 * @return 表达式的定义模板
	 */
	String pattern();

	/**
	 * 运算匹配到的表达式
	 * 
	 * @param expression
	 *            表达式
	 * @param 当前计算器
	 * @return 运算结果
	 */
	String operate(String expression, Calculator calculator);

}
