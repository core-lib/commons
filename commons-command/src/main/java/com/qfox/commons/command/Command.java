
package com.qfox.commons.command;

/**
 * 
 * 
 * 
 * @Description: 命令
 * @author Change
 * @date 2013年12月26日
 * 
 *       修订记录<br/>
 *       姓名:Change<br/>
 *       时间:2013年12月26日<br/>
 *       内容:初始<br/>
 * 
 */
public interface Command<Result> extends Runnable, Comparable<Command<?>> {

	/**
	 * 获取优先级
	 * 
	 * @return 优先级
	 */
	int priority();

	/**
	 * 获取执行结果
	 * 
	 * @return 执行结果
	 */
	Result result();

	/**
	 * 标记命令已完成
	 */
	void finish();

	/**
	 * 获取该命令是否已执行完
	 * 
	 * @return 如果命令在已经完成 返回 true 否则返回 false
	 */
	boolean executed();

}
