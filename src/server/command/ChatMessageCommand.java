package server.command;

import java.net.SocketAddress;


public class ChatMessageCommand extends AbstractCommand {
	
	private final SocketAddress from;
	private final String msg;
	
	public ChatMessageCommand(SocketAddress sender, String msg) {
		super(0);
		from = sender;
		this.msg = msg;
	}

	public void perform(GameCommandInterface gci) {
		gci.relayChatMessage(from, msg);
	}
}
