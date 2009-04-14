package client;

import java.net.SocketAddress;

import common.PacketSender;
import common.UDPPacket;

public class PacketAcker extends common.PacketAcker {
	private UDPPacket udpp;
	
	public PacketAcker(PacketSender ps, UDPPacket udpp){
		super(ps);
		this.udpp = udpp;
	}

	protected UDPPacket getUDPPacket(SocketAddress saddr) {
		return udpp;
	}

}
