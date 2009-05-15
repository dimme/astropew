package client.command;

import java.util.logging.Level;
import java.util.logging.Logger;

import common.world.WorldObject;



public class UpdateObjectHPCommand extends AbstractCommand {

	private final int objid;
	private final int instigatorid;
	private final float hp;

	public UpdateObjectHPCommand(int id, int instigatorid, float hp, float time) {
		super(time);
		objid = id;
		this.hp = hp;
		this.instigatorid= instigatorid;
	}

	public void perform(GameCommandInterface gci) {
		gci.updateObjectHP(objid, instigatorid, hp, time);
	}

}
