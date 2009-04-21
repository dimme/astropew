package common.world;

import com.jme.math.Matrix3f;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import common.Player;

public abstract class MobileObject extends WorldObject {
	private static final long serialVersionUID = 1L;
	
	protected long lastUpdate;
	
	protected final Vector3f position = new Vector3f();
	protected final Quaternion orientation = new Quaternion();
	protected final Vector3f movement = new Vector3f();
	
	protected final Matrix3f tmp1 = new Matrix3f();
	protected final Matrix3f tmp2 = new Matrix3f();
	
	protected final Vector3f tmpv = new Vector3f();
	
	public MobileObject(String name, Player owner) {
		super(name, owner);
		lastUpdate = System.currentTimeMillis();
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
	
	public boolean shouldUpdate(long newPointInTime) {
		if ( lastUpdate > newPointInTime ) {
			return false;
		}
		return true;
	}
	
	public void setLastUpdate(long newPointInTime) {
		lastUpdate = newPointInTime;
	}
	
	public long getLastUpdate() {
		return lastUpdate;
	}
	
	/**
	 * Interpolates and sets actual positions for rendering
	 */
	public void interpolate(long currentTime) {
		float delta = 0.001f * (currentTime - lastUpdate);
		
		
		//We don't interpolate orientation:
		setLocalRotation(orientation);
		
		//position:
		Vector3f transl = getLocalTranslation();
		transl.set(position);
		transl.addLocal(movement.mult(delta, tmpv));
	}
}
