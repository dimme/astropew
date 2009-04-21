package common.world;

import com.jme.math.Vector3f;

import common.Player;

public class SelfShip extends Ship {

	private static final long serialVersionUID = 1L;

	public SelfShip(Player owner) {
		super(owner);
	}

	public void interpolate(float delta, long currentTime) {
		
		//position:
		Vector3f transl = getLocalTranslation();
		movement.mult(delta, tmpv);
		transl.addLocal(tmpv);
		
		//TODO: Maybe do something with data from server
	}
}
