package client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;

import common.Util;
import common.network.PacketReaderThread;
import common.network.UDPConnection;

public class PacketSender extends common.network.PacketSender {
	UDPConnection sendPacket;
	
	public PacketSender(DatagramSocket sock, SocketAddress saddr, PacketReaderThread reader) throws SocketException {
		super(sock, reader);
		DatagramPacket dgp = new DatagramPacket(new byte[Util.PACKET_SIZE], 0, Util.PACKET_SIZE, saddr);
		sendPacket = new UDPConnection(dgp);
	}
	
	public void setSocketAddress(SocketAddress addr) throws SocketException {
		sendPacket.dgp.setSocketAddress(addr);
		
	}
	
	public void send(byte[] data) {
		send(data, sendPacket.dgp);
	}
	
	public void controlledSend(byte[] data) {
		controlledSend(data, sendPacket);
	}
}
