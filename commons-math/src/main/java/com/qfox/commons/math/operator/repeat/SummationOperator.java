package com.qfox.commons.math.operator.repeat;

import com.qfox.commons.math.Calculator;
import com.qfox.commons.math.operator.Operator;

/**
 * 
 * 
 * 
 * @Description:
 * 
 * 
 * @author Change
 * @date 2014年1月25日 上午11:31:19
 * 
 *       修订记录<br/>
 *       姓名:Change<br/>
 *       时间:2014年4月1日 下午5:48:44<br/>
 *       内容:初始<br/>
 * 
 */
public class SummationOperator implements Operator {

	public String pattern() {
		return "E" + RANGE;
	}

	public String operate(String expression, Calculator calculator) {
		String[] range = expression.substring(expression.indexOf("[") + 1, expression.lastIndexOf("]")).split(",");
		Double from = Double.valueOf(range[0]);
		Double to = Double.valueOf(range[1]);

		if (from != from.intValue() || to != to.intValue()) {
			throw new RuntimeException("can not make summation for float number");
		}

		int f = Math.min(from.intValue(), to.intValue());
		int t = Math.max(from.intValue(), to.intValue());

		return String.valueOf((f + t) * (t - f + 1) / 2);
	}
}
