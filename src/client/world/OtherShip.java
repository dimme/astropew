package client.world;

import com.jme.math.Vector3f;

import client.GameLogic;
import common.Player;

public class OtherShip extends common.world.Ship {
	private static final long serialVersionUID = -1L;
	
	private final TargetSprite ts;
	private GameLogic logic;

	public OtherShip(GameLogic logic, int id, Player owner, float creationtime, TargetSprite ts) {
		super(logic, id, owner, creationtime);
		this.ts = ts;
		this.logic = logic;
	}
	
	public void interpolate(float d, float t) {
		super.interpolate(d,t);
		
		Vector3f thispos = getWorldTranslation();
		Vector3f selfpos = logic.getSelf().getShip().getWorldTranslation();
		
		thispos.subtract(selfpos, ts.center);
		ts.center.normalizeLocal();
		//TODO: Position TargetSprite;
	}

	public void removeTargetSprite() {
		ts.removeFromParent();
	}
}
