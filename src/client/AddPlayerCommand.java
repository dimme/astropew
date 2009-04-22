package client;


public class AddPlayerCommand extends AbstractCommand {

	protected final String name;
	protected final int id;
	protected final int shipid;

	public AddPlayerCommand(int id, String name, int shipid) {
		super(0);
		this.name = name;
		this.id = id;
		this.shipid = shipid;
	}

	public void perform(Game game, float delta) {
		game.addPlayer(id,name, shipid);
	}

}
