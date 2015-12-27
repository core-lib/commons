/**
 * utils[com.change.utils.math.operator]
 * Change
 * 2014年1月18日 下午3:48:28
 */

package com.qfox.commons.math.operator.inverse;

import com.qfox.commons.math.Calculator;
import com.qfox.commons.math.operator.Operator;


/**
 * 
 * 
 * @Description:
 * 
 * 
 * @author Change
 * @date 2014年1月18日 下午3:48:28
 * 
 *       修订记录<br/>
 *       姓名:Change<br/>
 *       时间:2014年1月18日 下午3:48:28<br/>
 *       内容:初始<br/>
 * 
 */
public class LogarithmOperator implements Operator {

	public String pattern() {
		return "log" + NUMBER + "\\," + NUMBER;
	}

	public String operate(String expression, Calculator calculator) {
		Double base = Double.valueOf(expression.substring(3, expression.indexOf(",")));
		Double number = Double.valueOf(expression.substring(expression.indexOf(",") + 1, expression.length()));
		return String.valueOf(Math.log(number) / Math.log(base));
	}

}
