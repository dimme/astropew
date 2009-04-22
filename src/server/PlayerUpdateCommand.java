package server;

import java.net.SocketAddress;

import server.clientdb.Client;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import common.world.Ship;


public class PlayerUpdateCommand extends AbstractCommand {

	private Vector3f pos;
	private Quaternion ort;
	private Vector3f dir;
	private SocketAddress sender;
	
	public PlayerUpdateCommand(SocketAddress sender, Vector3f pos, Quaternion ort, Vector3f dir, float time) {
		super(time);
		this.pos=pos;
		this.ort=ort;
		this.dir=dir;
		this.sender=sender;
	}

	public void perform(GameCommandInterface gci) {
		gci.playerUpdate(pos, ort, dir, time, sender);
	}
}
