package common.world;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import common.Player;

public abstract class MobileObject extends WorldObject {
	private static final long serialVersionUID = 1L;
	
	private float lastUpdate;
	
	protected final Vector3f position = new Vector3f();
	protected final Quaternion orientation = new Quaternion();
	protected final Vector3f movement = new Vector3f();
	
	private final Vector3f tmpv = new Vector3f();
	
	public MobileObject(int id, String name, Player owner, float creationtime) {
		super(id, name, owner);
		lastUpdate = creationtime;
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
	
	public boolean shouldUpdate(float newPointInTime) {
		if ( lastUpdate > newPointInTime ) {
			return false;
		}
		return true;
	}
	
	public void setLastUpdate(float newPointInTime) {
		lastUpdate = newPointInTime;
	}
	
	public float getLastUpdate() {
		return lastUpdate;
	}
	
	/**
	 * Interpolates and sets actual positions for rendering
	 */
	public void interpolate(float delta, float time) {
		float deltafromupdate = time - lastUpdate;
		
		
		//We don't interpolate orientation:
		setLocalRotation(orientation);
		
		//position:
		
		Vector3f transl = getLocalTranslation();
		transl.set(position);
		transl.addLocal(movement.mult(deltafromupdate, tmpv));
		
	}
}
