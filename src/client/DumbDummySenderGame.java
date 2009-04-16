package client;

import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

import common.world.Ship;

public class DumbDummySenderGame extends Game {

	private long frameTime = 0;
	private float ticklength;
	private Snapshot[] snaps;
	
	public DumbDummySenderGame(GameClient gc) {
		super(gc);
		snaps = new Snapshot[20];
		double ang;
		for (int i=0; i<snaps.length; i++) {
			ang = i * 8 * Math.PI / snaps.length;
			snaps[i] = new Snapshot(
					new Vector3f(0, 4*(float)Math.sin(ang), -(float)ang),
					new Quaternion(),
					new Vector3f());
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
		
		common.Player self = logic.getSelf();
		if(self != null) {
			Ship s = self.getShip();
			
			long time = System.currentTimeMillis();
			Snapshot oldshot = snaps[(int)(time/1000)%snaps.length];
			Snapshot newshot = snaps[(int)(time/1000+1)%snaps.length];
			
			Snapshot shot = oldshot.interpolate(newshot, time, 1000);
			
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

		public Snapshot interpolate(Snapshot newshot, long time, long timediff) {
			time = time % timediff;
			Vector3f p = pos.add( newshot.pos.subtract(pos).mult(time/(float)timediff) );
			
			return new Snapshot(p, ort, dir);
		}
	}
	
}
