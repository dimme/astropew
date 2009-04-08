package common.world;

import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Pyramid;
import common.Player;


public class Ship extends MobileObject {
	private static final long serialVersionUID = 1L;
	public Ship(Player owner) {
		super("Ship");
		
		this.owner = owner;
		
		Spatial shape = new Pyramid("ShipPyramid",1,3);
		
		shape.rotateUpTo(Vector3f.UNIT_Z.mult(-1));
		attachChild(shape);
		
		movement = new Vector3f(0,0,-0.1f);
	}

}
