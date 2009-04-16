package client;

import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.action.InputActionEvent;
import com.jme.input.action.InputActionInterface;

import common.world.Ship;

public class FlyingGameInputHandler extends InputHandler {
	
	public FlyingGameInputHandler(Ship s) {
		setKeyBindings();
		setActions(s);
	}

	private void setKeyBindings() {
		final KeyBindingManager keyboard = KeyBindingManager.getKeyBindingManager();

		keyboard.set("forward", KeyInput.KEY_W);
		keyboard.set("backward", KeyInput.KEY_S);
	}
	
	private void setActions(Ship s) {
		addAction(new AccelerateAction(s,AccelerateAction.ACCELERATE), "forward", true);
		addAction(new AccelerateAction(s,AccelerateAction.DECELERATE), "backward", true);
	}
	
	private static class AccelerateAction implements InputActionInterface {
		
		public static final float ACCELERATE = 1.1f;
		public static final float DECELERATE = 1 / ACCELERATE;
		
		private final Ship ship;
		private final float acceleration;

		public AccelerateAction(Ship s, float acceleration) {
			ship = s;
			this.acceleration = acceleration;
		}

		public void performAction(InputActionEvent evt) {
			ship.getMovement().multLocal(acceleration);
		}
		
	}
}
