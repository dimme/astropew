package server.command;

import java.net.SocketAddress;


public class ClientJoiningCommand extends AbstractCommand {

	private final String name;
	private final SocketAddress saddr;

	public ClientJoiningCommand(String name, SocketAddress saddr) {
		super(0);
		this.name = name;
		this.saddr = saddr;
	}

	public void perform(GameCommandInterface gci) {
		gci.clientJoining(name, saddr);
	}


}
