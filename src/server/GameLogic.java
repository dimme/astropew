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

	public void destroy(WorldObject worldObject, WorldObject instigator) {
		_destroy(worldObject, instigator);
	}

	private void _destroy(WorldObject wobj, WorldObject instigator) {
		game.addCommand(new DestroyCommand(wobj, instigator, game.getFrameTime()));
	}

	public void destroy(Missile m, WorldObject instigator) {
		_destroy(m, instigator);
	}

	public void destroy(Ship ship, WorldObject instigator) {
		_destroy(ship, instigator);
		game.addCommand(new SpawnCommand(ship, game.getFrameTime()+5f));
	}
}
