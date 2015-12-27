
package com.qfox.commons.command;

public abstract class AbstractCommand<Result> implements Command<Result> {

	protected boolean executed = false;

	protected Result result;

	public Result result() {
		return result;
	}

	public void finish() {
		executed = true;
	}

	public boolean executed() {
		return executed;
	}

	public int compareTo(Command<?> o) {
		return this.priority() > o.priority() ? -1 : this.priority() < o.priority() ? 1 : 0;
	}

}
