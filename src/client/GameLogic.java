package client;

import java.util.HashMap;

import common.world.Ship;
import common.Player;

public class GameLogic extends common.GameLogic 
{

	private HashMap<Integer, Player> players;
	
	public GameLogic() {
		super();
		players = new HashMap<Integer, Player>();
	}
	
	public Player getPlayer(int id) {
		return players.get(id);
	}
	
	public void addShip(Ship s) {
		Player owner = s.getOwner();
		players.put(owner.getID(), owner);
		super.addShip(s);
	}

	public Ship getShip(int id) {
		return shiptable.get(players.get(id));
	}
	
}
