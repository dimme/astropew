package common;

import java.util.HashMap;

import common.Player;
import common.world.Ship;
import common.world.World;

public abstract class GameLogic {
	protected HashMap<Player, Ship> shiptable = new HashMap<Player, Ship>();
	protected World world;
	
	public GameLogic(World world) {
		this.world = world;
	}
}
