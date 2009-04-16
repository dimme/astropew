package client;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

public class UpdatePositionCommand extends AbstractCommand {

	private final Vector3f pos;
	private final Quaternion ort;
	private final Vector3f dir;
	private final int id;

	public UpdatePositionCommand(int id, Vector3f pos, Quaternion ort, Vector3f dir, long tick) {
		super(tick);
		this.id = id;
		this.pos = pos;
		this.dir = dir;
		this.ort = ort;
	}

	public void perform(Game game) {
		game.updatePosition(pos, ort, dir, id, id);
	}
}
