package client;

import java.util.HashMap;

import client.command.DestroyObjectCommand;

import common.Player;
import common.world.Missile;
import common.world.Ship;
import common.world.WorldObject;

public class GameLogic extends common.GameLogic {

	private final HashMap<Integer, Player> players;
	private Player self;
	private Game game;

	public GameLogic(Game game, Player self) {
		super();
		this.game=game;
		players = new HashMap<Integer, Player>();
		this.self = self;
	}
	
	public Player getSelf() {
		return self;
	}

	public Player getPlayer(int id) {
		return players.get(id);
	}

	public void add(Ship s) {
		final Player owner = s.getOwner();
		players.put(owner.getID(), owner);
		super.add(s);
	}

	public void removeShip(Ship s) {
		final Player p = s.getOwner();
		super.remove(p);
		players.remove(p.getID());
	}

	public Ship getShipByPlayerID(int playerid) {
		return getShipByPlayer(players.get(playerid));
	}

	public void destroy(WorldObject wobj) {
		_destroy(wobj);
	}

	public void destroy(Missile m) {
		_destroy(m);
	}

	public void destroy(Ship ship) {
		_destroy(ship);
	}

	private void _destroy(WorldObject wobj) {
		wobj.setHP(0);
		game.getUniverse().removeChild(wobj);
		remove(wobj);
	}

}
