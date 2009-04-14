package client;

import java.net.SocketAddress;

import common.network.PacketSender;
import common.network.UDPConnection;

public class AckingObserver extends common.network.AckingObserver {
	private UDPConnection udpc;
	
	public AckingObserver(PacketSender ps, UDPConnection udpc){
		super(ps);
		this.udpc = udpc;
	}

	protected UDPConnection getUDPConnection(SocketAddress saddr) {
		return udpc;
	}

}
