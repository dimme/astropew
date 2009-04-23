package server.command;

public abstract class AbstractCommand implements Command {

	protected float time;

	public AbstractCommand(float time) {
		this.time = time;
	}

	public float getTime() {
		return time;
	}

	public int compareTo(Command c) {
		if (time == c.getTime()) {
			return 0;
		}

		return time > c.getTime() ? 1 : -1;
	}

	public boolean equals(Object o) {
		if (o instanceof Command) {
			return compareTo((Command) o) == 0;
		}
		return false;
	}
}
