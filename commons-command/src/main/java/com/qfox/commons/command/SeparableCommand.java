
package com.qfox.commons.command;

/**
 * 
 * 
 * 
 * @Description: 可拆分命令,约定 实现这个接口的命令 将任务拆分成多个子命令 本身的 execute 方法实现将子命令的结果汇总生成新的结果
 * @author Change
 * @date 2013年12月26日
 * 
 *       修订记录<br/>
 *       姓名:Change<br/>
 *       时间:2013年12月26日<br/>
 *       内容:初始<br/>
 * 
 */
public interface SeparableCommand<Result> extends Command<Result> {

	/**
	 * 主命令准备
	 */
	void prepare();

	/**
	 * 拆分成多个子命令
	 * 
	 * @return 子命令数组
	 */
	Command<?>[] separate();

}
