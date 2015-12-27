package com.qfox.commons.math.operator.contrast;

import com.qfox.commons.math.Calculator;
import com.qfox.commons.math.operator.Operator;

/**
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author yangchangpei 646742615@qq.com
 *
 * @date 2015年11月18日 下午10:07:35
 *
 * @version 1.0.0
 */
public class StringEqualOperator implements Operator {

	public String pattern() {
		return "\\w+\\=\\=\\w+";
	}

	public String operate(String expression, Calculator calculator) {
		String[] contents = expression.split("==");
		return contents[0].equals(contents[1]) ? Boolean.TRUE.toString() : Boolean.FALSE.toString();
	}

}
