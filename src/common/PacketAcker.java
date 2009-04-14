package common;

import java.net.SocketAddress;

//Name author: Andy
public abstract class PacketAcker implements PacketObserver {
	protected PacketSender ps;

	public PacketAcker(PacketSender ps) {
		this.ps = ps;
	}

	public void packetReceived(byte[] data, SocketAddress addr) throws GameException {
		if ( ( data[OffsetAndSizeConstants.PACKET_TYPE_OFFSET] & Util.CONTROLLED_PACKET_MASK ) == Util.CONTROLLED_PACKET_MASK  ) {
			byte[] sendData = new byte[2];
			sendData[0] = CommonPacketType.ACK;
			sendData[1] = data[OffsetAndSizeConstants.SEQUENCE_NUMBER_OFFSET];	
			ps.send(sendData, getUDPPacket(addr));
		}
	}
	
	protected abstract UDPPacket getUDPPacket(SocketAddress saddr);

}
