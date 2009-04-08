package common.world;

import com.jme.math.Vector3f;
import com.jme.scene.Node;
import common.Player;

public abstract class WorldObject extends Node {
	
	protected Player owner = NoPlayer.instance;
	
	public WorldObject(String name) {
		super(name);
	}
	
	public Player getOwner() {
		return owner;
	}
	
}
