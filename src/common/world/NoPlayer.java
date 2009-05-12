package common.world;

import common.Player;

public class NoPlayer implements Player {

	public static final Player instance = new NoPlayer();

	private NoPlayer() {
	}

	public int getID() {
		return -1;
	}

	public String getName() {
		return "None";
	}

	public boolean equals(Object o) {
		return o == this;
	}

	public int hashCode() {
		return getID();
	}

	public Ship getShip() {
		throw new RuntimeException("This method should not be called!");
	}

	public void setShip(Ship ship) {
		throw new RuntimeException("This method should not be called!");
	}

	public int getPoints() {
		return 0;
	}

	public void setPoints(int p) {
	}
}
