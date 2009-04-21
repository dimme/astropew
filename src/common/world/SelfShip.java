package common.world;

import com.jme.math.Vector3f;

import common.Player;

public class SelfShip extends Ship {

	private static final long serialVersionUID = 1L;
	
	private long lastFrame;

	public SelfShip(Player owner) {
		super(owner);
		lastFrame = System.currentTimeMillis();
	}

	public void interpolate(long currentTime) {
		float delta = 0.001f * (currentTime - lastFrame);
		lastFrame = currentTime;
		
		//position:
		Vector3f transl = getLocalTranslation();
		movement.mult(delta, tmpv);
		transl.addLocal(tmpv);
		
		//TODO: Maybe do something with data from server
	}
}
