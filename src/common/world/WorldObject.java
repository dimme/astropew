package common.world;

import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Spatial;

import common.Player;

public abstract class WorldObject extends Node {
	private static final long serialVersionUID = 1L;
	protected Player owner = NoPlayer.instance;
	protected final int id;

	public WorldObject(int id, String name, Player owner) {
		super(name);
		this.id = id;
		this.owner = owner;
	}

	public Player getOwner() {
		return owner;
	}
	
	public final int getID() {
		return id;
	}

	public void interpolate(float delta, long currentTime) {
	}

	/**
	 * Called when wobj's collision check found this object
	 * @param wobj
	 */
	public void collidedBy(WorldObject wobj) {
		
	}
	
	/**
	 * Called when this object's collision check found wobj
	 * @param wobj
	 */
	public void collidedWith(WorldObject wobj) {
		
	}
}
