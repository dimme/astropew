package client;

import java.net.SocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme.math.Vector3f;

import common.GameException;
import common.OffsetAndSizeConstants;
import common.ServerPacketType;
import common.PacketObserver;
import common.Util;

public class GamePlayObserver implements PacketObserver {
	
	private GameClient client;
	private Game game;
	private Player self = null;
	
	public GamePlayObserver(GameClient client, Game game) {
		this.client = client;
		this.game = game;
	}
	
	public void packetReceived(byte[] data, SocketAddress addr) throws GameException {
		byte unmaskedData = (byte) (data[OffsetAndSizeConstants.PACKET_TYPE_OFFSET] & Util.CONTROLLED_PACKET_UNMASK);
		if(unmaskedData == ServerPacketType.INITIALIZER) {
			String name = new String(data, OffsetAndSizeConstants.INITIALIZER_STRING_OFFSET, data.length - OffsetAndSizeConstants.INITIALIZER_STRING_OFFSET);
			int id = Util.getInt(data, OffsetAndSizeConstants.INITIALIZER_ID_OFFSET);
			game.addPlayer(id, name);
			client.initialized(addr);
		} else if(unmaskedData == ServerPacketType.PLAYER_JOINED) {
			String name = new String(data, OffsetAndSizeConstants.PLAYER_JOINED_STRING_OFFSET, data.length - OffsetAndSizeConstants.PLAYER_JOINED_STRING_OFFSET);
			int id = Util.getInt(data, OffsetAndSizeConstants.PLAYER_JOINED_ID_OFFSET);
			game.addPlayer(id, name);
		} else if(unmaskedData == ServerPacketType.PLAYER_LEFT) {
			//TODO: remove player from system.
		} else if (unmaskedData == ServerPacketType.PLAYER_POSITION) {
			int id = Util.getInt(data, OffsetAndSizeConstants.PLAYER_POSITION_ID_OFFSET);
			long tick = Util.getLong(data, OffsetAndSizeConstants.PLAYER_POSITION_TICK_OFFSET);
			Vector3f pos = Util.getVector3f(data, OffsetAndSizeConstants.PLAYER_POSITION_POS_OFFSET, new Vector3f());
			Vector3f dir = Util.getVector3f(data, OffsetAndSizeConstants.PLAYER_POSITION_DIR_OFFSET, new Vector3f());
			Vector3f ort = Util.getVector3f(data, OffsetAndSizeConstants.PLAYER_POSITION_ORT_OFFSET, new Vector3f());
			game.updatePosition(pos, dir, ort, id, tick);
		} else {
			Logger.getLogger(getClass().getName()).log(Level.WARNING, "Unhandled packet type: " + data[0]);
		}
	}

}
