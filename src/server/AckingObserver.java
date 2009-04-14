package server;

import java.net.SocketAddress;

import server.clientdb.ClientDB;

import common.network.UDPConnection;

public class AckingObserver extends common.network.AckingObserver {

	private ClientDB cdb;
	
	public AckingObserver(PacketSender ps, ClientDB cdb) {
		super(ps);
	}

	protected UDPConnection getUDPConnection(SocketAddress saddr) {
		return cdb.getClient(saddr).udpc;
	}
}
