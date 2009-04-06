package server;

import java.net.DatagramPacket;

public class Client {
	protected int id;
	public final DatagramPacket dg;
	
	public Client(DatagramPacket packet, int id) {
		this.id = id;
		dg = packet;
	}
}
