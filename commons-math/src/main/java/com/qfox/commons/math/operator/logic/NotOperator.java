/**
 * utils[com.change.utils.math.operator.logic]
 * Change
 * 2014年1月18日 下午4:06:01
 */

package com.qfox.commons.math.operator.logic;

import com.qfox.commons.math.Calculator;
import com.qfox.commons.math.operator.Operator;

/**
 * 
 * 
 * @Description:
 * 
 * 
 * @author Change
 * @date 2014年1月18日 下午4:06:01
 * 
 *       修订记录<br/>
 *       姓名:Change<br/>
 *       时间:2014年1月18日 下午4:06:01<br/>
 *       内容:初始<br/>
 * 
 */
public class NotOperator implements Operator {

	public String pattern() {
		return "\\!" + BOOLEAN;
	}

	public String operate(String expression, Calculator calculator) {
		Boolean value = Boolean.valueOf(expression.substring(expression.indexOf("!") + 1, expression.length()));
		return String.valueOf(!value);
	}

}
