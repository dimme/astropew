package jmetest.actions;

import jmetest.Ship;

import com.jme.input.action.InputActionEvent;
import com.jme.input.action.KeyInputAction;
import com.jme.math.Matrix3f;
import com.jme.math.Vector3f;

/**
 * ShipRotateLeftAction turns the node to the left (while traveling forward).
 * 
 */
public class RotateAction extends KeyInputAction {
	// temporary variables to handle rotation
	public static final int LEFT = 1;
	public static final int RIGHT = -1;

	public static final int VERTICAL = 0;
	public static final int HORIZONTAL = 1;

	private static final Matrix3f incr = new Matrix3f();
	private static final Matrix3f tempMa = new Matrix3f();
	private static final Matrix3f tempMb = new Matrix3f();

	private final Ship node; // the node to manipulate
	private final int direction;
	private final int rotCol;

	/**
	 * create a new action with the node to turn.
	 * 
	 * @param node
	 *            the node to turn
	 */
	public RotateAction(Ship Ship, int rotCol, int direction) {
		this.node = Ship;
		this.rotCol = rotCol;
		this.direction = direction;
	}

	/**
	 * turn the node by its turning speed. If the node is traveling backwards,
	 * swap direction.
	 */
	public void performAction(InputActionEvent evt) {
		final Vector3f rotAxis = node.getLocalRotation().getRotationColumn(
				rotCol);
		// we want to turn differently depending on which direction we are
		// traveling in.
		if (node.getVelocity() < 0) {
			incr.fromAngleNormalAxis(direction * node.getTurnSpeed()
					* evt.getTime(), rotAxis);
		} else {
			incr.fromAngleNormalAxis(direction * node.getTurnSpeed()
					* evt.getTime(), rotAxis);
		}
		node.getLocalRotation().fromRotationMatrix(
				incr.mult(node.getLocalRotation().toRotationMatrix(tempMa),
						tempMb));

		node.getLocalRotation().normalize();
	}
}
