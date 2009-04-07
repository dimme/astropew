package server;

import java.net.DatagramSocket;
import java.net.SocketException;

import common.PacketSender;

import server.clientdb.Client;

public class ClientPacketSender extends PacketSender {
	
	public ClientPacketSender() throws SocketException {
		super();
	}
	
	public ClientPacketSender(DatagramSocket sock) {
		super(sock);
	}
	
	public void send(byte[] data, Client c) {
		send (data, c.dg);
	}
	
	
}
