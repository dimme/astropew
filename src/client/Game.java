package client;

import client.command.Command;

import common.world.Universe;

public interface Game {

	public abstract void addCommand(Command cmd);

	public abstract void startInThread();

	public abstract void fireMissile();

	public abstract Universe getUniverse();

	public abstract void setPlaying(boolean b);

	public abstract float getLastUpdateTime();
}