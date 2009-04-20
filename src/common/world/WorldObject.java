package common.world;

import com.jme.scene.Node;
import common.Player;

public abstract class WorldObject extends Node {
	private static final long serialVersionUID = 1L;
	protected Player owner = NoPlayer.instance;

	public WorldObject(String name, Player owner) {
		super(name);
		this.owner = owner;
	}

	public Player getOwner() {
		return owner;
	}

}
