package client.world;

import com.jme.scene.BillboardNode;
import com.jme.scene.shape.Quad;

public class TargetSprite extends BillboardNode {
	private static final long serialVersionUID = -1789645653338371438L;

	public TargetSprite(String name) {
		super("TargetSprite");

		
		Quad q = new Quad("tsQuad",1.5f,1.5f);
		attachChild(q);
		
		TextNode nameText = new TextNode(name);
		Quad nameQuad = nameText.getQuad(1);
		nameQuad.getLocalTranslation().y += 1;
		attachChild(nameQuad);
		
		//setAlignment(CAMERA_ALIGNED);
		setLocalTranslation(0,0.15f,1.2f);
		
		
	}
}
