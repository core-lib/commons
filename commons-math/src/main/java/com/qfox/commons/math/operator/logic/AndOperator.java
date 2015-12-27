/**
 * utils[com.change.utils.math.operator]
 * Change
 * 2014年1月18日 下午3:45:16
 */

package com.qfox.commons.math.operator.logic;

import com.qfox.commons.math.Calculator;
import com.qfox.commons.math.operator.Operator;

/**
 * 
 * 
 * @Description:
 * 
 * 
 * @author Change
 * @date 2014年1月18日 下午3:45:16
 * 
 *       修订记录<br/>
 *       姓名:Change<br/>
 *       时间:2014年1月18日 下午3:45:16<br/>
 *       内容:初始<br/>
 * 
 */
public class AndOperator implements Operator {

	public String pattern() {
		return BOOLEAN + "\\&\\&" + BOOLEAN;
	}

	public String operate(String expression, Calculator calculator) {
		Boolean left = Boolean.valueOf(expression.substring(0, expression.indexOf("&&")));
		Boolean right = Boolean.valueOf(expression.substring(expression.indexOf("&&") + 2, expression.length()));
		return String.valueOf(left && right);
	}

}
