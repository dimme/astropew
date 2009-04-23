package client;

import java.util.HashMap;

import client.command.DestroyObjectCommand;

import common.Player;
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

	public Ship getShip(int id) {
		return shiptable.get(players.get(id));
	}

	public void destroy(WorldObject wobj) {
		game.addCommand(new DestroyObjectCommand(wobj.getID()));
	}

}
