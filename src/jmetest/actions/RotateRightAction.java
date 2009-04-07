package jmetest.actions;

import jmetest.Ship;

import com.jme.input.action.InputActionEvent;
import com.jme.input.action.KeyInputAction;
import com.jme.math.Matrix3f;
import com.jme.math.Vector3f;

/**
 * ShipRotateLeftAction turns the node to the left (while 
 * traveling forward).
 *
 */
public class RotateRightAction extends KeyInputAction {
    //temporary variables to handle rotation
    private static final Matrix3f incr = new Matrix3f();
    private static final Matrix3f tempMa = new Matrix3f();
    private static final Matrix3f tempMb = new Matrix3f();

    
    private Vector3f upAxis = new Vector3f(0,1,0); //we are using +Y as our up
    
    private Ship node; //the node to manipulate
   
    /**
     * create a new action with the node to turn.
     * @param node the node to turn
     */
    public RotateRightAction(Ship Ship) {
        this.node = Ship;
    }

    /**
     * turn the node by its turning speed. If the node is traveling 
     * backwards, swap direction.
     */
    public void performAction(InputActionEvent evt) {
        //we want to turn differently depending on which direction we are traveling in.
        if(node.getVelocity() < 0) {
            incr.fromAngleNormalAxis(node.getTurnSpeed() * evt.getTime(), upAxis);
        } else {
            incr.fromAngleNormalAxis(-node.getTurnSpeed() * evt.getTime(), upAxis);
        }
        node.getLocalRotation().fromRotationMatrix(
                incr.mult(node.getLocalRotation().toRotationMatrix(tempMa),
                        tempMb));
        node.getLocalRotation().normalize();
    }
}
