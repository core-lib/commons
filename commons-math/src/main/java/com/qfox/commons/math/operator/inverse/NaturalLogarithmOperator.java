/**
 * utils[com.change.utils.math.operator]
 * Change
 * 2014年1月18日 下午3:49:38
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
 * @date 2014年1月18日 下午3:49:38
 * 
 *       修订记录<br/>
 *       姓名:Change<br/>
 *       时间:2014年1月18日 下午3:49:38<br/>
 *       内容:初始<br/>
 * 
 */
public class NaturalLogarithmOperator implements Operator {

	public String pattern() {
		return "ln" + NUMBER;
	}

	public String operate(String expression, Calculator calculator) {
		Double number = Double.valueOf(expression.substring(2, expression.length()));
		return String.valueOf(Math.log(number));
	}

}
