package client;

import com.jme.math.Vector3f;

public class AddMissileCommand extends AbstractCommand {
	
	private Vector3f pos;
	private Vector3f dir;
	private int ownerid;
	private int id;

	public AddMissileCommand(float time, int id, Vector3f pos, Vector3f dir, int ownerid) {
		super(time);
		this.pos = pos;
		this.dir = dir;
		this.ownerid=ownerid;
		this.id=id;
	}

	public void perform(Game game, float delta) {
		game.addMissile(id, pos, dir, ownerid, time);
	}

}
