package common.world;

import com.jme.math.Vector3f;
import com.jme.scene.shape.Sphere;
import common.Player;

public class Missile extends MobileObject {

	private static final long serialVersionUID = 1L;
	
	private final int id;

	public Missile(int id, Vector3f pos, Vector3f dir, Player owner, long time) {
		super("Missile" + id, owner);
		
		this.id = id;
		
		final Sphere shape = new Sphere("Missile", 10, 10, 0.1f );
		attachChild(shape);
		//orientation.lookAt(dir, Vector3f.ZERO);
		movement.set(dir);
		position.set(pos);
		setLastUpdate(time);
	}

	public int getID() {
		return id;
	}
}
