package client.command;



public class RemovePlayerCommand extends AbstractCommand {
	private final int id;

	public RemovePlayerCommand(int id) {
		super(0);
		this.id = id;
	}

	public void perform(GameCommandInterface gci) {
		gci.removePlayer(id);
	}

}
