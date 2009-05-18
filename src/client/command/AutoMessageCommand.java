package client.command;

import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;

import client.Game;
import client.GameLogic;
import common.MessageType;


public class AutoMessageCommand extends MessageCommand {

	private static final HashMap<Byte, String> messages = new HashMap<Byte, String>();
	private static final StringBuilder fmtout = new StringBuilder();
	private static final Formatter fmt = new Formatter(fmtout);
	private static Object[] formatData = new Object[3];

	public AutoMessageCommand(Game game, byte msgtype, int[] ids, String str, float time) {
		super(createMessageString(game, msgtype, ids, str), time);

	}

	private static String createMessageString(Game game, byte msgtype, int[] ids, String str) {
		if (formatData.length < ids.length+1) {
			formatData = new Object[2*(ids.length+1)];
		}
		for (int i=0; i<ids.length; i++) {
			formatData[i] = game.getPlayer(ids[i]);
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
			messages.put(MessageType.CHAT, "%s: %s");
		}

		return messages.get(msgtype);
	}
}
