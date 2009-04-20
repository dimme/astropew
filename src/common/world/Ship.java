package common.world;

import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.shape.Pyramid;
import common.Player;

public class Ship extends MobileObject {
	private static final long serialVersionUID = 1L;
	protected ColorRGBA color;
	private static int count = 0;

	public ColorRGBA getColor() {
		return color;
	}

	public Ship(Player owner) {
		super("Ship" + count);

		this.owner = owner;
		owner.setShip(this);

		int c = owner.getName().hashCode();
		color = new ColorRGBA(0.5f,0.5f,0.5f,1);
		color.r += (c % 256) / 256f;
		color.g += ((c / 256 ) % 256) / 256f;
		color.b += ((c / 65536) % 256) / 256f;

		Pyramid shape = new Pyramid("ShipPyramid" + count++, 1, 3);
		shape.rotateUpTo(Vector3f.UNIT_Z.mult(-1));
		shape.getLocalScale().z = 0.3f;
		attachChild(shape);
	}

}
