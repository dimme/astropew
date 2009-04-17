package client;

import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.action.InputActionEvent;
import com.jme.input.action.InputActionInterface;
import com.jme.math.Matrix3f;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

import common.world.Ship;

public class FlyingGameInputHandler extends InputHandler {
	
	public FlyingGameInputHandler(Ship s) {
		setKeyBindings();
		setActions(s);
	}

	private void setKeyBindings() {
		final KeyBindingManager keyboard = KeyBindingManager.getKeyBindingManager();

		keyboard.set("accelerate", KeyInput.KEY_W);
		keyboard.set("decelerate", KeyInput.KEY_S);
		keyboard.set("turn_right", KeyInput.KEY_D);
		keyboard.set("turn_left", KeyInput.KEY_A);
	}
	
	private void setActions(Ship s) {
		addAction(new AccelerateAction(s,AccelerateAction.ACCELERATE), "accelerate", true);
		addAction(new AccelerateAction(s,AccelerateAction.DECELERATE), "decelerate", true);
		addAction(new TurnAction(s,TurnAction.RIGHT), "turn_right", true);
		addAction(new TurnAction(s,TurnAction.LEFT), "turn_left", true);
	}
	
	private abstract static class AbstractAction implements InputActionInterface {

		protected final Ship ship;
		public AbstractAction(Ship ship) {
			this.ship=ship;
		}
		
	}
	
	private static class AccelerateAction extends AbstractAction {
		
		public static final float ACCELERATE = 0.1f;
		public static final float DECELERATE = -0.1f;
		
		protected final float acceleration;
		private final Vector3f z;

		public AccelerateAction(Ship s, float acceleration) {
			super(s);
			this.acceleration = acceleration;
			this.z = new Vector3f();
		}

		public void performAction(InputActionEvent evt) {
			Quaternion ort = ship.getOrientation();
			ort.getRotationColumn(2, z);
			ship.getMovement().addLocal(z.multLocal(-acceleration));
			ship.setLastUpdate(System.currentTimeMillis());
		}
	}
	
	private static class TurnAction extends AbstractAction {

		public static final float LEFT = 0.1f;
		public static final float RIGHT = -0.1f;
		
		protected final float angle;
		private final Matrix3f rotation;
		private final Vector3f y; 

		public TurnAction(Ship s, float angle) {
			super(s);
			this.angle=angle;
			rotation = new Matrix3f();
			y = new Vector3f();
		}

		public void performAction(InputActionEvent evt) {
			Quaternion ort = ship.getOrientation();
			ort.getRotationColumn(1, y);
			rotation.fromAngleNormalAxis(angle, y);
			ort.apply(rotation);
			ship.setLastUpdate(System.currentTimeMillis());
		}
	}
}
