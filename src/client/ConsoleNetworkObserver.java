package client;

import java.net.SocketAddress;

import common.OffsetAndSizeConstants;
import common.PacketObserver;
import common.ServerPacketType;
import common.Util;

/**
 *
 * @author jonsturk
 */
public class ConsoleNetworkObserver implements PacketObserver {

	public void packetReceived(byte[] data, SocketAddress saddr) {
		byte unmaskedData = (byte) (data[OffsetAndSizeConstants.PACKET_TYPE_OFFSET] & Util.CONTROLLED_PACKET_UNMASK);
		if (unmaskedData == ServerPacketType.INITIALIZER) {
			String name = new String(data, OffsetAndSizeConstants.INITIALIZER_STRING_OFFSET, data.length - OffsetAndSizeConstants.INITIALIZER_STRING_OFFSET);
			int id = Util.getInt(data, OffsetAndSizeConstants.INITIALIZER_ID_OFFSET);
			long randSeed = Util.getLong(data,OffsetAndSizeConstants.INITIALIZER_RANDOM_SEED_OFFSET);
			System.out.println("Established contact with server. Got id: "+ id +". Got name: "+name+". Got seed: "+ randSeed);
		} else if (unmaskedData == ServerPacketType.PLAYER_LEFT) {
			int lid = Util.getInt(data, OffsetAndSizeConstants.PLAYER_LEFT_ID_OFFSET);
			System.out.println("Player Left. ID = " + lid);
		} else if (unmaskedData == ServerPacketType.PLAYER_JOINED) {
			int nid = Util.getInt(data, OffsetAndSizeConstants.PLAYER_JOINED_ID_OFFSET);
			byte[] bt = new byte[data.length - OffsetAndSizeConstants.PLAYER_JOINED_STRING_OFFSET];
			System.arraycopy(data, OffsetAndSizeConstants.PLAYER_JOINED_STRING_OFFSET, bt, 0, bt.length);
			System.out.println("Player Joined. ID = " + nid + " Name = " + new String(bt));
		} else if (unmaskedData == ServerPacketType.PLAYER_POSITION){
			/*
			System.out.println("Recieved a player_position packet");
			System.out.println("The time was: "+Util.getLong(data, OffsetAndSizeConstants.PLAYER_POSITION_TICK_OFFSET));
			System.out.println("The player was: "+Util.getInt(data, OffsetAndSizeConstants.PLAYER_POSITION_ID_OFFSET));
			Vector3f v = new Vector3f();
			Util.getVector3f(data, OffsetAndSizeConstants.PLAYER_POSITION_POS_OFFSET, v);
			System.out.println(v);
			Util.getVector3f(data, OffsetAndSizeConstants.PLAYER_POSITION_DIR_OFFSET, v);
			System.out.println(v);
			Util.getVector3f(data, OffsetAndSizeConstants.PLAYER_POSITION_ORT_OFFSET, v);
			System.out.println(v);
			*/
		} else if (unmaskedData == ServerPacketType.MESSAGE) {
			System.out.println("Recieved msg: "+new String(data, OffsetAndSizeConstants.MESSAGE_STRING_OFFSET ,data.length - OffsetAndSizeConstants.MESSAGE_STRING_OFFSET));
		} else {
			/*System.out.println("Unhandled packet type: " + unmaskedData + ", length: " + data.length);
			System.out.println("\t" + "Data: " + hex(data));*/
		}
	}
	
	private String hex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			Util.hex(b, sb);
		}
		return sb.toString();
	}

}
