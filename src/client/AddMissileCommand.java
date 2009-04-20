package client;

import com.jme.math.Vector3f;

public class AddMissileCommand extends AbstractCommand {
	
	private Vector3f pos;
	private Vector3f dir;

	public AddMissileCommand(Vector3f pos, Vector3f dir) {
		super(0);
		this.pos = pos;
		this.dir = dir;
	}

	public void perform(Game game) {
		game.addMissile(pos, dir);
	}

}
