package client;

import java.util.LinkedList;
import java.util.List;

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
	
	private final List<TurnAction> turnactions = new LinkedList<TurnAction>();
	private float chatDelay = 0;

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
		keyboard.set("chat", KeyInput.KEY_RETURN);
	}

	private void add(TurnAction ta, String cmd) {
		turnactions.add(ta);
		_add(ta, cmd);
	}
	
	private void add(InputActionInterface iai, String cmd) {
		_add(iai, cmd);
	}
	
	private void _add(InputActionInterface iai, String cmd) {
		addAction(iai, cmd, true);
	}
	
	private void setActions(Ship s) {
		add(new AccelerateAction(AccelerateAction.ACCELERATE), "accelerate");
		add(new AccelerateAction(AccelerateAction.DECELERATE), "decelerate");
		add(new BrakeAction(), "brake");
		add(new TurnAction(TurnAction.Y, TurnAction.RIGHT), "turn_right");
		add(new TurnAction(TurnAction.Y, TurnAction.LEFT), "turn_left");
		add(new TurnAction(TurnAction.X, TurnAction.UP), "turn_down");
		add(new TurnAction(TurnAction.X, TurnAction.DOWN), "turn_up");
		add(new TurnAction(TurnAction.Z, TurnAction.CW), "turn_cw");
		add(new TurnAction(TurnAction.Z, TurnAction.CCW), "turn_ccw");
		add(new FireMissileAction(), "fire_missile");
		add(new ChatAction(), "chat");
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

		private static final float BRAKE_COEFFICIENT = 0.1f;

		public BrakeAction() {
		}

		public void performAction(InputActionEvent evt) {
			ship.getMovement().multLocal(FastMath.pow(BRAKE_COEFFICIENT, evt.getTime()));
		}
	}
	
	private class ChatAction implements InputActionInterface {
		public void performAction(InputActionEvent evt) {
			if (chatDelay <= 0f) {
				game.setChatMode(true);
			}
		}
	}

	private class TurnAction implements InputActionInterface {

		public static final int X = 0;
		public static final int Y = 1;
		public static final int Z = 2;

		public static final float LEFT = 1.5f;
		public static final float RIGHT = -1.5f;
		public static final float UP = 1.5f;
		public static final float DOWN = -1.5f;
		public static final float CW = -1.5f;
		public static final float CCW = 1.5f;

		protected final float angle;
		private final Matrix3f rotation;
		private final Matrix3f tmp;
		private final Vector3f axis;
		private final int axisid;
		
		private boolean heldLastTime = false;
		private boolean heldThisTime = false;
		private float held = 0;
		private static final float TURN_ACCELERATION_TIME = 0.3f;

		public TurnAction(int axisid, float angle) {
			this.angle=angle;
			rotation = new Matrix3f();
			tmp = new Matrix3f();
			axis = new Vector3f();
			this.axisid=axisid;
		}

		public void performAction(InputActionEvent evt) {
			heldThisTime = true;
			if (heldLastTime) {
				held += evt.getTime();
				held = Math.min(held, TURN_ACCELERATION_TIME);
			} else {
				held = 0;
			}
			
			Quaternion ort = ship.getLocalRotation();
			ort.getRotationColumn(axisid, axis);
			rotation.fromAngleAxis((held/TURN_ACCELERATION_TIME)*angle*evt.getTime(), axis);

			ort.toRotationMatrix(tmp);
			rotation.multLocal(tmp);
			ort.fromRotationMatrix(rotation);
		}

		public void startingUpdate() {
			heldLastTime = heldThisTime;
			heldThisTime = false;
		}
	}
	
	public void setEnabled(boolean b) {
		chatDelay = 0.2f;
		super.setEnabled(b);
	}
	
	public void update(float interpolation) {
		chatDelay = Math.max(chatDelay-interpolation, 0);
		for (TurnAction ta : turnactions) {
			ta.startingUpdate();
		}
		super.update(interpolation);
	}
}
