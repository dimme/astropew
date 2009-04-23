package server.command;

import java.net.SocketAddress;


public class FireMissileCommand extends AbstractCommand {
		
	SocketAddress sender;
	
	public FireMissileCommand(SocketAddress sender, float t) {
		super(t);
		this.sender = sender;
	}

	public void perform(GameCommandInterface gci) {
		gci.fireMissile(sender, time);
	}
}
