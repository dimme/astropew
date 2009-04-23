package server;

import server.command.DestroyCommand;
import common.world.WorldObject;

public class GameLogic extends common.GameLogic {

	private final Game game;
	
	public GameLogic(Game game) {
		super();
		this.game = game;
	}
	
	public void destroy(WorldObject worldObject) {
		game.addCommand(new DestroyCommand(worldObject, game.getFrameTime()));
	}
}
