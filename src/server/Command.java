package server;

public interface Command extends Comparable<Command> {

	public void perform(GameCommandInterface gci);

	public float getTime();

}
