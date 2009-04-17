package common.world;

import java.util.Random;

import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;

public class Universe extends Node {
	
	private static final long serialVersionUID = 1L;
	private static final int MAX_NUM_PLANETS = 1000;
	private static final int POSITION_RANGE = 3000;
	
	public Universe(long seed, PlanetFactory pf) {
		super("Universe");
		generate(seed, pf);
	}

	private void generate(long seed, PlanetFactory pf) {
		Random rnd = new Random(seed);
		
		final int numPlanets = rnd.nextInt(MAX_NUM_PLANETS);
		final Vector3f position = new Vector3f();
		final ColorRGBA color = new ColorRGBA();
		float size;
		
		for (int i=0; i<numPlanets; i++) {
			final float x = POSITION_RANGE*(rnd.nextFloat()-0.5f);
			final float y = POSITION_RANGE*(rnd.nextFloat()-0.5f);
			final float z = POSITION_RANGE*(rnd.nextFloat()-0.5f);
			position.set(x,y,z);
			size = 1+10*rnd.nextFloat();
			color.set(rnd.nextFloat(), rnd.nextFloat(), rnd.nextFloat(), 0.5f);
			
			Spatial planet = pf.createPlanet("Planet" + i, position, size, color);
			
			attachChild(planet);
		}
	}
	
}
