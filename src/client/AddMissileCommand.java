package client;

import com.jme.math.Vector3f;

public class AddMissileCommand extends AbstractCommand {
	
	private Vector3f pos;
	private Vector3f dir;
	private int ownerid;

	public AddMissileCommand(Vector3f pos, Vector3f dir, int ownerid) {
		super(0);
		this.pos = pos;
		this.dir = dir;
		this.ownerid=ownerid;
	}

	public void perform(Game game, float delta) {
		game.addMissile(0, pos, dir, ownerid, System.currentTimeMillis());
		//TODO: FIXA FIXA FIXA ATTRIBUT! (0 och currentmillis)
	}

}
