package server;

import java.net.SocketAddress;

import server.clientdb.Client;
import server.clientdb.ClientDB;

public class GameAdministration {

	private ClientDB cdb;
	private PacketSender ps;
	
	public GameAdministration(ClientDB cdb, PacketSender ps) {
		this.cdb = cdb;
		this.ps = ps;
	}
	
	public void newConnection(String name, SocketAddress saddr) {
		
		Client c = cdb.getClient(saddr);
		
		if (c == null) {
			c = cdb.createClient(name, saddr);
			
			byte[] data = PacketDataFactory.createPlayerJoined(c.getID(), name);
			
			for (Client cl : cdb.getClients()) {
				ps.send(data, cl);
			}
		}
		
		ps.send(PacketDataFactory.createInitializer(c.getID(), 12345), c);
	}
	
}
