package server;

import common.world.WorldObject;


public class DestroyCommand extends AbstractCommand {

	private final WorldObject obj;
	
	public DestroyCommand(WorldObject obj, float time) {
		super(time);
		this.obj=obj;
	}
	
	public void perform(GameCommandInterface gci) {
		gci.destroy(obj);
	}

}
