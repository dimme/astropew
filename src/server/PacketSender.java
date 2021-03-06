package server;

import java.io.IOException;
import java.net.DatagramSocket;

import server.clientdb.Client;
import server.clientdb.ClientDB;

import common.Util;
import common.network.PacketReaderThread;

public class PacketSender extends common.network.PacketSender {
	ClientDB cdb;

	public PacketSender(ClientDB cdb, DatagramSocket sock,
			PacketReaderThread pread) {
		super(sock, pread);
		this.cdb = cdb;
	}

	public void send(byte[] data, Client c) {
		send(data, c.udpc.dgp);
	}

	public void controlledSend(byte[] data, Client c) {
		controlledSend(data, c.udpc);
	}

	public void sendToAll(byte[] data) {
		addTask(new SendToAllTask(data));
	}

	public void controlledSendToAll(byte[] data) {
		addTask(new ControlledSendToAllTask(data));

	}

	private class SendToAllTask extends AbstractSendTask {

		public SendToAllTask(byte[] data) {
			super(data);
		}

		protected void perform() throws IOException {
			synchronized (cdb) {
				for (final Client c : cdb) {
					c.udpc.dgp.setData(data);
					send(c.udpc.dgp);
					c.udpc.dgp.setData(Util.nullbytes, 0, 0);
				}
			}
		}
	}

	private class ControlledSendToAllTask extends AbstractSendTask {

		public ControlledSendToAllTask(byte[] data) {
			super(data);
		}

		protected void perform() throws IOException {
			synchronized (cdb) {
				for (final Client c : cdb) {
					dserv.addDelivery(data, c.udpc);
				}
			}
		}
	}
}
