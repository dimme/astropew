package server.world;

import server.Game;
import server.command.DestroyCommand;

import com.jme.math.Vector3f;
import common.Player;
import common.world.WorldObject;

public class Missile extends common.world.Missile {
	private static final long serialVersionUID = 1L;
	
	private final Game game;
	
	public Missile(Game game, int id, Vector3f pos, Vector3f dir, Player owner, float time) {
		super(id, pos, dir, owner, time);
		this.game = game;
	}

	public void collidedWith(WorldObject wobj) {
		if (getOwner() != wobj.getOwner()) {
			System.out.println("Missile " + getID() + " collided with " + wobj);
			setIsCollidable(false);
			game.addCommand(new DestroyCommand(this, game.getFrameTime()));
		}
	}
}
