package common.world;

import com.jme.math.FastMath;
import com.jme.math.Matrix3f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.TriMesh;
import common.GameLogic;
import common.Player;

public class Ship extends MobileObject {

	private static final long serialVersionUID = 1L;

	protected ColorRGBA color;

	private static final float FIRE_INTERVAL = 0.23f;
	private float lastFire = 0;

	public ColorRGBA getColor() {
		return color;
	}

	public Ship(GameLogic logic, int id, Player owner, int subdivides, float creationtime) {
		super(logic, id, "Ship", owner, creationtime);

		owner.setShip(this);

		int c = owner.getName().hashCode();
		color = new ColorRGBA();
		color.fromIntRGBA(c);
		color.a = 1;
		float max = Math.max(color.r, Math.max(color.g, color.b));
		color.r /= max;  color.g /= max;  color.b /= max;

		TriMesh shape = ShipHull.create(subdivides);
		shape.rotateUpTo(Vector3f.UNIT_Z.mult(-1));
		shape.getLocalScale().z = 0.3f;

		Matrix3f incr = new Matrix3f();
		Matrix3f tempMa = new Matrix3f();
		Matrix3f tempMb = new Matrix3f();
        incr.fromAngleNormalAxis(FastMath.PI, Vector3f.UNIT_Y);

        shape.getLocalRotation().fromRotationMatrix(
                incr.mult(shape.getLocalRotation().toRotationMatrix(tempMa),
                        tempMb));
        shape.getLocalRotation().normalize();
		attachChild(shape);
		
		updateGeometricState(0, true);
		updateModelBound();
	}
	
	public void setSpawnPositions(float time) {
		getPosition().set(100*(float)Math.random(), 100*(float)Math.random(), 100*(float)Math.random());
		getLocalTranslation().set(getPosition());
		
		Vector3f z = getPosition();
		Vector3f y = z.cross(Vector3f.UNIT_X);
		Vector3f x = z.cross(y);
		getOrientation().fromAxes(x,y,z);
		getOrientation().normalize();
		getLocalRotation().set(getOrientation());
		
		getMovement().set(0,0,0);
		
		setLastUpdate(time);
	}

	public boolean canFire(float currentTime) {
		return isAlive() && lastFire+FIRE_INTERVAL <= currentTime;
	}

	public void setLastFireTime(float time) {
		lastFire = time;
	}

	protected float actualDamage(float dmg, WorldObject instigator) {
		instigator.getOwner().setPoints(instigator.getOwner().getPoints()+100);
		return dmg;
	}

	protected void destroy(Player instigator) {
		logic.destroy(this, instigator);
	}
	
	public void collidedWith(WorldObject wobj, float time) {
		wobj.takeDamage(100, this, time);
	}
}
