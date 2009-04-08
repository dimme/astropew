package common;

import java.util.Collection;
import java.util.HashMap;

import common.Player;
import common.world.Ship;

public abstract class GameLogic {
	protected HashMap<Player, Ship> shiptable = new HashMap<Player, Ship>();
	
	public GameLogic() {
	}
	
	public void addShip(Ship s) {
		shiptable.put(s.getOwner(), s);
	}
	
	public Ship removeShip(Player owner) {
		return shiptable.remove(owner);
	}
	
	public Collection<Ship> getShips() {
		return shiptable.values();
	}
}
