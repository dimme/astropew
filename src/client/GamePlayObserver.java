package client;

import java.net.SocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme.math.Vector3f;

import common.GameException;
import common.OffsetConstants;
import common.ServerPacketType;
import common.Util;
import common.network.AbstractPacketObserver;
import common.network.PacketObserver;

public class GamePlayObserver extends AbstractPacketObserver {
	
	private GameClient client;
	private Game game;
	private Player self = null;
	
	public GamePlayObserver(GameClient client, Game game) {
		this.client = client;
		this.game = game;
	}
	
	public boolean packetReceived(byte[] data, SocketAddress addr) throws GameException {
		byte packettype = packetType(data);
		if(packettype == ServerPacketType.INITIALIZER) {
			String name = new String(data, OffsetConstants.INITIALIZER_STRING_OFFSET, data.length - OffsetConstants.INITIALIZER_STRING_OFFSET);
			int id = Util.getInt(data, OffsetConstants.INITIALIZER_ID_OFFSET);
			game.addPlayer(id, name);
		} else if(packettype == ServerPacketType.PLAYER_JOINED) {
			String name = new String(data, OffsetConstants.PLAYER_JOINED_STRING_OFFSET, data.length - OffsetConstants.PLAYER_JOINED_STRING_OFFSET);
			int id = Util.getInt(data, OffsetConstants.PLAYER_JOINED_ID_OFFSET);
			game.addPlayer(id, name);
		} else if(packettype == ServerPacketType.PLAYERS_INFO) {
			for(int i = 2; i < data.length;){
				int id = Util.getInt(data, i);
				i+=4;
				byte tmp = data[i];
				i++;
				String name = new String(data, i, tmp);
				i+=tmp;
				game.addPlayer(id, name);
			}
		} else if(packettype == ServerPacketType.PLAYER_LEFT) {
			//TODO: remove player from system.
		} else if (packettype == ServerPacketType.PLAYER_POSITION) {
			int id = Util.getInt(data, OffsetConstants.PLAYER_POSITION_ID_OFFSET);
			long tick = Util.getLong(data, OffsetConstants.PLAYER_POSITION_TICK_OFFSET);
			Vector3f pos = Util.getVector3f(data, OffsetConstants.PLAYER_POSITION_POS_OFFSET, new Vector3f());
			Vector3f dir = Util.getVector3f(data, OffsetConstants.PLAYER_POSITION_DIR_OFFSET, new Vector3f());
			Vector3f ort = Util.getVector3f(data, OffsetConstants.PLAYER_POSITION_ORT_OFFSET, new Vector3f());
			game.updatePosition(pos, dir, ort, id, tick);
		} else {
			return false;
		}
		return true;
	}

}
