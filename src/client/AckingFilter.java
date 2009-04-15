package client;

import java.net.DatagramPacket;
import java.net.SocketAddress;

import common.Util;
import common.network.PacketSender;

public class AckingFilter extends common.network.AckingFilter {

	private final DatagramPacket dgp;

	public AckingFilter(PacketSender ps, SocketAddress saddr) {
		super(ps);
		dgp = new DatagramPacket(Util.nullbytes, 0);
		dgp.setSocketAddress(saddr);
	}

	protected DatagramPacket getDatagramPacket(SocketAddress saddr) {
		return dgp;
	}

}
