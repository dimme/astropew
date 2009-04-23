package client;

import client.command.Command;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

public interface Game {

	public abstract void addCommand(Command cmd);
	
	public abstract void startInThread();

	public abstract void fireMissile();
}