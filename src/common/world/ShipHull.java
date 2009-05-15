package common.world;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.jme.bounding.BoundingSphere;
import com.jme.math.FastMath;
import com.jme.math.Triangle;
import com.jme.math.Vector3f;
import com.jme.scene.TriMesh;
import com.jme.util.geom.BufferUtils;
import com.jme.util.geom.NormalGenerator;

public class ShipHull extends TriMesh {
	private static final long serialVersionUID = 1L;

	private static final NormalGenerator ng = new NormalGenerator();
	private static final TriMesh basis = new TriMesh("ShipHullBasis");
	private static boolean basisIsInitiated = false;
	
	private final int subdivs;
	
	private ShipHull(int subdivides) {
		super("ShipHull");
		subdivs = subdivides;
	}

	public static ShipHull create(int subdivides) {
		if (!basisIsInitiated) {
			initiate(basis);
			basisIsInitiated = true;
		}
		
		ShipHull hull = new ShipHull(subdivides);
		hull.setModelBound(new BoundingSphere());
		subdivide(hull, 0f);

		return hull;
	}

	private static void initiate(TriMesh tm) {
		float root2 = FastMath.sqrt(2);
		float topz = root2/3;
		float topy = 3;
		Vector3f[] positions = {
			new Vector3f(-0.6f, 0f, 0f),
			new Vector3f( 0.6f, 0f, 0f),
			new Vector3f(  0f, 0.5f, root2*0.5f),
			new Vector3f(  0f, topy, topz)
		};

		int[] indices = {0,1,2,0,3,1,0,2,3,2,1,3};

		tm.reconstruct(BufferUtils.createFloatBuffer(positions), null,null, null, BufferUtils.createIntBuffer(indices));
	}
	
	public void distort(float amount) {
		if (subdivs == 0) {
			return;
		}
		subdivide(this, amount);
	}

	private static void subdivide(ShipHull hull, float distort) {
		if (hull.subdivs == 0 || distort == 0f) {
			initiate(hull);
			return;
		}
		Triangle[] tris = new Triangle[basis.getTriangleCount()];
		basis.getMeshAsTriangles(tris);
		Triangle[] divd = subdivide(tris, hull.subdivs, distort, new HashMap<Vector3f, Vector3f>());

		Vector3f[] positions = new Vector3f[divd.length*3];
		int[] indices = new int[divd.length * 3];

		int i=0;
		for (Triangle t : divd) {
			for (int j=0; j<3; j++) {
				positions[i] = t.get(j);
				indices[i] = i;
				i++;
			}
		}
		FloatBuffer fb = BufferUtils.createFloatBuffer(positions);
		IntBuffer ib = BufferUtils.createIntBuffer(indices);
		
		hull.reconstruct(fb, null, null, null, ib);
		ng.generateNormals(hull, 0);
		hull.updateModelBound();
	}

	private static Triangle[] subdivide(Triangle[] ts, int numdivs, float distort, HashMap<Vector3f, Vector3f> points) {
		if (numdivs == 0) {
			return ts;
		}

		ts = subdivide(ts, numdivs-1, distort, points);

		List<Triangle> newts = new LinkedList<Triangle>();

		for (Triangle t : ts) {
			Vector3f v0 = t.get(0);
			Vector3f v1 = t.get(1);
			Vector3f v2 = t.get(2);

			Vector3f v01 = v0.add(v1).multLocal(0.5f);
			Vector3f v12 = v1.add(v2).multLocal(0.5f);
			Vector3f v20 = v2.add(v0).multLocal(0.5f);

			Vector3f normal = v20.cross(v12);

			Vector3f v01t;
			Vector3f v12t;
			Vector3f v20t;

			if (points.containsKey(v01)) {
				v01t = points.get(v01);
			} else {
				v01t = v01.add(normal.mult(distort*((float)Math.random()-0.5f)));
				points.put(v01, v01t);
			}

			if (points.containsKey(v12)) {
				v12t = points.get(v12);
			} else {
				v12t = v12.add(normal.mult(distort*((float)Math.random()-0.5f)));
				points.put(v12, v12t);
			}

			if (points.containsKey(v20)) {
				v20t = points.get(v20);
			} else {
				v20t = v20.add(normal.mult(distort*((float)Math.random()-0.5f)));
				points.put(v20, v20t);
			}


			newts.add(new Triangle(v0, v01t, v20t));
			newts.add(new Triangle(v1, v12t, v01t));
			newts.add(new Triangle(v2, v20t, v12t));
			newts.add(new Triangle(v01t, v12t, v20t));
		}

		return newts.toArray(new Triangle[newts.size()]);
	}

}
