package client;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

public interface Game {

	public abstract void addCommand(Command cmd);
	
	public abstract void addPlayer(int id, String name);

	public abstract void removePlayer(int id);

	public abstract void updatePosition(Vector3f pos, Quaternion ort, Vector3f dir, int id, long tick);
	
	public abstract void startInThread();

	public abstract void addMissile(int id, Vector3f pos, Vector3f dir, int ownerid, long creationtime);
	
	public abstract void fireMissile();

	public abstract long getLastUpdate();
}