package common.world;

import com.jme.math.Vector3f;

public abstract class MobileObject extends WorldObject {
	private static final long serialVersionUID = 1L;

	public MobileObject(String name) {
		super(name);
	}

	protected Vector3f movement = Vector3f.ZERO;

	public Vector3f getMovement() {
		return movement;
	}

	public void setMovement(Vector3f movement, Long tick) {
		if ( checkTick(tick) ){
			this.movement = movement;
		}
	}
}
