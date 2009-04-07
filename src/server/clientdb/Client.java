package server.clientdb;

import java.net.DatagramPacket;

import common.Player;

public class Client implements Player {
	protected int id;
	protected String name;
	public final DatagramPacket dg;
	//TODO: Add reference to world player object or other way..
	
	public Client(DatagramPacket packet, int id, String name) {
		this.id = id;
		dg = packet;
		this.name = name;
	}
	
	public int hashCode() {
		return id;
	}
	
	public int getID() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public boolean equals(Object o) {
		if (o instanceof Player) {
			return id == ((Player)o).getID();
		}
		return false;
	}
}
