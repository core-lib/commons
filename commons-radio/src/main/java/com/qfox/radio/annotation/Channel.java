package com.qfox.radio.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.qfox.radio.core.Broadcaster;

/**
 * 频道
 * 
 * @author Payne
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Inherited
public @interface Channel {

	/**
	 * 频率
	 * 
	 * @return 广播频率
	 */
	String frequency();

	/**
	 * 设置频道的广播器 默认是异步广播器
	 * 
	 * @return 信号广播器
	 */
	Broadcaster broadcaster() default Broadcaster.ASYNCHRONOUS;

	/**
	 * 广播结果
	 * 
	 * @return 广播结果消息配置 当注解的 value = "" 则不广播结果
	 */
	Message result() default @Message("result");

	/**
	 * 广播异常
	 * 
	 * @return 广播异常消息配置 当注解的 value = "" 则忽略异常
	 */
	Message exception() default @Message("exception");

}
