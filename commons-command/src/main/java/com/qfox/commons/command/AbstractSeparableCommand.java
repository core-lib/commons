
package com.qfox.commons.command;

public abstract class AbstractSeparableCommand<Result> extends AbstractCommand<Result> implements SeparableCommand<Result> {

	protected Command<?>[] subcommands;

	public Command<?>[] separate() {
		return subcommands;
	}

}
