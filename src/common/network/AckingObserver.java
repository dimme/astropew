package common.network;

import java.net.SocketAddress;

import common.CommonPacketType;
import common.GameException;
import common.Util;

public abstract class AckingObserver implements PacketObserver {
	protected PacketSender ps;

	public AckingObserver(PacketSender ps) {
		this.ps = ps;
	}

	public void packetReceived(byte[] data, SocketAddress addr) throws GameException {
		if ( ( data[OffsetConstants.PACKET_TYPE_OFFSET] & Util.CONTROLLED_PACKET_MASK ) == Util.CONTROLLED_PACKET_MASK  ) {
			byte[] sendData = new byte[2];
			sendData[0] = CommonPacketType.ACK;
			sendData[1] = data[OffsetConstants.SEQUENCE_NUMBER_OFFSET];	
			ps.send(sendData, getUDPConnection(addr));
		}
	}
	
	protected abstract UDPConnection getUDPConnection(SocketAddress saddr);

}
