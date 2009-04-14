package server;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;

import server.clientdb.Client;
import server.clientdb.ClientDB;

public class PacketSender extends common.PacketSender {
	ClientDB cdb;
	
	public PacketSender(ClientDB cdb) throws SocketException {
		super();
		this.cdb = cdb;
	}
	
	public PacketSender(ClientDB cdb, DatagramSocket sock) {
		super(sock);
		this.cdb = cdb;
	}
	
	public void send(byte[] data, Client c) {
		send(data, c.udpp);
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
				c.udpp.dgp.setData(data);
				send(c.udpp.dgp);
				c.udpp.dgp.setData(nullbytes, 0, 0);
			}
		}
	}
}
