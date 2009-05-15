package client.command;


public class MessageCommand extends AbstractCommand {

	private final String msg;

	public MessageCommand(String msg, float time) {
		super(time);
		this.msg = msg;
	}

	public final void perform(GameCommandInterface gci) {
		gci.addMessage(new Message(msg, time));
	}
}
