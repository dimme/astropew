package common.world;

import com.jme.math.Matrix3f;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

public abstract class MobileObject extends WorldObject {
	private static final long serialVersionUID = 1L;
	
	protected long lastUpdate;
	
	protected final Vector3f position = new Vector3f();
	protected final Quaternion orientation = new Quaternion();
	protected final Vector3f movement = new Vector3f();
	protected final Matrix3f rotationspeed = new Matrix3f();
	
	public MobileObject(String name) {
		super(name);
	}
	
	public Vector3f getPosition() {
		return position;
	}

	public Quaternion getOrientation() {
		return orientation;
	}

	public Vector3f getMovement() {
		return movement;
	}
	
	public Matrix3f getRotationSpeed() {
		return rotationspeed;
	}
	
	public boolean shouldUpdate(long newPointInTime) {
		if ( lastUpdate > newPointInTime ) {
			return false;
		}
		return true;
	}
	
	public void setLastUpdate(long newPointInTime) {
		lastUpdate = newPointInTime;
	}
	
	/**
	 * Interpolates and sets actual positions for rendering
	 * @param secondsSinceLast seconds since last interpolation
	 */
	protected void interpolate(float secondsSinceLast) {
		orientation.apply(rotationspeed);
		localRotation.set(orientation);
		
		
		localTranslation.addLocal(movement.mult(secondsSinceLast));
	}
	
	public void resetGeometrics() {
		localTranslation.set(position);
		localRotation.set(orientation);
	}
	
	
	public void updateGeometricState(float secondsSinceLast, boolean initiator) {
		interpolate(secondsSinceLast);
		super.updateGeometricState(secondsSinceLast, initiator);
	}
}
