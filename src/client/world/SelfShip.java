package client.world;

import client.Game;
import client.command.MessageCommand;

import com.jme.math.Vector3f;
import common.GameLogic;
import common.Player;
import common.world.NoPlayer;
import common.world.Ship;

public class SelfShip extends Ship {

	private static final long serialVersionUID = 1L;

	private Vector3f tmpv = new Vector3f();
	private final Game game;

	public SelfShip(Game game, GameLogic logic, int id, Player owner, float creationtime) {
		super(logic, id, owner, 3, creationtime);
		this.game = game;
	}

	public void interpolate(float delta, float currentTime) {

		//position:
		Vector3f transl = getLocalTranslation();
		movement.mult(delta, tmpv);
		transl.addLocal(tmpv);

		//TODO: Maybe do something with data from server
	}

	public boolean shouldUpdate(long newPointInTime) {
		return false;
	}

	protected void destroy(Player instigator) {
		super.destroy(instigator);
		if (instigator != NoPlayer.instance) {
			game.addCommand(new MessageCommand("You were killed by " + instigator, game.getLastUpdateTime()));
		} else {
			game.addCommand(new MessageCommand("You died.", game.getLastUpdateTime()));
		}
		game.setPlaying(false);
	}
	
	public void forceHP(float hp, Player instigator, float atTime) {
		super.forceHP(hp, instigator, atTime);
		hull.distort(1f - this.hp * 0.01f);
	}
}
