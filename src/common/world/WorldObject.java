package common.world;

import com.jme.scene.Node;
import common.GameLogic;
import common.Player;

public abstract class WorldObject extends Node {
	private static final long serialVersionUID = 1L;
	protected Player owner = NoPlayer.instance;
	protected final int id;
	protected final GameLogic logic;

	protected float hp;
	protected boolean hp_changed = false;
	protected float last_hp_update = 0;

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
	public final boolean takeDamage(float dmg, WorldObject instigator) {
		float ad = actualDamage(dmg);
		if (ad != 0) {
			hp -= ad;
			hp_changed = true;
		}
		if (checkDestroy()) {
			Player owner = instigator.getOwner();
			System.out.println(owner + " got points!");
			owner.setPoints(owner.getPoints() + 1000);
			return true;
		}
		return false;
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

	public void setHP(float hp, float atTime) {
		if (atTime >= last_hp_update) {
			last_hp_update = atTime;
			forceHP(hp);
		}
	}

	public void forceHP(float hp) {
		this.hp = hp;
		hp_changed=true;
		checkDestroy();
	}

	public void resetHPChanged() {
		hp_changed=false;
	}

	public boolean isAlive() {
		return hp > 0;
	}

	public int hashCode() {
		return id;
	}

	public boolean getHPChanged() {
		return hp_changed;
	}

	public float getHPLastUpdate() {
		return last_hp_update;
	}
}
