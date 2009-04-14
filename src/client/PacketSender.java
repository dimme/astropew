package client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;

import common.Util;
import common.network.UDPConnection;

public class PacketSender extends common.network.PacketSender {
	UDPConnection sendPacket;
	
	public PacketSender(DatagramSocket sock, SocketAddress saddr) throws SocketException {
		super(sock);
		setSocketAddress(saddr);
	}
	
	public void setSocketAddress(SocketAddress addr) throws SocketException {
		DatagramPacket dgp = new DatagramPacket(new byte[Util.PACKET_SIZE], 0, Util.PACKET_SIZE, addr);
		sendPacket = new UDPConnection(dgp);
	}
	
	public void send(byte[] data) {
		send(data, sendPacket);
	}
}
