/**
 * utils[com.change.utils.math.operator.other]
 * Change
 * 2014年1月20日 下午6:12:06
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
 * @date 2014年1月20日 下午6:12:06
 * 
 *       修订记录<br/>
 *       姓名:Change<br/>
 *       时间:2014年1月20日 下午6:12:06<br/>
 *       内容:初始<br/>
 * 
 */
public class SignOperator implements Operator {

	public String pattern() {
		return "sign" + NUMBER;
	}

	public String operate(String expression, Calculator calculator) {
		Double number = Double.valueOf(expression.substring(4, expression.length()));
		return String.valueOf(Math.signum(number));
	}

}
