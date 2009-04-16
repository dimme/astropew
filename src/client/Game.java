package client;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.scene.state.MaterialState;
import common.Player;

public interface Game {

	public abstract void addCommand(Command cmd);
	
	public abstract void addPlayer(int id, String name);

	public abstract void removePlayer(int id);

	public abstract void updatePosition(Vector3f pos, Quaternion ort, Vector3f dir, int id, long tick);
	
}