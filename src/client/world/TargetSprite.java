package client.world;

import com.jme.math.Vector3f;
import com.jme.scene.BillboardNode;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Quad;

public class TargetSprite extends BillboardNode {
	
	public TargetSprite() {
		super("TargetSprite");
		
		Quad q = new Quad("tsQuad",1.5f,1.5f);
		attachChild(q);
		
		setAlignment(CAMERA_ALIGNED);
		setLocalTranslation(0,0.15f,1.2f);
		
		
	}
}
