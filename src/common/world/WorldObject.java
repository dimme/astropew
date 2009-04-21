package common.world;

import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Spatial;

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

	public void interpolate(float delta, long currentTime) {
	}
}
