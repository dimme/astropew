package common.world;

import server.command.DestroyCommand;

import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Spatial;

import common.GameLogic;
import common.Player;

public abstract class WorldObject extends Node {
	private static final long serialVersionUID = 1L;
	protected Player owner = NoPlayer.instance;
	protected final int id;
	protected final GameLogic logic;
	
	protected float hp;

	public WorldObject(GameLogic logic, int id, String name, Player owner) {
		super(name);
		this.logic = logic;
		this.id = id;
		this.owner = owner;
		hp = 100;
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
	
	/**
	 * 
	 * @param dmg
	 * @return true if the object was destroyed
	 */
	public final boolean takeDamage(float dmg) {
		float ad = actualDamage(dmg);
		hp -= ad;
		return checkDestroy();
	}
	
	protected final boolean checkDestroy() {
		if (hp <= 0) {
			destroy();
			return true;
		}
		return false;
	}
	
	protected abstract void destroy();

	protected float actualDamage(float dmg) {
		return 0; //no damage as default
	}
	
	public float getHP() {
		return hp;
	}
	
	public void setHP(float hp) {
		this.hp = hp;
		checkDestroy();
	}
	
	public boolean isAlive() {
		return hp > 0;
	}
	
	public int hashCode() {
		return id;
	}
}
