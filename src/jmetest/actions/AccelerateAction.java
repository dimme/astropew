package jmetest.actions;

import jmetest.Ship;

import com.jme.input.action.InputActionEvent;
import com.jme.input.action.KeyInputAction;

/**
 * AccelerateAction defines the action that occurs when the key is pressed to
 * speed the Ship up. It obtains the velocity of the Ship and translates the
 * Ship by this value.
 *
 */
public class AccelerateAction extends KeyInputAction {
	public static final int FORWARD = 1;
	public static final int BACKWARD = 0;

	private final int direction;
	private final Ship node;

	/**
	 * The Ship to accelerate is supplied during construction.
	 *
	 * @param node
	 *            the Ship to speed up.
	 */
	public AccelerateAction(Ship node, int direction) {
		this.node = node;
		this.direction = direction;
	}

	/**
	 * the action calls the Ship's accelerate command which adjusts its
	 * velocity. I
	 */
	public void performAction(InputActionEvent evt) {
		if (direction == FORWARD) {
			node.accelerate(evt.getTime());
		} else if (direction == BACKWARD) {
			node.brake(evt.getTime());
		}
	}
}
