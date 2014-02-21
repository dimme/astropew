package client;

import java.util.HashMap;

import common.Player;
import common.world.Missile;
import common.world.NoPlayer;
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
		Player p = (id == -1 ? NoPlayer.instance : players.get(id));
		return p;
	}

	public void add(Ship s)  {
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

	public void destroy(WorldObject wobj, Player instigator) {
		_destroy(wobj);
	}

	public void destroy(Missile m, Player instigator) {
		_destroy(m);
	}

	public void destroy(Ship ship, Player instigator) {
		_destroy(ship);
	}

	private void _destroy(WorldObject wobj) {
		game.getUniverse().removeChild(wobj);
		remove(wobj);
	}

}
