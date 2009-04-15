package client;

import com.jme.math.Vector3f;
import common.world.Ship;

public class UpdatePositionCommand extends AbstractCommand {

	private final Vector3f pos;
	private final Vector3f dir;
	private final Vector3f ort;
	private final int id;

	public UpdatePositionCommand(int id, Vector3f pos, Vector3f dir,
			Vector3f ort, long tick) {
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
			// TODO: orientation!
		}
	}
}
