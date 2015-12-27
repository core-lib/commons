/**
 * utils[com.change.utils.math.operator.rounding]
 * Change
 * 2014年1月20日 上午11:08:33
 */

package com.qfox.commons.math.operator.rounding;

import com.qfox.commons.math.Calculator;
import com.qfox.commons.math.operator.Operator;

/**
 * 
 * 
 * @Description:
 * 
 * 
 * @author Change
 * @date 2014年1月20日 上午11:08:33
 * 
 *       修订记录<br/>
 *       姓名:Change<br/>
 *       时间:2014年1月20日 上午11:08:33<br/>
 *       内容:初始<br/>
 * 
 */
public class RoundOperator implements Operator {

	public String pattern() {
		return "round" + NUMBER;
	}

	public String operate(String expression, Calculator calculator) {
		Double number = Double.valueOf(expression.substring(5, expression.length()));
		return String.valueOf(Math.round(number));
	}

}
