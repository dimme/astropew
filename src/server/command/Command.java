package server.command;


public interface Command extends Comparable<Command> {

	public void perform(GameCommandInterface gci);

	public float getTime();

}
