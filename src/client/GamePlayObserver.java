package client;

import java.net.SocketAddress;

import common.GameException;
import common.PacketType;
import common.PacketObserver;
import common.Util;

public class GamePlayObserver implements PacketObserver {
	GameClient client;
	
	public GamePlayObserver(GameClient client) {
		this.client = client;
	}
	
	public void packetReceived(byte[] data, SocketAddress addr) throws GameException {
		if(data[0] == PacketType.INITIALIZER){
			client.initialized(addr);
		}
		
		if(data[0] == PacketType.PLAYER_JOINED){
			byte[] bt = new byte[data.length - 5];
			System.arraycopy(data, 5, bt, 0, bt.length);
			PlayerRepresentation pr = new PlayerRepresentation(new String(bt), Util.getInt(data, 1));
			client.addPlayer(pr);
		}
	}

}
