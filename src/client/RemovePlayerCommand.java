package client;

import common.world.Ship;

public class RemovePlayerCommand extends AbstractCommand {
	private final int id;

	public RemovePlayerCommand(int id) {
		super(0);
		this.id = id;
	}

	public void perform(GameLogic logic, Game game) {

		final Ship s = logic.getShip(id);
		game.removeFromRoot(s);
		logic.removeShip(s);
	}

}
