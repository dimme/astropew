package common.world;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.jme.bounding.BoundingBox;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.scene.TriMesh;
import com.jme.util.geom.BufferUtils;
import com.jme.util.geom.NormalGenerator;

public class ShipHull {
	private static final long serialVersionUID = 1L;

	private ShipHull() {
	}
	
	public static TriMesh create() {
		float root2 = FastMath.sqrt(2);
		float topz = root2/3;
		float topy = 3;
		Vector3f[] positions = {
			new Vector3f(-0.6f, 0f, 0f),
			new Vector3f( 0.6f, 0f, 0f),
			new Vector3f(  0f, 0.5f, root2*0.8f),
			new Vector3f(  0f, topy, topz)
		};

		int[] indices = {0,1,2,0,3,1,0,2,3,2,1,3};

		TriMesh m = new TriMesh("ShipHull");
		m.reconstruct(BufferUtils.createFloatBuffer(positions), null,null, null, BufferUtils.createIntBuffer(indices));
		NormalGenerator ng = new NormalGenerator();
		ng.generateNormals(m, 0);

		m.setModelBound(new BoundingBox());
		m.updateModelBound();

		return m;
	}
	
}