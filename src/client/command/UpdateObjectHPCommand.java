package client.command;



public class UpdateObjectHPCommand extends AbstractCommand {

	private final int objid;
	private final float hp;

	public UpdateObjectHPCommand(int id, float hp, float time) {
		super(time);
		objid = id;
		this.hp = hp;
	}

	public void perform(GameCommandInterface gci) {
		gci.updateObjectHP(objid, hp, time);
	}

}
