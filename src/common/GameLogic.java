package common;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import common.world.Missile;
import common.world.MobileObject;
import common.world.Ship;
import common.world.Universe;
import common.world.WorldObject;

public abstract class GameLogic {
	protected HashMap<Player, Ship> shiptable = new HashMap<Player, Ship>();
	private final HashSet<MobileObject> mobjs = new HashSet<MobileObject>();
	private final HashMap<Integer, WorldObject> objects = new HashMap<Integer, WorldObject>();

	public GameLogic() {
	}

	public void add(Ship s) {
		shiptable.put(s.getOwner(), s);
		mobjs.add(s);
		_add(s);
	}
	
	public void add(MobileObject mobj) {
		mobjs.add(mobj);
		_add(mobj);
	}
	
	public void add(WorldObject obj) {
		_add(obj);
	}
	
	private void _add(WorldObject obj) {
		objects.put(obj.getID(), obj);
	}

	public Ship remove(Player owner) {
		Ship removed = shiptable.remove(owner);
		remove(removed);
		return removed;
	}
	
	public WorldObject remove(WorldObject obj) {
		if (obj != null) {
			mobjs.remove(obj);
			objects.remove(obj.getID());
		}
		return obj;
	}
	
	public WorldObject remove(int objid) {
		return remove(objects.get(objid));
	}

	public Collection<Ship> getShips() {
		return shiptable.values();
	}
	
	public void interpolate(float delta, float currentTime) {
		for (MobileObject mobj : mobjs) {
			mobj.interpolate(delta, currentTime);
		}
	}
	
	public void handleCollisions(Universe universe) {
		for (MobileObject mobj : mobjs) {
			//System.out.println("Checking collisions for " + mobj);
			Collection<WorldObject> collisions = new LinkedList<WorldObject>();
			universe.findCollisions(mobj, collisions);
			
			for (WorldObject wobj : collisions) {
				mobj.collidedWith(wobj);
				wobj.collidedBy(mobj);
				//System.out.println(mobj + ", owned by " + mobj.getOwner().getName() + ", collided with " + wobj);
			}
		}
	}
}
