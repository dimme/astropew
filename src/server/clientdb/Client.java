package server.clientdb;

import java.net.DatagramPacket;

import common.Player;
import common.network.UDPConnection;

public class Client implements Player {
	protected int id;
	protected String name;
	public final UDPConnection udpc;

	// TODO: Add reference to world player object or other way..

	public Client(DatagramPacket packet, int id, String name) {
		this.id = id;
		udpc = new UDPConnection(packet);
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
			return id == ((Player) o).getID();
		}
		return false;
	}
}
