package client;

import client.world.SelfShip;

import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.action.InputActionEvent;
import com.jme.input.action.InputActionInterface;
import com.jme.math.FastMath;
import com.jme.math.Matrix3f;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

import common.world.Ship;

public class FlyingGameInputHandler extends InputHandler {
	protected final Ship ship;
	protected final Game game;
	
	public FlyingGameInputHandler(Ship s, Game game) {
		this.ship = s;
		this.game =  game;
		setKeyBindings();
		setActions(s);
	}

	private void setKeyBindings() {
		final KeyBindingManager keyboard = KeyBindingManager.getKeyBindingManager();

		keyboard.set("accelerate", KeyInput.KEY_W);
		keyboard.set("decelerate", KeyInput.KEY_S);
		keyboard.set("brake", KeyInput.KEY_LSHIFT);
		keyboard.set("turn_right", KeyInput.KEY_D);
		keyboard.set("turn_left", KeyInput.KEY_A);
		keyboard.set("turn_up", KeyInput.KEY_DOWN);
		keyboard.set("turn_down", KeyInput.KEY_UP);
		keyboard.set("turn_ccw", KeyInput.KEY_LEFT);
		keyboard.set("turn_cw", KeyInput.KEY_RIGHT);
		keyboard.set("fire_missile", KeyInput.KEY_SPACE);
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
		addAction(new FireMissileAction(), "fire_missile", true);
	}
	
	private class FireMissileAction implements InputActionInterface {
		
		public FireMissileAction(){
		}
		
		public void performAction(InputActionEvent evt) {
			game.fireMissile();
		}
		
	}
	
	private class AccelerateAction implements InputActionInterface {
		
		public static final float ACCELERATE = 20f;
		public static final float DECELERATE = -20f;
		public static final float MAX_SPEED = 20f;
		public static final float MAX_SPEED_SQ = MAX_SPEED*MAX_SPEED;
		
		protected final float acceleration;
		private final Vector3f z;

		public AccelerateAction(float acceleration) {
			this.acceleration = acceleration;
			this.z = new Vector3f();
		}

		public void performAction(InputActionEvent evt) {
			Quaternion ort = ship.getLocalRotation();
			ort.getRotationColumn(2, z);
			Vector3f velocity = ship.getMovement();
			velocity.addLocal(z.multLocal(acceleration*evt.getTime()));
			if (velocity.lengthSquared() > MAX_SPEED_SQ) {
				velocity.normalizeLocal();
				velocity.multLocal(MAX_SPEED);
			}
		}
	}
	
	private class BrakeAction implements InputActionInterface {

		private static final float BRAKE_COEFFICIENT = 0.97f;
		
		public BrakeAction() {
		}

		public void performAction(InputActionEvent evt) {
			ship.getMovement().multLocal(FastMath.pow(BRAKE_COEFFICIENT, evt.getTime()));
		}
	}
	
	private class TurnAction implements InputActionInterface {

		public static final int X = 0;
		public static final int Y = 1;
		public static final int Z = 2;
		
		public static final float LEFT = 1f;
		public static final float RIGHT = -1f;
		public static final float UP = 1f;
		public static final float DOWN = -1f;
		public static final float CW = -1f;
		public static final float CCW = 1f;
		
		protected final float angle;
		private final Matrix3f rotation;
		private final Matrix3f tmp;
		private final Vector3f axis; 
		private final int axisid;

		public TurnAction(int axisid, float angle) {
			this.angle=angle;
			rotation = new Matrix3f();
			tmp = new Matrix3f();
			axis = new Vector3f();
			this.axisid=axisid;
		}

		public void performAction(InputActionEvent evt) {
			Quaternion ort = ship.getLocalRotation();
			ort.getRotationColumn(axisid, axis);
			rotation.fromAngleAxis(angle*evt.getTime(), axis);
			
			ort.toRotationMatrix(tmp);
			rotation.multLocal(tmp);
			ort.fromRotationMatrix(rotation);
		}
	}
}
