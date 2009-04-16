package client;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.scene.state.MaterialState;

public interface Game {

	public abstract void finish();

	public abstract void updatePosition(Vector3f pos, Quaternion ort, Vector3f dir, int id, long tick);

	public abstract void addPlayer(int id, String name);

	public abstract void addSelf(int id, String name);

	public abstract void removePlayer(int id);

	public abstract MaterialState createMaterialState();

	public abstract void attachToRoot(Spatial s);

	public abstract void removeFromRoot(Spatial s);

}