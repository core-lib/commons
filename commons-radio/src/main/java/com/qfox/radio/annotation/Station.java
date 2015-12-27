package com.qfox.radio.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.qfox.radio.core.Filter;

/**
 * 广播基站
 * 
 * @author Payne
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
public @interface Station {

	/**
	 * 滤波器类型数组,用于对广播基站的所有频道信号波进行过滤或拦截操作,过滤链的顺序与该数组顺序一致
	 * 
	 * @return 滤波器类型数组
	 */
	Class<? extends Filter>[] filters() default {};

}
