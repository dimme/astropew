package server;

import java.util.Collection;

import server.clientdb.Client;
import server.clientdb.ClientDB;

import common.OffsetConstants;
import common.ServerPacketType;
import common.Util;
import common.world.Missile;
import common.world.Ship;
import common.world.WorldObject;

public class PacketDataFactory {

	public static byte[] createMessagePacket(byte msgtype, String str) {

		final byte[] sb = str.getBytes();

		final byte[] b = new byte[OffsetConstants.MESSAGE_STRING_OFFSET	+ sb.length];

		b[0] = ServerPacketType.MESSAGE;
		b[1] = 0;
		b[OffsetConstants.MESSAGE_MESSAGE_TYPE_OFFSET] = msgtype;
		Util.put(sb, b, OffsetConstants.MESSAGE_STRING_OFFSET);

		return b;
	}

	public static byte[] createInitializer(long worldseed, int id, int shipid, String name, long ltime, float ftime) {
		final byte[] namebytes = name.getBytes();
		final byte[] b = new byte[OffsetConstants.INITIALIZER_STRING_OFFSET	+ namebytes.length];

		b[0] = ServerPacketType.INITIALIZER;
		b[1] = 0;
		Util.put(worldseed, b, OffsetConstants.INITIALIZER_RANDOM_SEED_OFFSET);
		Util.put(id, b, OffsetConstants.INITIALIZER_ID_OFFSET);
		Util.put(shipid, b, OffsetConstants.INITIALIZER_SHIPID_OFFSET);
		Util.put(ltime, b, OffsetConstants.INITIALIZER_CURRENT_TIME_MILLIS_OFFSET);
		Util.put(ftime, b, OffsetConstants.INITIALIZER_CURRENT_TIME_FLOAT_OFFSET);
		System.out.println("sending ftime " + ftime);
		Util.put(namebytes, b, OffsetConstants.INITIALIZER_STRING_OFFSET);

		return b;
	}

