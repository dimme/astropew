package client;

import java.net.SocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme.math.Vector3f;

import common.GameException;
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
		if(data[0] == ServerPacketType.INITIALIZER) {
			String name = new String(data, 13, data.length-13);
			int id = Util.getInt(data, 9);
			game.addPlayer(id, name);
			client.initialized(addr);
		} else if(data[0] == ServerPacketType.PLAYER_JOINED) {
			String name = new String(data, 5, data.length-5);
			int id = Util.getInt(data, 1);
			game.addPlayer(id, name);
		} else if(data[0] == ServerPacketType.PLAYER_LEFT) {
			//TODO: remove player from system.
		} else if (data[0] == ServerPacketType.PLAYER_POSITION) {
			int id = Util.getInt(data, 9);
			long tick = Util.getLong(data, 1);
			Vector3f pos = Util.getVector3f(data, 13, new Vector3f());
			Vector3f dir = Util.getVector3f(data, 25, new Vector3f());
			Vector3f ort = Util.getVector3f(data, 37, new Vector3f());
			game.updatePosition(pos, dir, ort, id, tick);
		} else {
			Logger.getLogger(getClass().getName()).log(Level.WARNING, "Unhandled packet type: " + data[0]);
		}
	}

}
