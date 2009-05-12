package common.world;

import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;

public interface PlanetFactory {
	public Planet createPlanet(Vector3f center, float size, ColorRGBA color);
}
