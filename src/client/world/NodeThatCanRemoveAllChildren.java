package client.world;

import com.jme.scene.Node;

public class NodeThatCanRemoveAllChildren extends Node {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public NodeThatCanRemoveAllChildren(String name) {
		super(name);
	}

	public void removeAllChildren() {
		while (children != null && !children.isEmpty()) {
			children.get(0).removeFromParent();
		}
	}

}
