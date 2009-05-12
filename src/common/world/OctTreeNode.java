package common.world;

import java.util.Collection;
import java.util.HashSet;

import com.jme.bounding.BoundingBox;
import com.jme.bounding.BoundingVolume;
import com.jme.scene.Node;
import com.jme.scene.Spatial;

public class OctTreeNode extends Node {

	private static final long serialVersionUID = 1L;

	private OctTreeData data;
	
	private final BoundingBox worldbound;

	public OctTreeNode(BoundingBox worldbound){
		this("OctTreeNode", worldbound);
	}
	
	public BoundingVolume getWorldBound() {
		return worldbound;
	}

	public OctTreeNode(String name, BoundingBox worldbound) {
		super(name);
		data = new WholeData();
		this.worldbound = worldbound;
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
	
	public void split() {
		new SplitData(); //Så att Fredrik inte tar bort den igen.
		throw new RuntimeException("Not yet");
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
	
	private static class SplitData implements OctTreeData {
		private final OctTreeNode n0;
		private final OctTreeNode n1;
		private final OctTreeNode n2;
		private final OctTreeNode n3;
		private final OctTreeNode n4;
		private final OctTreeNode n5;
		private final OctTreeNode n6;
		private final OctTreeNode n7;
		
		public SplitData() {
			//TODO: split. :)
			n0 = new OctTreeNode(null);
			n1 = new OctTreeNode(null);
			n2 = new OctTreeNode(null);
			n3 = new OctTreeNode(null);
			n4 = new OctTreeNode(null);
			n5 = new OctTreeNode(null);
			n6 = new OctTreeNode(null);
			n7 = new OctTreeNode(null);
			throw new RuntimeException("Trodde du ja!");
		}
		
		public void findCollisions(WorldObject wobj, Collection<WorldObject> collided) {
			n0.findCollisions(wobj, collided);
			n1.findCollisions(wobj, collided);
			n2.findCollisions(wobj, collided);
			n3.findCollisions(wobj, collided);
			n4.findCollisions(wobj, collided);
			n5.findCollisions(wobj, collided);
			n6.findCollisions(wobj, collided);
			n7.findCollisions(wobj, collided);
		}

		public boolean addIfColliding(WorldObject wobj) {
			boolean b = false;
			b = n0.addIfColliding(wobj) || b;
			b = n1.addIfColliding(wobj) || b;
			b = n2.addIfColliding(wobj) || b;
			b = n3.addIfColliding(wobj) || b;
			b = n4.addIfColliding(wobj) || b;
			b = n5.addIfColliding(wobj) || b;
			b = n6.addIfColliding(wobj) || b;
			b = n7.addIfColliding(wobj) || b;
			return b;
		}

		public void remove(WorldObject wobj) {
			n0.data.remove(wobj);
			n1.data.remove(wobj);
			n2.data.remove(wobj);
			n3.data.remove(wobj);
			n4.data.remove(wobj);
			n5.data.remove(wobj);
			n6.data.remove(wobj);
			n7.data.remove(wobj);
		}
	}

}
