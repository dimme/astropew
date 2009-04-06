package server.clientdb;

import java.net.DatagramPacket;

public class Client {
	protected int id;
	public final DatagramPacket dg;
	//TODO: Add reference to world player object or other way..
	
	public Client(DatagramPacket packet, int id) {
		this.id = id;
		dg = packet;
	}
	
	public int hashCode() {
		return id;
	}
}
