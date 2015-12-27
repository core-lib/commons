/**
 * utils[com.change.utils.math.operator]
 * Change
 * 2014年1月18日 下午3:54:25
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
 * @date 2014年1月18日 下午3:54:25
 * 
 *       修订记录<br/>
 *       姓名:Change<br/>
 *       时间:2014年1月18日 下午3:54:25<br/>
 *       内容:初始<br/>
 * 
 */
public class AverageOperator implements Operator {

	public String pattern() {
		return "avg" + COLLECTION;
	}

	public String operate(String expression, Calculator calculator) {
		String[] digits = expression.substring(expression.indexOf("{") + 1, expression.indexOf("}")).split(",");
		Double sum = 0d;
		for (String digit : digits) {
			Double value = Double.valueOf(digit);
			sum = sum + value;
		}
		return String.valueOf(sum / digits.length);
	}

}
