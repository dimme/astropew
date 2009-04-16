package client;

import com.jme.scene.state.MaterialState;

import common.world.Ship;

public class AddPlayerCommand extends AbstractCommand {

	protected final String name;
	protected final int id;

	public AddPlayerCommand(int id, String name) {
		super(0);
		this.name = name;
		this.id = id;
	}

	public void perform(Game game) {
		game.addPlayer(id,name);
	}

}
