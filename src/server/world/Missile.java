package server.world;

import com.jme.math.Vector3f;

import common.GameLogic;
import common.Player;
import common.world.WorldObject;

public class Missile extends common.world.Missile {
	private static final long serialVersionUID = 1L;
	
	public Missile(GameLogic logic, int id, Vector3f pos, Vector3f dir, Player owner, float time) {
		super(logic, id, pos, dir, owner, time);
	}

	public void collidedWith(WorldObject wobj) {
		if (getOwner() != wobj.getOwner()) {
			System.out.println("Missile (" + getID() + ") collided with " + wobj);
			setIsCollidable(false);
			wobj.takeDamage(10);
			destroy();
		}
	}
}
