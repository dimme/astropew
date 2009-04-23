package client.command;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

public interface GameCommandInterface {

	void addMissile(int id, Vector3f pos, Vector3f dir, int ownerid, float time);

	void addPlayer(int id, String name, int shipid);

	void destroyObject(int objid);

	void removePlayer(int id);

	void updatePosition(Vector3f pos, Quaternion ort, Vector3f dir, int id, float time);

}
