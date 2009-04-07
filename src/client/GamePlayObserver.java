package client;

import java.net.SocketAddress;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import common.GameException;
import common.PacketType;
import common.PacketObserver;
import common.Util;

public class GamePlayObserver implements PacketObserver {
	
	private GameClient client;
	private HashMap<Integer, Player> players;
	private Player self = null;
	
	public GamePlayObserver(GameClient client) {
		this.client = client;
		players = new HashMap<Integer, Player>();
	}
	
	public void packetReceived(byte[] data, SocketAddress addr) throws GameException {
		if(data[0] == PacketType.INITIALIZER){
			String name = new String(data, 13, data.length-13);
			int id = Util.getInt(data, 9);
			self = new Player(name, id);
			client.initialized(addr);
		}
		else if(data[0] == PacketType.PLAYER_JOINED){
			String name = new String(data, 5, data.length-5);
			int id = Util.getInt(data, 1);
			Player p = new Player(name, id);
			if (!p.equals(self)) {
				players.put(p.getID(), p);
			}
		}
		else {
			Logger.getLogger(getClass().getName()).log(Level.WARNING, "Unhandled packet type: " + data[0]);
		}
	}

}
