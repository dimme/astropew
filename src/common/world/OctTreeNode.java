package common.world;

import java.util.Collection;
import java.util.HashSet;

import com.jme.scene.Node;
import com.jme.scene.Spatial;

public class OctTreeNode extends Node {

	private static final long serialVersionUID = 1L;

	private OctTreeData data;

	public OctTreeNode(){
		this("OctTreeNode");
	}

	public OctTreeNode(String name) {
		super(name);
		data = new WholeData();
	}

	public int attachChild(WorldObject wobj) {
		int sup = super.attachChild(wobj);

		boolean placingSucceeded = place(wobj);

		if (!placingSucceeded) {
			throw new RuntimeException("Couldn't place " + wobj);
		}

		return sup;
	}

	public boolean place(WorldObject wobj) {
		remove(wobj);
		return addIfColliding(wobj);
	}

	protected void remove(WorldObject wobj) {
		data.remove(wobj);
	}

	private boolean addIfColliding(WorldObject wobj) {
		if (isColliding(wobj, this)) {
			return data.addIfColliding(wobj);
		}
		return false;
	}

	public void findCollisions(WorldObject wobj, Collection<WorldObject> collided) {
		if ( isColliding(wobj, this) ) {
			data.findCollisions(wobj,collided);
		}
	}

	private static boolean isColliding(Spatial s1, Spatial s2) {
		//System.out.println(s1 + " worldbound is " + s1.getWorldBound());
		//System.out.println(s2 + " worldbound is " + s2.getWorldBound());
		//System.out.println("\t" + s1.getName() + " against " + s2.getName());
		return (s1!=s2 && s1.isCollidable() && s2.isCollidable() && s1.getWorldBound().intersects(s2.getWorldBound()));
	}

	private static interface OctTreeData {
		void findCollisions(WorldObject wobj, Collection<WorldObject> collided);
		boolean addIfColliding(WorldObject wobj);
		void remove(WorldObject wobj);
	}

	private static class WholeData implements OctTreeData {

		private Collection<WorldObject> nodes;

		public WholeData() {
			nodes = new HashSet<WorldObject>();
		}

		public void findCollisions(WorldObject wobj, Collection<WorldObject> collided) {
			for (WorldObject wo : nodes) {
				if (isColliding(wo, wobj)) {
					collided.add(wo);
				}
			}
		}

		public boolean addIfColliding(WorldObject wobj) {
			nodes.add(wobj);
			return true;
		}

		public void remove(WorldObject wobj) {
			nodes.remove(wobj);
		}
	}

}
