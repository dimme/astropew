package server.command;

import common.Player;
import common.world.WorldObject;


public class DestroyCommand extends AbstractCommand {

	private final WorldObject obj;
	private final Player instigator;

	public DestroyCommand(WorldObject obj, Player instigator, float time) {
		super(time);
		this.obj=obj;
		this.instigator = instigator;
	}

	public void perform(GameCommandInterface gci) {
		gci.destroy(obj, instigator);
	}

}
