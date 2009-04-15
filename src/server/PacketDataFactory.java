package server;

import java.util.Collection;

import server.clientdb.Client;
import server.clientdb.ClientDB;

import common.OffsetConstants;
import common.ServerPacketType;
import common.Util;
import common.world.Ship;

public class PacketDataFactory {

	public static byte[] createMessagePacket(byte msgtype, String str) {

		final byte[] sb = str.getBytes();

		final byte[] b = new byte[OffsetConstants.MESSAGE_STRING_OFFSET
				+ sb.length];

		b[0] = ServerPacketType.MESSAGE;
		b[1] = 0;
		b[OffsetConstants.MESSAGE_MESSAGE_TYPE_OFFSET] = msgtype;
		Util.put(sb, b, OffsetConstants.MESSAGE_STRING_OFFSET);

		return b;
	}

	public static byte[] createInitializer(long worldseed, int id, String name) {
		final byte[] namebytes = name.getBytes();
		final byte[] b = new byte[OffsetConstants.INITIALIZER_STRING_OFFSET
				+ namebytes.length];

		b[0] = ServerPacketType.INITIALIZER;
		b[1] = 0;
		Util.put(worldseed, b, OffsetConstants.INITIALIZER_RANDOM_SEED_OFFSET);
		Util.put(id, b, OffsetConstants.INITIALIZER_ID_OFFSET);
		Util.put(namebytes, b, OffsetConstants.INITIALIZER_STRING_OFFSET);

		return b;
	}

	public static byte[] createPlayerJoined(int id, String name) {
		final byte[] sb = name.getBytes();
		final byte[] b = new byte[OffsetConstants.PLAYER_JOINED_STRING_OFFSET
				+ sb.length];

		b[0] = ServerPacketType.PLAYER_JOINED;
		b[1] = 0;
		Util.put(id, b, OffsetConstants.PLAYER_JOINED_ID_OFFSET);
		Util.put(sb, b, OffsetConstants.PLAYER_JOINED_STRING_OFFSET);

		return b;
	}

	public static byte[] createPlayerLeft(int id) {
		final byte[] b = new byte[OffsetConstants.PLAYER_LEFT_SIZE];
		b[0] = ServerPacketType.PLAYER_LEFT;
		b[1] = 0;
		Util.put(id, b, OffsetConstants.PLAYER_LEFT_ID_OFFSET);
		return b;
	}

	public static byte[] createPosition(long time, Collection<Ship> ships) {
		
		
		//TODO: Är detta inom ramen för ett udp paket?
		final byte[] b = new byte[ships.size() * 44  + 10];
		
		b[0] = ServerPacketType.PLAYER_POSITIONS;
		b[1] = 0;
		Util.put(time, b, OffsetConstants.PLAYER_POSITIONS_TICK_OFFSET);
		
		int offset = 10;
		
		for(final Ship s: ships){
			Util.put(s.getOwner().getID(), b, offset);
			offset += 4;
			Util.put(s.getLocalTranslation(), b, offset);
			offset += 12;
			Util.put(s.getLocalRotation(), b, offset);
			offset += 16;
			Util.put(s.getMovement(), b, offset);
			offset += 12;
		}
		
		return b;
	}

	public static byte[] createPlayersInfo(ClientDB cdb, Client Player) {
		int size = 2;
		byte[][] byteNames;
		int[] ids;
		int i;

		synchronized (cdb) {
			byteNames = new byte[cdb.getClients().size()][];
			ids = new int[cdb.getClients().size()];
			i = 0;
			for (final Client c : cdb.getClients()) {
				if (!c.equals(Player)) {
					byteNames[i] = c.getName().getBytes();
					ids[i] = c.getID();
					size += 5 + byteNames[i].length;
					i++;
				}
			}
		}

		final int numberOfClients = i;
		final byte b[] = new byte[size];

		b[0] = ServerPacketType.PLAYERS_INFO;
		b[1] = 0;

		int offset = 2;
		for (int k = 0; k < numberOfClients; k++) {
			Util.put(ids[k], b, offset);
			offset += 4;
			b[offset] = (byte) byteNames[k].length;
			offset++;
			Util.put(byteNames[k], b, offset);
			offset += byteNames[k].length;
		}

		return b;
	}
}
