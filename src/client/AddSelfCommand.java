package client;

public class AddSelfCommand extends AddPlayerCommand {

	public AddSelfCommand(int id, String name) {
		super(id, name);
	}

	public void perform(GameLogic logic, Game game) {
		super.perform(logic, game);
		logic.setSelf(id);
	}

}
