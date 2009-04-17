package common.world;

import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Spatial;

public interface PlanetFactory {
	public Spatial createPlanet(String name, Vector3f center, float size, ColorRGBA color);
}
