package server;

import server.clientdb.Client;
import server.clientdb.ClientDB;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import common.world.Ship;


public class PlayerUpdateCommand extends AbstractCommand {

	private Vector3f pos;
	private Quaternion ort;
	private Vector3f dir;
	private int id;
	
	public PlayerUpdateCommand(int id, Vector3f pos, Quaternion ort, Vector3f dir, long time) {
		super(time);
		this.pos=pos;
		this.ort=ort;
		this.dir=dir;
		this.id=id;
	}

	public void perform(ClientDB cdb, float delta) {
		Client c = cdb.getClient(id);
		if (c != null) {
			Ship s = c.getShip();
			
			s.setLocalTranslation(pos);
			s.setLocalRotation(ort);
			s.setMovement(dir, time);
		}
	}

}
