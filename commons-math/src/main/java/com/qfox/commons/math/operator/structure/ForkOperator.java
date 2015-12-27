package com.qfox.commons.math.operator.structure;

import com.qfox.commons.math.Calculator;
import com.qfox.commons.math.operator.Operator;

/**
 * 
 * 
 * 
 * @Description:选择操作符
 * 
 * 
 * @author Change
 * @date 2014年1月25日 上午11:31:19
 * 
 *       修订记录<br/>
 *       姓名:Change<br/>
 *       时间:2014年4月3日 下午5:37:15<br/>
 *       内容:初始<br/>
 * 
 */
public class ForkOperator implements Operator {

	public String pattern() {
		return "[^?:]*\\?[^?:]*\\:[^?:]*";
	}

	public String operate(String expression, Calculator calculator) {
		String _boolean = expression.substring(0, expression.indexOf("?"));
		String _true = expression.substring(expression.indexOf("?") + 1, expression.indexOf(":"));
		String _false = expression.substring(expression.indexOf(":") + 1);
		return Boolean.valueOf(_boolean) ? _true : _false;
	}

}
