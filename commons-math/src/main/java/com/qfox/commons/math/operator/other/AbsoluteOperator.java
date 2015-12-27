/**
 * utils[com.change.utils.math.operator.other]
 * Change
 * 2014年1月20日 下午3:17:19
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
 * @date 2014年1月20日 下午3:17:19
 * 
 *       修订记录<br/>
 *       姓名:Change<br/>
 *       时间:2014年1月20日 下午3:17:19<br/>
 *       内容:初始<br/>
 * 
 */
public class AbsoluteOperator implements Operator {

	public String pattern() {
		return "\\|" + NUMBER + "\\|";
	}

	public String operate(String expression, Calculator calculator) {
		Double number = Double.valueOf(expression.substring(1, expression.length() - 1));
		return String.valueOf(Math.abs(number));
	}

}
