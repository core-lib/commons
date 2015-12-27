package com.qfox.radio.builder;

/**
 * 信号广播基站建造者
 * 
 * @author Payne
 * 
 */
public interface RadioStationBuilder {

	/**
	 * 建造基站
	 * 
	 * @param _class
	 *            基站类型
	 * @return 基站
	 */
	Object build(Class<?> _class);

}
