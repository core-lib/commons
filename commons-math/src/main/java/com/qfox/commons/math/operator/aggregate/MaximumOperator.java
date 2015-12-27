/**
 * utils[com.change.utils.math.operator]
 * Change
 * 2014年1月18日 下午3:51:27
 */

package com.qfox.commons.math.operator.aggregate;

import com.qfox.commons.math.Calculator;
import com.qfox.commons.math.operator.Operator;

/**
 * 
 * 
 * @Description:
 * 
 * 
 * @author Change
 * @date 2014年1月18日 下午3:51:27
 * 
 *       修订记录<br/>
 *       姓名:Change<br/>
 *       时间:2014年1月18日 下午3:51:27<br/>
 *       内容:初始<br/>
 * 
 */
public class MaximumOperator implements Operator {

	public String pattern() {
		return "max" + COLLECTION;
	}

	public String operate(String expression, Calculator calculator) {
		String[] digits = expression.substring(expression.indexOf("{") + 1, expression.indexOf("}")).split(",");
		Double max = Double.valueOf(digits[0]);
		for (String digit : digits) {
			Double value = Double.valueOf(digit);
			if (value > max) {
				max = value;
			}
		}
		return String.valueOf(max);
	}

}
