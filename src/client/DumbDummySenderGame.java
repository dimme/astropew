package client;

import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme.math.Vector3f;

import common.world.Ship;

public class DumbDummySenderGame extends Game {

	private long frameTime = 0;
	private float ticklength;
	
	public DumbDummySenderGame(GameClient gc) {
		super(gc);
		
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
			Vector3f mov = new Vector3f(Vector3f.UNIT_Z);
			mov = mov.mult(-delta);
			s.getLocalTranslation().addLocal(mov);
		
			byte[] data = PacketDataFactory.createPlayerUpdate(System.currentTimeMillis(), s);
			try {
				this.gc.sender.send(data);
			} catch (RejectedExecutionException e) {
				Logger.getLogger(getClass().getName()).log(Level.INFO, "Rejected execution of send task: This is NOT a problem if you were shutting down.");
			}
		}
	}

}
