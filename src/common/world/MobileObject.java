package common.world;

import com.jme.math.Vector3f;


public abstract class MobileObject extends WorldObject {
	
	public MobileObject(String name) {
		super(name);
	}
	
	protected Vector3f movement = Vector3f.ZERO;
	
	public Vector3f getMovement() {
		return movement;
	}
}
