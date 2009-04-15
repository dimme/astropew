package client;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import common.world.Ship;

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

	public void perform(GameLogic logic, Game game) {
		final Ship s = logic.getShip(id);
		if (s != null) {
			s.setLocalTranslation(pos);
			s.setMovement(dir);
			s.setLocalRotation(ort);
		}
	}
}
