package common.world;

import com.jme.scene.Node;
import common.GameLogic;
import common.Player;

public abstract class WorldObject extends Node {
	public static final int TYPE_SHIP = 0;
	public static final int TYPE_MISSILE = 1;
	public static final int TYPE_OTHER = 2;

	private static final long serialVersionUID = 1L;
	protected final Player owner;
	protected final int id;
	protected final GameLogic logic;

	public static final WorldObject NullWobj = NullWorldObject.instance;

	protected float hp;
	protected boolean hp_changed = false;
	protected float last_hp_update = 0;

	private Player lastInstigator = NoPlayer.instance;

	public WorldObject(GameLogic logic, int id, String name) {
		this(logic, id, NoPlayer.instance, name);
	}

	public WorldObject(GameLogic logic, int id, Player owner, String name) {
		super(name);
		this.owner = owner;
		this.logic = logic;
		this.id = id;
		hp = 100;
	}

	public int getType(){
		return TYPE_OTHER;
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
	public void collidedBy(WorldObject wobj, float time) {

	}

	/**
	 *
	 * @param dmg
	 * @return the actual damage taken.
	 */
	public final float takeDamage(float dmg, WorldObject instigator, float time) {
		float ad = actualDamage(dmg, instigator);
		if (ad != 0) {
			forceHP(hp-ad, instigator.getOwner(), time);
		}
		return ad;
	}

	protected final void checkDestroy(Player instigator) {
		if (hp <= 0) {
			destroy(instigator);
		}
	}

	protected abstract void destroy(Player instigator);

	protected float actualDamage(float dmg, WorldObject instigator) {
		return 0; //no damage as default
	}

	public float getHP() {
		return hp;
	}

	public void setHP(float hp, Player instigator, float atTime) {
		if (atTime >= last_hp_update) {
			forceHP(hp, instigator, atTime);
		}
	}

	public void forceHP(float hp, Player instigator, float atTime) {
		if (this.hp != hp) {
			last_hp_update = atTime;
			lastInstigator = instigator;
			this.hp = hp;
			hp_changed = true;
			checkDestroy(instigator);
		}
	}

	public void resetHPChanged() {
		hp_changed = false;
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

	public Player getLastInstigator() {
		return lastInstigator;
	}
}
