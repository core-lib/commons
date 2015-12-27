package com.qfox.radio.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 收听者
 * 
 * @author Payne
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Inherited
public @interface Listener {

	/**
	 * 频率
	 * 
	 * @return 广播频率
	 */
	String frequency();

	/**
	 * 信号强度 信号越强 则优先权越大 范围为从 1 到 10 默认值是5
	 * 
	 * @return 信号强度
	 */
	int intensity() default 5;

}
