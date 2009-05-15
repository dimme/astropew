package common.world;

import common.Player;

public class NullWorldObject extends WorldObject {

	public static final NullWorldObject instance = new NullWorldObject();
	
	private NullWorldObject() {
		super(null, -1, "NullWorldObject");
	}

	protected void destroy(Player instigator) {
	}
}
