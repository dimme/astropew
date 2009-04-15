package common.network;

import java.net.DatagramPacket;
import java.net.SocketAddress;

import common.CommonPacketType;
import common.GameException;
import common.Util;

public abstract class AckingObserver extends AbstractPacketObserver {
	protected PacketSender ps;
	
	//TODO: template method för executors (fånga throwable)
	//TODO: Scheduled executor - efterforska möjlighet
	//TODO: Ta bort clientframe
	//TODO: ACK-filter
	//TODO: Gör en TODO-fil. :)

	public AckingObserver(PacketSender ps) {
		this.ps = ps;
	}

	public boolean packetReceived(byte[] data, SocketAddress addr) throws GameException {
		if ( isControlledPacket(data) ) {
			byte[] sendData = new byte[2];
			sendData[0] = CommonPacketType.ACK;
			sendData[1] = data[1];	
			ps.send(sendData, getDatagramPacket(addr));
		}
		return false;
	}
	
	protected abstract DatagramPacket getDatagramPacket(SocketAddress saddr);
}
