package common.world;

import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.shape.Pyramid;
import common.Player;

public class Ship extends MobileObject {
	private static final long serialVersionUID = 1L;
	protected ColorRGBA color;

	public ColorRGBA getColor() {
		return color;
	}

	public Ship(Player owner) {
		super("Ship");

		this.owner = owner;
		owner.setShip(this);

		color = ColorRGBA.red;

		final Pyramid shape = new Pyramid("ShipPyramid", 1, 3);
		shape.rotateUpTo(Vector3f.UNIT_Z.mult(-1));
		shape.getLocalScale().z = 0.3f;
		attachChild(shape);

		movement.set(0, 0, -1f);
	}

}
