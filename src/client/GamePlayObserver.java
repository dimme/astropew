package client;

import java.net.SocketAddress;

import common.GameException;
import common.PacketType;
import common.PacketObserver;

public class GamePlayObserver implements PacketObserver {
	GameClient client;
	
	public GamePlayObserver(GameClient client) {
		this.client = client;
	}
	
	public void packetReceived(byte[] data, SocketAddress addr) throws GameException {
		if(data[0] == PacketType.INITIALIZER){
			client.initialized(addr);
		}
	}

}
