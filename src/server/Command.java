package server;

public interface Command extends Comparable<Command> {

	public void perform(Game g, float delta);

	public long getTime();

}
