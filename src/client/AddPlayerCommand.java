package client;

import com.jme.scene.state.MaterialState;
import common.world.Ship;

public class AddPlayerCommand extends AbstractCommand {

	private String name;
	private int id;
	
	public AddPlayerCommand(int id, String name) {
		super(0);
		this.name = name;
		this.id = id;
	}

	public void perform(GameLogic logic, Game game) {
		if (logic.getPlayer(id) != null) {
			return;
		}
		
		Player p = new Player(name, id);
		
		Ship s = new Ship(p);
		MaterialState ms = game.createMaterialState();
		ms.setDiffuse(s.getColor());
		ms.setEmissive(s.getColor().multLocal(0.2f));
		ms.setAmbient(s.getColor().multLocal(0.1f));
		s.setRenderState(ms);
		game.attachToRoot(s);
		logic.addShip(s);
	}

}
