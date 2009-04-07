package client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;

import common.PacketSender;
import common.Util;

public class ServerPacketSender extends PacketSender {
	DatagramPacket sendPacket;
	public ServerPacketSender(DatagramSocket sock, SocketAddress saddr) throws SocketException {
		super(sock);
		setSocketAddress(saddr);
	}
	
	public void setSocketAddress(SocketAddress addr) throws SocketException {
		System.out.println(addr);
		sendPacket = new DatagramPacket(new byte[Util.PACKET_SIZE], 0, Util.PACKET_SIZE, addr);
	}
	
	public void send(byte[] data) {
		super.send(data, sendPacket);
	}
}
