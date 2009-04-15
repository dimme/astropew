package common;

import common.world.Ship;

public interface Player {
	public int getID();

	public String getName();
	public void setShip(Ship ship);
	public Ship getShip();
}