	public static byte[] createPlayerJoined(int id, String name, int shipid) {
		final byte[] sb = name.getBytes();
		final byte[] b = new byte[OffsetConstants.PLAYER_JOINED_STRING_OFFSET
				+ sb.length];

		b[0] = ServerPacketType.PLAYER_JOINED;
		b[1] = 0;
		Util.put(id, b, OffsetConstants.PLAYER_JOINED_ID_OFFSET);
		Util.put(shipid, b, OffsetConstants.PLAYER_JOINED_SHIPID_OFFSET);
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

	public static byte[] createPosition(float time, Collection<Ship> ships) {

		//TODO: Check if the array will fit in a UDP Packet.
		final byte[] b = new byte[ships.size() * OffsetConstants.PLAYER_POSITIONS_ONE_SIZE  + OffsetConstants.PLAYER_POSITIONS_DATA_START];

		b[0] = ServerPacketType.PLAYER_POSITIONS;
		b[1] = 0;
		Util.put(time, b, OffsetConstants.PLAYER_POSITIONS_TIME_OFFSET);

		int offset = OffsetConstants.PLAYER_POSITIONS_DATA_START;

		for(final Ship s: ships) {
			Util.put(s.getOwner().getID(), b, offset + OffsetConstants.PLAYER_POSITIONS_ID_OFFSET);
			Util.put(s.getLocalTranslation(), b, offset+OffsetConstants.PLAYER_POSITIONS_POS_OFFSET);
			Util.put(s.getLocalRotation(), b, offset+OffsetConstants.PLAYER_POSITIONS_ORT_OFFSET);
			Util.put(s.getMovement(), b, offset+OffsetConstants.PLAYER_POSITIONS_DIR_OFFSET);
			Util.put(s.getOwner().getPoints(), b, offset+OffsetConstants.PLAYER_POSITIONS_POINTS_OFFSET);
			offset += OffsetConstants.PLAYER_POSITIONS_ONE_SIZE;
		}

		return b;
	}

	public static byte[] createPlayersInfo(ClientDB cdb, Client Player) {
		int size = OffsetConstants.PLAYERS_INFO_DATA_START;
		byte[][] byteNames;
		int[] ids;
		int[] shipids;
		int i;

		synchronized (cdb) {
			Collection<Client> clients = cdb.getClients();
			byteNames = new byte[clients.size()][];
			ids = new int[clients.size()];
			shipids = new int[clients.size()];
			i = 0;
			for (final Client c : clients) {
				if (!c.equals(Player)) {
					byteNames[i] = c.getName().getBytes();
					ids[i] = c.getID();
					shipids[i] = c.getShip().getID();
					size += OffsetConstants.PLAYERS_DATA_NAME_OFFSET + byteNames[i].length;
					i++;
				}
			}
		}

		final int numberOfClients = i;
		final byte b[] = new byte[size];

		b[0] = ServerPacketType.PLAYERS_INFO;
		b[1] = 0;

		int offset = OffsetConstants.PLAYERS_INFO_DATA_START;
		for (int k = 0; k < numberOfClients; k++) {
			Util.put(ids[k], b, offset + OffsetConstants.PLAYERS_DATA_ID_OFFSET);
			Util.put(shipids[k], b, offset+OffsetConstants.PLAYERS_DATA_SHIPID_OFFSET);
			b[offset+OffsetConstants.PLAYERS_DATA_NAME_LENGTH_OFFSET] = (byte) byteNames[k].length;
			Util.put(byteNames[k], b, offset+OffsetConstants.PLAYERS_DATA_NAME_OFFSET);
			offset += OffsetConstants.PLAYERS_DATA_NAME_OFFSET+byteNames[k].length;
		}

		return b;
	}
	/**
	 * Sent to all clients when a missile is created <br>
	 * ServerPacketType - byte - 1 byte <br>
	 * Sequence Number - byte - 1 byte <br>
	 * Time of Creation - long - 8 bytes <br>
	 * pos - Vector3f - 12 byte <br>
	 * dir - Vector3f - 12 byte <br>
	 */
	public static byte[] createMissile(Missile m) {

		final byte[] b = new byte[OffsetConstants.MISSILE_SIZE];

		b[0] = ServerPacketType.MISSILE;
		b[1] = 0;
		Util.put(m.getLastUpdate(), b, OffsetConstants.MISSILE_TIME_OFFSET);
		Util.put(m.getPosition(), b, OffsetConstants.MISSILE_POS_OFFSET);
		Util.put(m.getMovement(), b, OffsetConstants.MISSILE_DIR_OFFSET);
		Util.put(m.getID(), b, OffsetConstants.MISSILE_ID_OFFSET);
		Util.put(m.getOwner().getID(), b, OffsetConstants.MISSILE_OWNER_OFFSET);

		return b;
	}

	public static byte[] createHPUpdate(WorldObject wobj) {
		final byte[] b = new byte[OffsetConstants.OBJECT_HP_SIZE];
		b[0] = ServerPacketType.OBJECT_HP;
		b[1] = 0;

		Util.put(wobj.getID(), b, OffsetConstants.OBJECT_HP_ID_OFFSET);
		Util.put(wobj.getHP(), b, OffsetConstants.OBJECT_HP_VALUE_OFFSET);
		Util.put(wobj.getHPLastUpdate(), b, OffsetConstants.OBJECT_HP_TIME_OFFSET);

		return b;
	}

	public static byte[] createSpawn(Ship ship) {
		final byte[] b = new byte[OffsetConstants.SPAWN_SIZE];

		b[0] = ServerPacketType.SPAWN;
		b[1] = 0;

		Util.put(ship.getLastUpdate(), b, OffsetConstants.SPAWN_TIME_OFFSET);
		Util.put(ship.getOwner().getID(), b, OffsetConstants.SPAWN_PLAYERID_OFFSET);
		Util.put(ship.getPosition(), b, OffsetConstants.SPAWN_POS_OFFSET);
		Util.put(ship.getOrientation(), b, OffsetConstants.SPAWN_ORT_OFFSET);
		Util.put(ship.getMovement(), b, OffsetConstants.SPAWN_DIR_OFFSET);

		return b;
	}
}
