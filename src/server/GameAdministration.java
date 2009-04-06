package server;

import java.net.SocketAddress;

import common.MessageType;

import server.clientdb.Client;
import server.clientdb.ClientDB;

public class GameAdministration {

	private ClientDB cdb;
	private PacketSender ps;
	
	public void newConnection(String name, SocketAddress saddr) {
		Client c = cdb.createClient(name, saddr);
		sendMessage(MessageType.PLAYER_JOINED, name);
	}
	
	private void sendMessage(int msgtype, String str) {
		byte[] data = PacketDataFactory.createMessagePacket(MessageType.PLAYER_JOINED, str);
	}
	
}
