package common.world;

import com.jme.app.SimpleHeadlessApp;
import com.jme.scene.Spatial;

public interface World {

	public void attachChild(Spatial child);
	public void start();

}
