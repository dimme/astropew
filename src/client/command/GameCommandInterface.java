package client.command;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import common.Player;

public interface GameCommandInterface {

	void addMissile(int id, Vector3f pos, Vector3f dir, int ownerid, float time);

	void addPlayer(int id, String name, int shipid);

	void updateObjectHP(int objid, int instigatorpid, float hp, float time);

	Player removePlayer(int id);

	void updatePosition(Vector3f pos, Quaternion ort, Vector3f dir, int id, float time);
	void updatePoints(int pid, int points);

	void spawn(int playerid, Vector3f pos, Quaternion ort, Vector3f dir, float time);

	void addMessage(Message m);

	float getCurrentTime();
}
