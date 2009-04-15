package client;

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
		
		Ship s = logic.getSelf().getShip();
		System.out.println("My player is " + logic.getSelf());
		System.out.println("My ship is " + s);
		if(s != null) {
			Vector3f mov = new Vector3f(Vector3f.UNIT_Z);
			mov = mov.mult(delta);
			s.getLocalTranslation().add(mov);
			
			System.out.println(s.getLocalTranslation());
		
			byte[] data = PacketDataFactory.createPlayerUpdate(System.currentTimeMillis(), s);
			this.gc.sender.send(data);
		}
	}

}
