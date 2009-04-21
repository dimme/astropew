package common.world;

import com.jme.bounding.BoundingBox;
import com.jme.bounding.BoundingSphere;
import com.jme.math.Vector3f;
import com.jme.scene.shape.Sphere;
import common.Player;

public class Missile extends MobileObject {

	private static final long serialVersionUID = 1L;
	
	private final int id;

	public Missile(int id, Vector3f pos, Vector3f dir, Player owner, long time) {
		super("Missile", owner);
		
		this.id = id;
		
		final Sphere shape = new Sphere("Missile", 10, 10, 0.1f );
		shape.setModelBound(new BoundingSphere(0.1f, Vector3f.ZERO));
		attachChild(shape);
		//orientation.lookAt(dir, Vector3f.ZERO);
		movement.set(dir);
		position.set(pos);
		setLastUpdate(time);
		
		updateGeometricState(0, true);
		updateModelBound();
	}

	public int getID() {
		return id;
	}
}
