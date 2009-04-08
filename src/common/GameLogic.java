package common;

import java.util.HashMap;

import common.Player;
import common.world.Ship;

public abstract class GameLogic {
	protected HashMap<Player, Ship> shiptable = new HashMap<Player, Ship>();
	protected Game world;
	
	public GameLogic(Game world) {
		this.world = world;
	}
}
