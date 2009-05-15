package client.command;

import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;

import common.MessageType;


public class AutoMessageCommand extends MessageCommand {

	private static final HashMap<Byte, String> messages = new HashMap<Byte, String>();
	private static final StringBuilder fmtout = new StringBuilder();
	private static final Formatter fmt = new Formatter(fmtout);
	private static Object[] formatData = new Object[3];
	
	public AutoMessageCommand(byte msgtype, int[] ids, String str, float time) {
		super(createMessageString(msgtype, ids, str), time);
		
	}
	
	private static String createMessageString(byte msgtype, int[] ids, String str) {
		if (formatData.length < ids.length+1) {
			formatData = new Object[2*(ids.length+1)];
		}
		for (int i=0; i<ids.length; i++) {
			formatData[i] = ids[i]; //TODO: Fetch actual object? Not needed until we actually use the Message Packet type. :)
		}
		formatData[ids.length] = str;
		
		fmt.format(getFormatString(msgtype), formatData);
		
		String returned = fmtout.toString();
		
		fmtout.delete(0, fmtout.length());
		Arrays.fill(formatData, null);
		
		return returned; 
	}
	
	private static String getFormatString(byte msgtype) {
		if (messages.isEmpty()) {
			messages.put(MessageType.CHAT, "%s");
		}
		
		return messages.get(msgtype);
	}
}
