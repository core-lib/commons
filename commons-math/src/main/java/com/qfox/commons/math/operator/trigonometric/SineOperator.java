/**
 * utils[com.change.utils.math.operator]
 * Change
 * 2014年1月18日 下午3:10:10
 */

package com.qfox.commons.math.operator.trigonometric;

import com.qfox.commons.math.Calculator;
import com.qfox.commons.math.operator.Operator;

/**
 * 
 * 
 * @Description:
 * 
 * 
 * @author Change
 * @date 2014年1月18日 下午3:10:10
 * 
 *       修订记录<br/>
 *       姓名:Change<br/>
 *       时间:2014年1月18日 下午3:10:10<br/>
 *       内容:初始<br/>
 * 
 */
public class SineOperator implements Operator {

	public String pattern() {
		return "sin" + NUMBER;
	}

	public String operate(String expression, Calculator calculator) {
		Double number = Double.valueOf(expression.substring("sin".length()));
		return String.valueOf(Math.sin(number));
	}

}
