package client.command;


public abstract class AbstractCommand implements Command {

	protected float time;

	public AbstractCommand(float time) {
		this.time = time;
	}

	public float getTime() {
		return time;
	}

	public int compareTo(Command c) {
		if (getTime() == c.getTime()) {
			return new Integer(c.hashCode()).compareTo(this.hashCode());
		}

		return getTime() > c.getTime() ? 1 : -1;
	}

	public boolean equals(Object o) {
		if (o instanceof Command) {
			return compareTo((Command) o) == 0;
		}
		return false;
	}
}
