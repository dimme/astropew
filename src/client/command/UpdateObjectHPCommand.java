package client.command;




public class UpdateObjectHPCommand extends AbstractCommand {

	private final int objid;
	private final int instigatorpid;
	private final float hp;

	public UpdateObjectHPCommand(int id, int instigatingplayerid, float hp, float time) {
		super(time);
		objid = id;
		this.hp = hp;
		this.instigatorpid= instigatingplayerid;
	}

	public void perform(GameCommandInterface gci) {
		gci.updateObjectHP(objid, instigatorpid, hp, time);
	}

}
