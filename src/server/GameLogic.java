package server;

import server.command.DestroyCommand;
import server.command.SpawnCommand;
import common.world.Missile;
import common.world.Ship;
import common.world.WorldObject;

public class GameLogic extends common.GameLogic {

	private final Game game;
	
	public GameLogic(Game game) {
		super();
		this.game = game;
	}
	
	public void destroy(WorldObject worldObject) {
		_destroy(worldObject);
	}
	
	private void _destroy(WorldObject wobj) {
		game.addCommand(new DestroyCommand(wobj, game.getFrameTime()));
	}

	public void destroy(Missile m) {
		_destroy(m);
	}

	public void destroy(Ship ship) {
		_destroy(ship);
		game.addCommand(new SpawnCommand(ship, game.getFrameTime()+5f));
	}
}
