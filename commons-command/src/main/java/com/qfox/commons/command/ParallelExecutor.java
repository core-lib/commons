
package com.qfox.commons.command;

import java.util.concurrent.CountDownLatch;

/**
 * 
 * 
 * 
 * @Description: 并行执行者
 * @author Change
 * @date 2013年12月26日
 * 
 *       修订记录<br/>
 *       姓名:Change<br/>
 *       时间:2013年12月26日<br/>
 *       内容:初始<br/>
 * 
 */
public class ParallelExecutor implements Executor {

	public void execute(Command<?> command) {
		if (command.executed()) {
			throw new RuntimeException("command [" + command + "] had been executed!");
		}
		if (command instanceof SeparableCommand) {
			SeparableCommand<?> separableCommand = (SeparableCommand<?>) command;
			separableCommand.prepare();
			Command<?>[] subcommands = separableCommand.separate();
			if (subcommands != null && subcommands.length > 0) {
				final CountDownLatch latch = new CountDownLatch(subcommands.length);
				for (final Command<?> subcommand : subcommands) {

					Thread thread = new Thread(new Runnable() {

						public void run() {
							try {
								execute(subcommand);
							}
							catch (Exception e) {
								throw new RuntimeException(e);
							}
							finally {
								latch.countDown();
							}
						}
					});
					thread.setPriority(subcommand.priority() < Thread.MIN_PRIORITY ? Thread.MIN_PRIORITY : subcommand.priority() > Thread.MAX_PRIORITY ? Thread.MAX_PRIORITY : subcommand.priority());
					thread.start();

				}
				try {
					latch.await();
				}
				catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		}
		command.run();
		command.finish();
	}

}
