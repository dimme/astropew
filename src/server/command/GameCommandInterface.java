package server.command;

import java.net.SocketAddress;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import common.world.WorldObject;

public interface GameCommandInterface {
	public void fireMissile(SocketAddress sender, float time);
	public void clientJoining(String name, SocketAddress sender);
	public void clientLeaving(SocketAddress sender);
	public void playerUpdate(Vector3f pos, Quaternion ort, Vector3f dir, float time, SocketAddress sender);
	public void destroy(WorldObject obj);
}
