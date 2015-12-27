/**
 * utils[com.change.utils.math.operator.other]
 * Change
 * 2014年1月20日 下午3:04:52
 */

package com.qfox.commons.math.operator.other;

import com.qfox.commons.math.Calculator;
import com.qfox.commons.math.operator.Operator;

/**
 * 
 * 
 * @Description:
 * 
 * 
 * @author Change
 * @date 2014年1月20日 下午3:04:52
 * 
 *       修订记录<br/>
 *       姓名:Change<br/>
 *       时间:2014年1月20日 下午3:04:52<br/>
 *       内容:初始<br/>
 * 
 */
public class ModuloOperator implements Operator {

	public String pattern() {
		return "mod" + NUMBER + "\\," + NUMBER;
	}

	public String operate(String expression, Calculator calculator) {
		Double number = Double.valueOf(expression.substring(3, expression.indexOf(",")));
		Double divisor = Double.valueOf(expression.substring(expression.indexOf(",") + 1, expression.length()));
		double n = Math.floor(number / divisor);
		return String.valueOf(number - n * divisor);
	}

}
