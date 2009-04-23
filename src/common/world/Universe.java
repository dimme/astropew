package common.world;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

import com.jme.bounding.BoundingBox;
import com.jme.bounding.BoundingVolume;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;

public class Universe extends OctTreeNode {
	
	private static final long serialVersionUID = 1L;
	private static final int MAX_NUM_PLANETS = 1000;
	private static final int POSITION_RANGE = 1000;
	
	private final long seed;
	private final BoundingBox worldbound;
	
	public Universe(long seed) {
		super("Universe");
		this.seed = seed;
		
		worldbound = new BoundingBox(Vector3f.ZERO,POSITION_RANGE,POSITION_RANGE,POSITION_RANGE);
	}
	
	public BoundingVolume getWorldBound() {
		return worldbound;
	}

	public void generate(PlanetFactory pf) {
		Random rnd = new Random(seed);
		
		final int numPlanets = (int)(MAX_NUM_PLANETS* ( 0.3f + 0.7f*rnd.nextFloat() ));
		final Vector3f position = new Vector3f();
		final ColorRGBA color = new ColorRGBA();
		float size;
		
		for (int i=0; i<numPlanets; i++) {
			float x = POSITION_RANGE*(rnd.nextFloat()-0.5f);
			float y = POSITION_RANGE*(rnd.nextFloat()-0.5f);
			float z = POSITION_RANGE*(rnd.nextFloat()-0.5f);
			
			position.set(x,y,z);
			size = 1+10*rnd.nextFloat();
			
			float r = 0.8f + 0.2f*rnd.nextFloat();
			float g = r;
			float b = 1f;
			color.set(r, g, b, 1);
			
			Planet planet = pf.createPlanet(position, size, color);
			
			attachChild(planet);
		}
	}
	
	public void removeChild(WorldObject wobj) {
		if (wobj != null) {
			remove(wobj);
			wobj.removeFromParent();
		}
	}
}
