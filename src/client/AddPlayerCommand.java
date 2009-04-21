package client;


public class AddPlayerCommand extends AbstractCommand {

	protected final String name;
	protected final int id;

	public AddPlayerCommand(int id, String name) {
		super(0);
		this.name = name;
		this.id = id;
	}

	public void perform(Game game, float delta) {
		game.addPlayer(id,name);
	}

}
