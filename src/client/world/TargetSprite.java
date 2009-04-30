package client.world;

import com.jme.math.Vector3f;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Quad;

public class TargetSprite extends Box {
	public TargetSprite() {
		//super("TargetSprite", 2f, 2f);
		super("TargetSprite", new Vector3f(), 0.3f, 0.3f, 0.3f);
		this.setRandomColors();
	}
}
