package client;

import java.net.SocketAddress;

import common.GameException;
import common.PackageType;
import common.PacketObserver;

public class GamePlayObserver implements PacketObserver {
	GameClient client;
	
	public GamePlayObserver(GameClient client) {
		this.client = client;
	}
	
	public void packetReceived(byte[] data, SocketAddress addr) throws GameException {
		if(data[0] == PackageType.INITIALIZER){
			client.initialized(addr);
		}
	}

}
