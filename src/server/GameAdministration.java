package server;

import java.net.SocketAddress;

import server.clientdb.Client;
import server.clientdb.ClientDB;

public class GameAdministration {

	private ClientDB cdb;
	private ClientPacketSender ps;
	
	public GameAdministration(ClientDB cdb, ClientPacketSender ps) {
		this.cdb = cdb;
		this.ps = ps;
	}
	
	public void newConnection(String name, SocketAddress saddr) {
		
		Client c = cdb.getClient(saddr);
		
		if (c == null) {
			c = cdb.createClient(name, saddr);
			
			byte[] data = PacketDataFactory.createPlayerJoined(c.getID(), name);
			
			sendToAll(data);
		}
		
		ps.send(PacketDataFactory.createInitializer(c.getID(), 12345), c);
	}
	
	public void leaving(SocketAddress saddr) {
		Client removed = cdb.removeClient(saddr);
		
		byte[] data = PacketDataFactory.createPlayerLeft(removed.getID());
		sendToAll(data);
	}
	
	private void sendToAll(byte[] data) {
		for (Client c : cdb.getClients()) {
			ps.send(data, c);
		}
	}
}
