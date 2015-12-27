
package com.qfox.commons.command;

/**
 * 
 * 
 * 
 * @Description:独立执行者
 * @author Change
 * @date 2013年12月31日
 * 
 *       修订记录<br/>
 *       姓名:Change<br/>
 *       时间:2013年12月31日<br/>
 *       内容:初始<br/>
 * 
 */
public class IndependentExecutor implements Executor {

	public void execute(Command<?> command) {
		if (command.executed()) {
			throw new RuntimeException("command [" + command + "] had been executed!");
		}
		if (command instanceof SeparableCommand) {
			SeparableCommand<?> separableCommand = (SeparableCommand<?>) command;
			separableCommand.prepare();
			Command<?>[] subcommands = separableCommand.separate();
			if (subcommands != null && subcommands.length > 0) {
				for (Command<?> subcommand : subcommands) {
					execute(subcommand);
				}
			}
		}
		final Command<?> cmd = command;
		Thread thread = new Thread(new Runnable() {

			public void run() {
				cmd.run();
				cmd.finish();
			}

		});
		thread.setPriority(cmd.priority() < Thread.MIN_PRIORITY ? Thread.MIN_PRIORITY : cmd.priority() > Thread.MAX_PRIORITY ? Thread.MAX_PRIORITY : cmd.priority());
		thread.start();
	}

}
