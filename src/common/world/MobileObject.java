package common.world;

import com.jme.math.Vector3f;

public abstract class MobileObject extends WorldObject {
	private static final long serialVersionUID = 1L;
	protected Vector3f movement = Vector3f.ZERO;
	protected long lastUpdate;
	
	public MobileObject(String name) {
		super(name);
	}


	public Vector3f getMovement() {
		return movement;
	}

	public void setMovement(Vector3f movement, Long pointInTime) {
		if ( checkTick(pointInTime) ){
			this.movement = movement;
		}
	}
	
	public boolean checkTick(long pointInTime){
		if ( lastUpdate > pointInTime ) {
			return false;
		}
		lastUpdate = pointInTime;
		return true;
	}
}
