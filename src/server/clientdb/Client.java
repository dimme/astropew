package server.clientdb;

import java.net.DatagramPacket;

import common.Player;
import common.network.UDPConnection;
import common.world.Ship;

public class Client implements Player {
	protected int id;
	protected String name;
	protected Ship ship;
	protected int points = 0;
	public final UDPConnection udpc;

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

	public Ship getShip() {
		return ship;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}
	
	public void addPoints(int p) {
		points+=p;
	}
}
