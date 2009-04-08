package common;

import java.util.HashMap;

import common.Player;
import common.world.Ship;

public abstract class GameLogic {
	protected HashMap<Player, Ship> shiptable = new HashMap<Player, Ship>();
	protected Game game;
	
	public GameLogic(Game game) {
		this.game = game;
	}
}
