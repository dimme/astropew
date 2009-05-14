package client.command;

import java.util.Formatter;
import java.util.HashMap;

import common.MessageType;


public class MessageCommand extends AbstractCommand {

	private static final HashMap<Byte, String> messages = new HashMap<Byte, String>();
	private static final StringBuilder fmtout = new StringBuilder();
	private static final Formatter fmt = new Formatter(fmtout);
	
	private final byte msgtype;
	private final int[] ids;
	private final String str;
	
	public MessageCommand(byte msgtype, int[] ids, String str, float time) {
		super(time);
		this.msgtype=msgtype;
		this.ids = ids;
		this.str=str;
	}

	public void perform(GameCommandInterface gci) {
		Object[] obj = new Object[ids.length+1];
		for (int i=0; i<ids.length; i++) {
			obj[i] = ids[i];
		}
		obj[ids.length] = str;
		fmt.format(getMessageString(msgtype), obj);
		
		gci.addMessage(new Message(fmtout.toString(), time));
		fmtout.delete(0, fmtout.length());
	}
	
	private static String getMessageString(byte msgtype) {
		if (messages.isEmpty()) {
			messages.put(MessageType.CHAT, "%s");
			messages.put(MessageType.KILLED, "%s was killed by %s");
		}
		
		return messages.get(msgtype);
	}
}
