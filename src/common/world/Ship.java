package common.world;

import com.jme.math.FastMath;
import com.jme.math.Matrix3f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Spatial;
import com.jme.scene.TriMesh;
import common.GameLogic;
import common.Player;

public class Ship extends MobileObject {

	private static final long serialVersionUID = 1L;

	protected ColorRGBA color;

	private static final float FIRE_INTERVAL = 0.23f;
	private float lastFire = 0;
	protected final ShipHull hull;

	public ColorRGBA getColor() {
		return color;
	}

	public Ship(GameLogic logic, int id, Player owner, int subdivides, float creationtime) {
		super(ObjectType.Ship, logic, id, "Ship", owner, creationtime);

		owner.setShip(this);

		int c = owner.getName().hashCode();
		color = new ColorRGBA();
		color.fromIntRGBA(c);
		color.a = 1;
		float max = Math.max(color.r, Math.max(color.g, color.b));
		color.r /= max;  color.g /= max;  color.b /= max;
		if (color.r > 0.5 && color.g > 0.5 && color.b > 0.5) {
			if (color.r <= color.g && color.r <= color.b) {
				color.r = 0.5f;
			} else if (color.g < color.b) {
				color.g = 0.5f;
			} else {
				color.b = 0.5f;
			}
		}

		hull = ShipHull.create(subdivides);
		hull.rotateUpTo(Vector3f.UNIT_Z.mult(-1));
		hull.getLocalScale().z = 0.3f;

		Matrix3f incr = new Matrix3f();
		Matrix3f tempMa = new Matrix3f();
		Matrix3f tempMb = new Matrix3f();
        incr.fromAngleNormalAxis(FastMath.PI, Vector3f.UNIT_Y);

        hull.getLocalRotation().fromRotationMatrix(
                incr.mult(hull.getLocalRotation().toRotationMatrix(tempMa),
                        tempMb));
        hull.getLocalRotation().normalize();
		attachChild(hull);

		updateGeometricState(0, true);
		updateModelBound();
	}

	public void setSpawnPositions(float time) {
		getPosition().set(600*(float)Math.random(), 600*(float)Math.random(), 600*(float)Math.random());
		//getPosition().set(0,0,0);
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
		if (instigator.type == ObjectType.Missile) {
			if(hp<=dmg){
				instigator.getOwner().addPoints(200);
			}
			instigator.getOwner().addPoints(100);
		} else if ( instigator.type == ObjectType.Ship) {
			instigator.getOwner().addPoints(-500);
		}
		return dmg;
	}

	protected void destroy(Player instigator) {
		logic.destroy(this, instigator);
	}

	public void collidedWith(WorldObject wobj, float time) {
		wobj.takeDamage(100, this, time);
	}
}
