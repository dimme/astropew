package client;

import com.jme.scene.state.MaterialState;
import common.world.Ship;

public class AddSelfCommand extends AbstractCommand {

	private final String name;
	private final int id;

	public AddSelfCommand(int id, String name) {
		super(0);
		this.name = name;
		this.id = id;
	}

	public void perform(GameLogic logic, Game game) {
		if (logic.getPlayer(id) != null) {
			return;
		}

		final common.Player p = new client.Player(name, id);
		final Ship s = new Ship(p);
		
		final MaterialState ms = game.createMaterialState();
		ms.setDiffuse(s.getColor());
		ms.setEmissive(s.getColor().multLocal(0.2f));
		ms.setAmbient(s.getColor().multLocal(0.1f));
		s.setRenderState(ms);
		game.attachToRoot(s);
		logic.addSelf(id, name);
		
		System.out.println("Doing other stuff...");
	}

}
