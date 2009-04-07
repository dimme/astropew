package jmetest.actions;

import jmetest.Ship;

import com.jme.input.action.InputActionEvent;
import com.jme.input.action.KeyInputAction;
import com.jme.math.Vector3f;

/**
 * BrakeAction defines the action that occurs when the key is pressed to 
 * slow down the Ship. It obtains the velocity of the Ship and 
 * translates the Ship by this value.
 *
 */
public class BrakeAction extends KeyInputAction {
    private Ship node;
    private static final Vector3f tempVa = new Vector3f();  //temporary vector for the rotation

    /**
     * The Ship to brake is supplied during construction.
     * @param node the Ship to speed up.
     */
    public BrakeAction(Ship node) {
        this.node = node;
    }

    /**
     * the action calls the Ship's brake command which adjusts its velocity. It
     * then translates the Ship based on this new velocity value.
     */
    public void performAction(InputActionEvent evt) {
    	node.brake(evt.getTime());
    	Vector3f loc = node.getLocalTranslation();
    	loc.addLocal(node.getLocalRotation().getRotationColumn(2, tempVa)
    	        .multLocal(node.getVelocity() * evt.getTime()));
    	node.setLocalTranslation(loc);
    }
}


