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
	protected final Ship ship;
	
	public FlyingGameInputHandler(Ship s) {
		this.ship = s;
		setKeyBindings();
		setActions(s);
	}

	private void setKeyBindings() {
		final KeyBindingManager keyboard = KeyBindingManager.getKeyBindingManager();

		keyboard.set("accelerate", KeyInput.KEY_W);
		//keyboard.set("decelerate", KeyInput.KEY_S);
		keyboard.set("brake", KeyInput.KEY_S);
		keyboard.set("turn_right", KeyInput.KEY_D);
		keyboard.set("turn_left", KeyInput.KEY_A);
		keyboard.set("turn_up", KeyInput.KEY_DOWN);
		keyboard.set("turn_down", KeyInput.KEY_UP);
		keyboard.set("turn_ccw", KeyInput.KEY_LEFT);
		keyboard.set("turn_cw", KeyInput.KEY_RIGHT);
	}
	
	private void setActions(Ship s) {
		addAction(new AccelerateAction(AccelerateAction.ACCELERATE), "accelerate", true);
		addAction(new AccelerateAction(AccelerateAction.DECELERATE), "decelerate", true);
		addAction(new BrakeAction(), "brake", true);
		addAction(new TurnAction(TurnAction.Y, TurnAction.RIGHT), "turn_right", true);
		addAction(new TurnAction(TurnAction.Y, TurnAction.LEFT), "turn_left", true);
		addAction(new TurnAction(TurnAction.X, TurnAction.UP), "turn_down", true);
		addAction(new TurnAction(TurnAction.X, TurnAction.DOWN), "turn_up", true);
		addAction(new TurnAction(TurnAction.Z, TurnAction.CW), "turn_cw", true);
		addAction(new TurnAction(TurnAction.Z, TurnAction.CCW), "turn_ccw", true);
	}
	
	private class AccelerateAction implements InputActionInterface {
		
		public static final float ACCELERATE = 1f;
		public static final float DECELERATE = -1f;
		
		protected final float acceleration;
		private final Vector3f z;

		public AccelerateAction(float acceleration) {
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
	
	private class BrakeAction implements InputActionInterface {

		private static final float BRAKE_COEFFICIENT = 0.9f;
		
		public BrakeAction() {
		}

		public void performAction(InputActionEvent evt) {
			ship.getMovement().multLocal(BRAKE_COEFFICIENT);
		}
	}
	
	private class TurnAction implements InputActionInterface {

		public static final int X = 0;
		public static final int Y = 1;
		public static final int Z = 2;
		
		public static final float LEFT = 0.01f;
		public static final float RIGHT = -0.01f;
		public static final float UP = -0.005f;
		public static final float DOWN = 0.005f;
		public static final float CW = -0.01f;
		public static final float CCW = 0.01f;
		
		protected final float angle;
		private final Matrix3f rotation;
		private final Vector3f axis; 
		private final int axisid;

		public TurnAction(int axisid, float angle) {
			this.angle=angle;
			rotation = new Matrix3f();
			axis = new Vector3f();
			this.axisid=axisid;
		}

		public void performAction(InputActionEvent evt) {
			ship.getOrientation().getRotationColumn(axisid, axis);
			rotation.fromAngleAxis(angle, axis);
			
			ship.getRotationSpeed().multLocal(rotation);
			
			/*Quaternion ort = ship.getOrientation();
			ort.getRotationColumn(1, y);
			rotation.fromAngleNormalAxis(angle, y);
			ort.apply(rotation);
			ship.setLastUpdate(System.currentTimeMillis());*/
		}
	}
}
