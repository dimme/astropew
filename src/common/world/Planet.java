package common.world;

import com.jme.bounding.BoundingSphere;
import com.jme.math.Vector3f;
import com.jme.scene.shape.Sphere;
import common.GameLogic;
import common.Player;

public class Planet extends WorldObject {

	private static final long serialVersionUID = 1L;

	public Planet(GameLogic logic, int id, Vector3f center, int zsamples, int rsamples, float size) {
		super(ObjectType.Planet, logic, id, "Planet");

		Sphere s = new Sphere("PlanetSphere", center, zsamples, rsamples, size);
		s.setModelBound( new BoundingSphere(size, center) );
		attachChild(s);

		updateGeometricState(0, true);
		updateModelBound();
	}

	protected void destroy(Player instigator) {
	}

	public void collidedBy(WorldObject wobj, float time) {
		if (getOwner() != wobj.getOwner()) {
			if(wobj.type == ObjectType.Ship){
				wobj.getOwner().addPoints(-1000);
			}
			wobj.forceHP(0, this.getOwner(), time);
		}
	}

}
