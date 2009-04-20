package common.world;

import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.TriMesh;
import com.jme.scene.shape.Pyramid;
import common.Player;

public class Ship extends MobileObject {
	
	private static final long serialVersionUID = 1L;
	
	protected ColorRGBA color;

	public ColorRGBA getColor() {
		return color;
	}

	public Ship(Player owner) {
		super("Ship" + owner.getID());

		this.owner = owner;
		owner.setShip(this);

		int c = owner.getName().hashCode();
		color = new ColorRGBA();
		color.fromIntRGBA(c);
		color.a = 1;
		float max = Math.max(color.r, Math.max(color.g, color.b));
		float mult = 1f/max;
		color.r *= mult;
		color.g *= mult;
		color.b *= mult;

		TriMesh shape = ShipHull.create();
		shape.rotateUpTo(Vector3f.UNIT_Z.mult(-1));
		shape.getLocalScale().z = 0.3f;
		attachChild(shape);
	}

}
