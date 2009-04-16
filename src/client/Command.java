package client;


public interface Command extends Comparable<Command> {

	public void perform(Game game);

	public long getTick();

}
