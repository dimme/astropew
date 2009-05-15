package server.command;

import common.world.NoPlayer;
import common.world.WorldObject;

public class SetHPCommand extends AbstractCommand {

	private final WorldObject obj;
	private final float hp;

	public SetHPCommand(WorldObject obj, float hp, float time) {
		super(time);
		this.obj=obj;
		this.hp = hp;
	}

	public void perform(GameCommandInterface gci) {
		if (obj.getParent() != null) {
			obj.setHP(hp, NoPlayer.instance, time);
		}
	}

}