/**
 * utils[com.change.utils.math.operator]
 * Change
 * 2014年1月18日 下午3:23:37
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
 * @date 2014年1月18日 下午3:23:37
 * 
 *       修订记录<br/>
 *       姓名:Change<br/>
 *       时间:2014年1月18日 下午3:23:37<br/>
 *       内容:初始<br/>
 * 
 */
public class TangentOperator implements Operator {

	public String pattern() {
		return "tan" + NUMBER;
	}

	public String operate(String expression, Calculator calculator) {
		Double number = Double.valueOf(expression.substring("tan".length()));
		return String.valueOf(Math.tan(number));
	}

}
