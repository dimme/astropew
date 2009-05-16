package client.world;

import client.Game;
import client.GameLogic;
import client.command.MessageCommand;

import com.jme.math.Vector3f;
import common.Player;
import common.world.NoPlayer;

public class OtherShip extends common.world.Ship {
	private static final long serialVersionUID = -1L;
	private static final float camFovY = 45.0f;
	private static final float camFovYinv = 1/camFovY;

	private static final float closeBP = 20f;
	private static final float farBP = 50f;

	private final TargetSprite ts;
	private final GameLogic logic;
	private final Game game;

	private final Vector3f diff = new Vector3f();

	public OtherShip(Game game, GameLogic logic, int id, Player owner, float creationtime, TargetSprite ts) {
		super(logic, id, owner, 2, creationtime);
		this.game = game;
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

	public void destroy(Player instigator) {
		super.destroy(instigator);
		if (instigator != NoPlayer.instance) {
			game.addCommand(new MessageCommand(this.getOwner() + " was killed by " + instigator, game.getLastUpdateTime()));
		} else {
			game.addCommand(new MessageCommand(this.getOwner() + " died.", game.getLastUpdateTime()));
		}
	}
	
	public void forceHP(float hp, Player instigator, float atTime) {
		super.forceHP(hp, instigator, atTime);
		hull.distort(1f - this.hp * 0.01f);
	}
}
