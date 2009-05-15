package common.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jme.bounding.BoundingSphere;
import com.jme.math.FastMath;
import com.jme.math.Triangle;
import com.jme.math.Vector3f;
import com.jme.scene.TriMesh;
import com.jme.util.geom.BufferUtils;
import com.jme.util.geom.NormalGenerator;

public class ShipHull {
	private static final long serialVersionUID = 1L;

	private ShipHull() {
	}

	public static TriMesh create(int subdivides) {
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

		TriMesh m = new TriMesh("ShipHull");
		m.reconstruct(BufferUtils.createFloatBuffer(positions), null,null, null, BufferUtils.createIntBuffer(indices));
		subdivide(m, subdivides);


		NormalGenerator ng = new NormalGenerator();
		ng.generateNormals(m, 0);


		m.setModelBound(new BoundingSphere());
		m.updateModelBound();

		return m;
	}

	private static void subdivide(TriMesh tm, int numdivs) {
		if (numdivs == 0) {
			return;
		}
		Triangle[] tris = new Triangle[tm.getTriangleCount()];
		tm.getMeshAsTriangles(tris);
		Triangle[] divd = subdivide(tris, numdivs, new HashMap<Vector3f, Vector3f>());

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

		tm.reconstruct(BufferUtils.createFloatBuffer(positions), null,null, null, BufferUtils.createIntBuffer(indices));

	}

	private static Triangle[] subdivide(Triangle[] ts, int numdivs, HashMap<Vector3f, Vector3f> points) {
		if (numdivs == 0) {
			return ts;
		}

		ts = subdivide(ts, numdivs-1, points);

		List<Triangle> newts = new ArrayList<Triangle>();

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
				v01t = v01.add(normal.mult(0.2f*((float)Math.random()-0.5f)));
				points.put(v01, v01t);
			}

			if (points.containsKey(v12)) {
				v12t = points.get(v12);
			} else {
				v12t = v12.add(normal.mult(0.2f*((float)Math.random()-0.5f)));
				points.put(v12, v12t);
			}

			if (points.containsKey(v20)) {
				v20t = points.get(v20);
			} else {
				v20t = v20.add(normal.mult(0.2f*((float)Math.random()-0.5f)));
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
