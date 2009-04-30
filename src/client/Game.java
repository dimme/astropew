package client;

import client.command.Command;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import common.world.Universe;

public interface Game {

	public abstract void addCommand(Command cmd);
	
	public abstract void startInThread();

	public abstract void fireMissile();
	
	public abstract Universe getUniverse();
	
	public abstract void setPlaying(boolean b);
}