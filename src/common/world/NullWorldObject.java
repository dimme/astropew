package common.world;

import common.Player;

public class NullWorldObject extends WorldObject {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public static final NullWorldObject instance = new NullWorldObject();

	private NullWorldObject() {
		super(null, -1, "NullWorldObject");
	}

	protected void destroy(Player instigator) {
	}
}
