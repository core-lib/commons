/**
 * utils[com.change.utils.math.operator.rounding]
 * Change
 * 2014年1月20日 上午11:00:57
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
 * @date 2014年1月20日 上午11:00:57
 * 
 *       修订记录<br/>
 *       姓名:Change<br/>
 *       时间:2014年1月20日 上午11:00:57<br/>
 *       内容:初始<br/>
 * 
 */
public class CeilOperator implements Operator {

	public String pattern() {
		return "ceil" + NUMBER;
	}

	public String operate(String expression, Calculator calculator) {
		Double number = Double.valueOf(expression.substring(4, expression.length()));
		return String.valueOf(Math.ceil(number));
	}

}
