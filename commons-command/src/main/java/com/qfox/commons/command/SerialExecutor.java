
package com.qfox.commons.command;

import java.util.Arrays;

/**
 * 
 * 
 * 
 * @Description: 串行执行者
 * @author Change
 * @date 2013年12月26日
 * 
 *       修订记录<br/>
 *       姓名:Change<br/>
 *       时间:2013年12月26日<br/>
 *       内容:初始<br/>
 * 
 */
public class SerialExecutor implements Executor {

	/**
	 * 执行命令 的串行执行实现 将命令的子命令先完成再完成自己
	 */
	public void execute(Command<?> command) {
		if (command.executed()) {
			throw new RuntimeException("command [" + command + "] had been executed!");
		}
		if (command instanceof SeparableCommand) {
			SeparableCommand<?> separableCommand = (SeparableCommand<?>) command;
			separableCommand.prepare();
			Command<?>[] subcommands = separableCommand.separate();
			if (subcommands != null && subcommands.length > 0) {
				Arrays.sort(subcommands);
				for (Command<?> subcommand : subcommands) {
					execute(subcommand);
				}
			}
		}
		command.run();
		command.finish();
	}

}
