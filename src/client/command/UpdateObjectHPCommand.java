package client.command;

import common.world.WorldObject;


public class UpdateObjectHPCommand extends AbstractCommand {

	private final int objid;
	private final float hp;
	
	public UpdateObjectHPCommand(int id, float hp) {
		super(0); //as fast as possible
		objid = id;
		this.hp = hp;
	}

	public void perform(GameCommandInterface gci) {
		gci.updateObjectHP(objid, hp);
	}

}
