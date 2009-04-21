package client;


public interface Command extends Comparable<Command> {

	public void perform(Game game, float interpolation);

	public long getTime();

}
