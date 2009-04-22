package client;

import java.util.HashMap;

import common.Player;
import common.world.Ship;

public class GameLogic extends common.GameLogic {

	private final HashMap<Integer, Player> players;
	private Player self;

	public GameLogic(Player self) {
		super();
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

}
