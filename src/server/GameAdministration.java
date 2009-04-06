package server;

import java.net.SocketAddress;

import server.clientdb.Client;
import server.clientdb.ClientDB;

public class GameAdministration {

	private ClientDB cdb;
	
	public void newConnection(String name, SocketAddress saddr) {
		Client c = cdb.createClient(name, saddr);
	}
	
	private void sendMessage(String msg) {
		
	}
	
}
