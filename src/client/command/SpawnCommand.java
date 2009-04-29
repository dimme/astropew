package client.command;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;


public class SpawnCommand extends AbstractCommand {

	final int pid;
	final Vector3f pos, dir;
	final Quaternion ort;
	
	
	public SpawnCommand(int playerid, Vector3f pos, Quaternion ort, Vector3f dir, float t) {
		super(t);
		pid = playerid;
		this.pos=pos;
		this.dir=dir;
		this.ort=ort;
	}

	public void perform(GameCommandInterface gci) {
		gci.spawn(pid, pos, ort, dir, time);
	}

	
}
