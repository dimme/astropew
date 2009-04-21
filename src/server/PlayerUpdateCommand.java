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
	
	public PlayerUpdateCommand(SocketAddress sender, Vector3f pos, Quaternion ort, Vector3f dir, long time) {
		super(time);
		this.pos=pos;
		this.ort=ort;
		this.dir=dir;
		this.sender=sender;
	}

	public void perform(Game g, float delta) {
		Client c = g.cdb.getClient(sender);
		if (c != null) {
			Ship s = c.getShip();
			
			if (s.shouldUpdate(time)) {
				s.getPosition().set(pos);
				s.getOrientation().set(ort);
				s.getMovement().set(dir);
				s.setLastUpdate(time);
			}
		}
	}
}
