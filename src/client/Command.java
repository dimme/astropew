package client;

public interface Command extends Comparable<Command> {

	public void perform(GameLogic logic, Game game);
	
	public long getTick();
	
}
