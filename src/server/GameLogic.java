package server;

import server.command.DestroyCommand;
import server.command.SpawnCommand;

import common.Player;
import common.world.Missile;
import common.world.Ship;
import common.world.WorldObject;

public class GameLogic extends common.GameLogic {

	private final Game game;

	public GameLogic(Game game) {
		super();
		this.game = game;
	}

	public void destroy(WorldObject worldObject, Player instigator) {
		_destroy(worldObject, instigator);
	}

	private void _destroy(WorldObject wobj, Player instigator) {
		game.addCommand(new DestroyCommand(wobj, instigator, game.getFrameTime()));
	}

	public void destroy(Missile m, Player instigator) {
		_destroy(m, instigator);
	}

	public void destroy(Ship ship, Player instigator) {
		_destroy(ship, instigator);
		game.addCommand(new SpawnCommand(ship, game.getFrameTime()+5f));
	}
}
