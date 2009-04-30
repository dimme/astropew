package client.world;

import com.jme.math.Vector3f;

import client.GameLogic;
import common.Player;

public class OtherShip extends common.world.Ship {
	private static final long serialVersionUID = -1L;
	private static final float camFovY = 45.0f;
	private static final float camFovYinv = 1/camFovY;
	
	private static final float closeBP = 20f;
	private static final float farBP = 50f;
	
	private final TargetSprite ts;
	private GameLogic logic;
	
	private final Vector3f diff = new Vector3f();

	public OtherShip(GameLogic logic, int id, Player owner, float creationtime, TargetSprite ts) {
		super(logic, id, owner, creationtime);
		this.ts = ts;
		this.logic = logic;
	}
	
	public void interpolate(float d, float t) {
		super.interpolate(d,t);
		
		positionTargetSprite();
	}

	private void positionTargetSprite() {
		Vector3f thispos = getWorldTranslation();
		Vector3f selfpos = logic.getSelf().getShip().getWorldTranslation();
		
		selfpos.subtract(thispos, diff);
		
		float len = diff.length();
		
		if ( len > farBP ) {
			float lendiff = len-farBP;
			ts.setLocalScale( 1 + camFovYinv*lendiff );
		} else if ( len > closeBP ) {
			ts.setLocalScale( 1 );
		} else {
			float lendiff = len - closeBP;
			ts.setLocalScale( 1 + camFovYinv*lendiff );
		}
	}

	public void removeTargetSprite() {
		ts.removeFromParent();
	}
}
