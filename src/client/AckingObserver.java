package client;

import java.net.DatagramPacket;
import java.net.SocketAddress;

import common.Util;
import common.network.PacketSender;

public class AckingObserver extends common.network.AckingObserver {
	
	private DatagramPacket dgp;
	
	public AckingObserver(PacketSender ps, SocketAddress saddr) {
		super(ps);
		dgp = new DatagramPacket(Util.nullbytes,0);
		dgp.setSocketAddress(saddr);
	}

	protected DatagramPacket getDatagramPacket(SocketAddress saddr) {
		return dgp;
	}

}
