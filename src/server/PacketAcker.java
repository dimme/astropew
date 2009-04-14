package server;

import java.net.SocketAddress;

import server.clientdb.ClientDB;

import common.PacketSender;
import common.UDPPacket;

public class PacketAcker extends common.PacketAcker {

	private ClientDB cdb;
	
	public PacketAcker(PacketSender ps, ClientDB cdb) {
		super(ps);
	}

	protected UDPPacket getUDPPacket(SocketAddress saddr) {
		return cdb.getClient(saddr).udpp;
	}

}
