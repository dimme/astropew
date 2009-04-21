
package client;

import com.jme.input.controls.controller.camera.CameraPerspective;
import com.jme.math.Matrix3f;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.scene.Spatial;
import com.jme.scene.Spatial.CullHint;

public class FollowCameraPerspective implements CameraPerspective {
	private Quaternion q;
	private Vector3f v;
    private Matrix3f m;
	
	private Vector3f location;
	private boolean hideSpatialOnActivate;
	
	private CullHint previousCullMode;
	private Vector3f v2;
	
	public FollowCameraPerspective(Vector3f location) {
		this(location, false);
	}
	
	public FollowCameraPerspective(Vector3f location, boolean hideSpatialOnActivate) {
		q = new Quaternion();
		v = new Vector3f();
		v2 = new Vector3f();
        m = new Matrix3f();
		this.location = location;
		this.hideSpatialOnActivate = hideSpatialOnActivate;
	}
	
	public Vector3f getLocation() {
		return location;
	}
	
	public void update(Camera camera, Spatial spatial, float time) {
		q.set(spatial.getWorldRotation());				// Get the spatial's current rotation
		camera.setDirection(q.getRotationColumn(2));	// Match direction to the spatial's
		camera.setLeft(q.getRotationColumn(0));			// Match left to the spatial's
		camera.setUp(q.getRotationColumn(1));			// Match up to the spatial's

		// Update location
		spatial.updateWorldVectors();				// Update this spatial's world coordinates
		v.set(spatial.getWorldTranslation());		// Set the location to the same as our spatial's
		q.set(spatial.getWorldRotation());			// Set the Quaternion value to the spatials' rotation
		v2.set(location);							// Set the values for our location to this temp holder
		q.multLocal(v2);							// Multiply the intended location offset to the rotation (to match our spatial's rotation)
		v.addLocal(v2);								// Add the rotational applied offset to the location
		camera.setLocation(v);						// Set the camera location

		camera.update();
	}

	
	public void setActive(Camera camera, Spatial spatial, boolean active) {
		if ((active) && (hideSpatialOnActivate)) {
			previousCullMode = spatial.getCullHint();
			spatial.setCullHint(CullHint.Always);
		} else if ((!active) && (hideSpatialOnActivate)) {
			spatial.setCullHint(previousCullMode);
		}
	}
}