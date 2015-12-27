/**
 * utils[com.change.utils.math.operator]
 * Change
 * 2014年1月18日 下午3:02:11
 */

package com.qfox.commons.math.operator.base;

import com.qfox.commons.math.Calculator;
import com.qfox.commons.math.operator.Operator;

/**
 * 
 * 
 * @Description:
 * 
 * 
 * @author Change
 * @date 2014年1月18日 下午3:02:11
 * 
 *       修订记录<br/>
 *       姓名:Change<br/>
 *       时间:2014年1月18日 下午3:02:11<br/>
 *       内容:初始<br/>
 * 
 */
public class MultiplyOperator implements Operator {

	public String pattern() {
		return NUMBER + "\\*" + NUMBER;
	}

	public String operate(String expression, Calculator calculator) {
		Double left = Double.valueOf(expression.substring(0, expression.indexOf("*")));
		Double right = Double.valueOf(expression.substring(expression.indexOf("*") + 1, expression.length()));
		return String.valueOf(left * right);
	}

}
