package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import common.CatastrophicException;
import common.PacketSender;

import server.clientdb.Client;
import server.clientdb.ClientDB;

public class ClientPacketSender extends PacketSender {
	
	ClientDB cdb;
	
	public ClientPacketSender(ClientDB cdb) throws SocketException {
		super();
		this.cdb = cdb;
	}
	
	public ClientPacketSender(ClientDB cdb, DatagramSocket sock) {
		super(sock);
		this.cdb = cdb;
	}
	
	public void send(byte[] data, Client c) {
		send(data, c.dg);
	}
	
	public void sendToAll(byte[] data) { 
		addTask( new SendToAllTask(data) );
	}
	
	private class SendToAllTask extends AbstractSendTask {
		
		public SendToAllTask(byte[] data) {
			super(data);
		}

		protected void perform() throws IOException {
			//TODO: Think about concurrency problems with iterating over cdb... 
			for ( Client c : cdb )
			{
				c.dg.setData(data);
				send(c.dg);
				c.dg.setData(null, 0, 0);
			}
		}
	}
}
