package common.world;

import com.jme.scene.Node;
import common.Player;

public abstract class WorldObject extends Node {
	private static final long serialVersionUID = 1L;
	protected Player owner = NoPlayer.instance;

	public WorldObject(String name) {
		super(name);
	}

	public Player getOwner() {
		return owner;
	}

}
