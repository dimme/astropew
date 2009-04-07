package jmetest.actions;

import jmetest.Ship;

import com.jme.input.action.InputActionEvent;
import com.jme.input.action.KeyInputAction;
import com.jme.math.Vector3f;

/**
 * AccelerateAction defines the action that occurs when the key is pressed to 
 * speed the Ship up. It obtains the velocity of the Ship and 
 * translates the Ship by this value.
 *
 */
public class AccelerateAction extends KeyInputAction {
    private Ship node;
    private static final Vector3f tempVa = new Vector3f();  //temporary vector for the rotation

    /**
     * The Ship to accelerate is supplied during construction.
     * @param node the Ship to speed up.
     */
    public AccelerateAction(Ship node) {
        this.node = node;
    }

    /**
     * the action calls the Ship's accelerate command which adjusts its velocity. It
     * then translates the Ship based on this new velocity value.
     */
    public void performAction(InputActionEvent evt) {
        node.accelerate(evt.getTime());
        Vector3f loc = node.getLocalTranslation();
        loc.addLocal(node.getLocalRotation().getRotationColumn(2, tempVa)
        		.multLocal(node.getVelocity() * evt.getTime()));
        node.setLocalTranslation(loc);
    }
}
