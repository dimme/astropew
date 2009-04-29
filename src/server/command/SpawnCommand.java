package server.command;

import common.world.Ship;


public class SpawnCommand extends AbstractCommand{

	final Ship ship;
	
	public SpawnCommand(Ship ship, float time) {
		super(time);
		this.ship=ship;
	}

	public void perform(GameCommandInterface gci) {
		gci.spawn(ship);
	}

}
