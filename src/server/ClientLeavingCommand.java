package server;

import java.net.SocketAddress;

public class ClientLeavingCommand extends AbstractCommand {

	private final SocketAddress saddr;
	
	public ClientLeavingCommand(SocketAddress saddr) {
		super(0);
		this.saddr=saddr;
	}
	
	public void perform(Game g, float delta) {
		g.clientLeaving(saddr);
	}
}
