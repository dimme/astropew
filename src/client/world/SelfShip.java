package client.world;

import com.jme.math.Vector3f;

import common.Player;
import common.world.Ship;

public class SelfShip extends Ship {

	private static final long serialVersionUID = 1L;
	
	private Vector3f tmpv = new Vector3f();

	public SelfShip(int id, Player owner, float creationtime) {
		super(id, owner, creationtime);
	}

	public void interpolate(float delta, float currentTime) {
		
		//position:
		Vector3f transl = getLocalTranslation();
		movement.mult(delta, tmpv);
		transl.addLocal(tmpv);
		
		//TODO: Maybe do something with data from server
	}
	
	public boolean shouldUpdate(long newPointInTime) {
		return false;
	}
}
