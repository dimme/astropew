package client.command;

import common.Player;



public class RemovePlayerCommand extends AbstractCommand {
	private final int id;

	public RemovePlayerCommand(int id) {
		super(0);
		this.id = id;
	}

	public void perform(GameCommandInterface gci) {
		Player removed = gci.removePlayer(id);
		gci.addMessage(new Message(removed.getName() + " left the game.", gci.getCurrentTime()));
	}

}
