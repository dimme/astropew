package client;

public class DestroyObjectCommand extends AbstractCommand {

	private final int objid;
	
	public DestroyObjectCommand(int objid) {
		super(0); //as fast as possible
		this.objid = objid;
	}

	public void perform(Game game, float interpolation) {
		game.destroyObject(objid);
	}

}
