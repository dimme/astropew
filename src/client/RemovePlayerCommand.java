package client;

import common.world.Ship;

public class RemovePlayerCommand extends AbstractCommand {
	private final int id;

	public RemovePlayerCommand(int id) {
		super(0);
		this.id = id;
	}

	public void perform(Game game) {
		game.removePlayer(id);
	}

}
