package common.world;
import common.Player;

import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.shape.Pyramid;



public class Ship extends MobileObject {
	private static final long serialVersionUID = 1L;
	protected ColorRGBA color;
	
	public ColorRGBA getColor() {
		return color;
	}

	public Ship(Player owner) {
		super("Ship");
		
		this.owner = owner;
		
		color = ColorRGBA.red;
		
		Pyramid shape = new Pyramid("ShipPyramid",1,3);
		
		shape.rotateUpTo(Vector3f.UNIT_Z.mult(-1));
		attachChild(shape);
		
		movement = new Vector3f(0,0,-0.1f);
	}

}
