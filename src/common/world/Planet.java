package common.world;

import com.jme.bounding.BoundingSphere;
import com.jme.math.Vector3f;
import com.jme.scene.shape.Sphere;
import common.GameLogic;

public class Planet extends WorldObject {

	private static final long serialVersionUID = 1L;
	
	public Planet(GameLogic logic, int id, Vector3f center, int zsamples, int rsamples, float size) {
		super(logic, id, "Planet", NoPlayer.instance);
		
		Sphere s = new Sphere("PlanetSphere", center, zsamples, rsamples, size);
		s.setModelBound( new BoundingSphere(size, center) );
		attachChild(s);
		
		updateGeometricState(0, true);
		updateModelBound();
	}

}
