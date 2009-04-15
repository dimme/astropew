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

}
