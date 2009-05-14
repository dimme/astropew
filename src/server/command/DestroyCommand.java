package server.command;

import common.world.WorldObject;


public class DestroyCommand extends AbstractCommand {

	private final WorldObject obj;
	private final WorldObject instigator;

	public DestroyCommand(WorldObject obj, WorldObject instigator, float time) {
		super(time);
		this.obj=obj;
		this.instigator = instigator;
	}

	public void perform(GameCommandInterface gci) {
		gci.destroy(obj, instigator);
	}

}
