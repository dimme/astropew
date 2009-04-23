package client.command;

import common.world.WorldObject;


public class DestroyObjectCommand extends AbstractCommand {

	private final int objid;
	
	public DestroyObjectCommand(int id) {
		super(0); //as fast as possible
		objid = id;
	}

	public void perform(GameCommandInterface gci) {
		gci.destroyObject(objid);
	}

}
