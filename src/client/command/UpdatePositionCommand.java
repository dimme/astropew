package client.command;


import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

public class UpdatePositionCommand extends AbstractCommand {

	private final Vector3f pos;
	private final Quaternion ort;
	private final Vector3f dir;
	private final int id;
	private final int points;

	public UpdatePositionCommand(int id, Vector3f pos, Quaternion ort, Vector3f dir, int points, float time) {
		super(time);
		this.id = id;
		this.pos = pos;
		this.dir = dir;
		this.ort = ort;
		this.points = points;
	}

	public void perform(GameCommandInterface gci) {
		gci.updatePosition(pos, ort, dir, id, time);
		gci.updatePoints(id, points);
	}
}
