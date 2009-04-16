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
	private float ticklength;
	private final Snapshot[] snaps;
	private final float timediff;
	
	public DumbDummySenderGame(GameClient gc) {
		super(gc);
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
		ticklength = 1f / timer.getResolution();
	}
	
	protected synchronized void simpleUpdate() {
		super.simpleUpdate();
		
		final long old = frameTime;
		frameTime = timer.getTime();
		float delta = ticklength * (frameTime - old);
		float time = ticklength * frameTime;
		
		common.Player self = logic.getSelf();
		if(self != null) {
			Ship s = self.getShip();
			
			Snapshot oldshot = snaps[(int)(time/timediff)%snaps.length];
			Snapshot newshot = snaps[(int)(time/timediff+1)%snaps.length];
			
			Snapshot shot = oldshot.interpolate(newshot, time, timediff);
			
			s.setLocalTranslation(shot.pos);
			s.setLocalRotation(shot.ort);
			s.setMovement(shot.dir, Long.MAX_VALUE);
		
			byte[] data = PacketDataFactory.createPlayerUpdate(System.currentTimeMillis(), s);
			try {
				this.gc.sender.send(data);
			} catch (RejectedExecutionException e) {
				Logger.getLogger(getClass().getName()).log(Level.INFO, "Rejected execution of send task: This is NOT a problem if you were shutting down.");
			}
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

		public Snapshot interpolate(Snapshot newshot, float time, float timediff) {
			time = time % timediff;
			Vector3f p = pos.add( newshot.pos.subtract(pos).mult(time/timediff) );
			Quaternion o = ort.add( newshot.ort.subtract(ort).mult(time/timediff) );
			Vector3f d = dir.add( newshot.dir.subtract(dir).mult(time/timediff) );
			
			return new Snapshot(p, o, d);
		}
	}
	
}
