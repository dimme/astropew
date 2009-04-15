package server;

import java.net.DatagramPacket;
import java.net.SocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import server.clientdb.Client;
import server.clientdb.ClientDB;

import common.Util;
import common.network.PacketSender;

public class AckingFilter extends common.network.AckingFilter {
	private final ClientDB cdb;

	public AckingFilter(PacketSender ps, ClientDB cdb) {
		super(ps);
		this.cdb = cdb;
	}

	protected DatagramPacket getDatagramPacket(SocketAddress saddr) {
		final Client c = cdb.getClient(saddr);
		if (c == null) {
			Logger.getLogger(getClass().getName()).log(Level.INFO,
					"ACKing packet from nonexistant client");
			final DatagramPacket dgp = new DatagramPacket(Util.nullbytes, 0);
			dgp.setSocketAddress(saddr);
			return dgp;
		}
		return c.udpc.dgp;
	}
}
