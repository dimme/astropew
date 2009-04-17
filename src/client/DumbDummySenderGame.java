package client;

import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme.math.FastMath;
import com.jme.math.Matrix3f;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import common.world.Ship;

public class DumbDummySenderGame extends ObserverGame {

	private long frameTime = 0;
	private final Snapshot[] snaps;
	private final double timediff;
	
	public DumbDummySenderGame(int id, String name, GameClient gc) {
		super(id, name, gc);
		snaps = new Snapshot[100];
		timediff = 0.2f;
		double ang;
		
		for (int i=0; i<snaps.length; i++) {
			ang = i * 8 * Math.PI / snaps.length;
			Vector3f pos = new Vector3f(0, 4*(float)Math.sin(ang), -(float)ang);
			Quaternion ort;
			Vector3f dir = new Vector3f(10,0,0);
			if (i == 0) {
				//ort = new Quaternion((float)Math.random(),(float)Math.random(),(float)Math.random(),(float)Math.random());
				ort = new Quaternion(new float[] {0,FastMath.HALF_PI,0});
			} else {
				Matrix3f rot = new Matrix3f();
				ort = new Quaternion(snaps[i-1].ort);
				rot.fromAngleNormalAxis(0.2f, Vector3f.UNIT_Z);
				ort.apply(rot);
			}
			snaps[i] = new Snapshot(pos,ort,dir);
		}
	}
	
	protected void simpleInitGame() {
		super.simpleInitGame();
	}
	
	protected void simpleRender() {
		super.simpleRender();
		Ship s = self.getShip();
		byte[] data = PacketDataFactory.createPlayerUpdate(System.currentTimeMillis(), s);
		try {
			gc.sender.send(data);
		} catch (RejectedExecutionException e) {
			Logger.getLogger(getClass().getName()).log(Level.INFO, "Rejected execution of send task: This is NOT a problem if you were shutting down.");
		}
	}
	
	protected synchronized void simpleUpdate() {
		super.simpleUpdate();
		
		frameTime = System.currentTimeMillis();
		double time = 0.001 * frameTime;
		
		Ship s = self.getShip();
		
		double location = (time / timediff) % snaps.length;
		int old = (int)location;
		int next = old==snaps.length-1 ? 0 : old+1;
		
		Snapshot oldshot = snaps[old];
		Snapshot newshot = snaps[next];
		
		Snapshot shot = oldshot.interpolate(newshot, (float)(location%1));
		
		if (s.shouldUpdate(frameTime)) {
			s.getPosition().set(shot.pos);
			s.getOrientation().set(shot.ort);
			s.getMovement().set(shot.dir);
			s.resetGeometrics();
			s.setLastUpdate(frameTime);
		}
	}

	private class Snapshot {
		Vector3f pos;
		Quaternion ort;
		Vector3f dir;
		
		public Snapshot(Vector3f pos, Quaternion ort, Vector3f dir) {
			super();
			this.dir = dir;
			this.ort = ort;
			this.pos = pos;
		}

		public Snapshot interpolate(Snapshot newshot, float location) {
			Vector3f p = pos.add( newshot.pos.subtract(pos).mult(location) );
			Quaternion o = ort.add( newshot.ort.subtract(ort).mult(location) );
			Vector3f d = dir.add( newshot.dir.subtract(dir).mult(location) );
			
			return new Snapshot(p, o, d);
		}
	}
	
}
