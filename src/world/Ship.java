package world;

import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Pyramid;


public class Ship extends MobileObject {

	public Ship() {
		super("Ship");
		
		Spatial shape = new Pyramid("ShipPyramid",1,3);
		
		shape.rotateUpTo(Vector3f.UNIT_Z.mult(-1));
		attachChild(shape);
		
		movement = new Vector3f(0,0,-0.1f);
	}

}
