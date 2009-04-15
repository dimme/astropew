package common.world;

import com.jme.scene.Node;
import common.Player;

public abstract class WorldObject extends Node {
	private static final long serialVersionUID = 1L;
	protected Player owner = NoPlayer.instance;
	protected long latestTick;
	
	public boolean checkTick(long newTick){
		if ( latestTick > newTick ) {
			return false;
		}
		latestTick = newTick;
		return true;
	}

	public WorldObject(String name) {
		super(name);
		latestTick = 0;
	}

	public Player getOwner() {
		return owner;
	}

}
