package client;

import common.world.Ship;

public class Player implements common.Player {

	private final String name;
	private final int id;
	private Ship ship;
	private int points = 0;

	public Player(String name, int id) {
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public int getID() {
		return id;
	}

	public boolean equals(Object o) {
		if (o instanceof common.Player) {
			return id == ((common.Player) o).getID();
		}
		return false;
	}

	public int hashCode() {
		return id;
	}

	public Ship getShip() {
		return ship;
	}

	public void setShip(Ship ship) {
		this.ship=ship;
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

	public String toString() {
		return name;
	}

}
