package client;

public abstract class AbstractCommand implements Command {

	protected long tick;
	
	public AbstractCommand(long tick) {
		this.tick = tick;
	}
	
	public long getTick() {
		return tick;
	}

	public int compareTo(Command c) {
		if (tick == c.getTick()) {
			return 0;
		}
		
		return tick>c.getTick() ? 1 : -1;
	}
	
	public boolean equals(Object o) {
		if (o instanceof Command) {
			return compareTo((Command)o) == 0;
		}
		return false;
	}
}
