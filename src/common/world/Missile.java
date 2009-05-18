package common.world;

import com.jme.bounding.BoundingSphere;
import com.jme.math.Vector3f;
import com.jme.scene.SharedMesh;
import com.jme.scene.TriMesh;
import com.jme.scene.shape.Sphere;
import common.GameLogic;
import common.Player;

public class Missile extends MobileObject {

	private static final long serialVersionUID = 1L;
	private static final Sphere sphere = new Sphere("MissileSphere", 6, 6, 0.1f );

	public Missile(GameLogic logic, int id, Vector3f pos, Vector3f dir, Player owner, float time) {
		super(ObjectType.Missile, logic, id, "Missile", owner, time);

		TriMesh mesh = new SharedMesh("MissileMesh", sphere);
		mesh.setModelBound(new BoundingSphere(0.1f, Vector3f.ZERO));
		attachChild(mesh);
		orientation.lookAt(dir, dir.cross(Vector3f.UNIT_Y));
		movement.set(dir);
		position.set(pos);
		setLastUpdate(time);

		updateGeometricState(0, true);
		updateModelBound();
	}

	protected void destroy(Player instigator) {
		logic.destroy(this, getOwner());
	}
}
