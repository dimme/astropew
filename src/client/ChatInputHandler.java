package client;

import client.command.AbstractCommand;
import client.command.Command;
import client.command.GameCommandInterface;
import client.command.Message;
import client.command.MessageCommand;

import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.action.InputActionEvent;
import com.jme.input.action.InputActionInterface;

public class ChatInputHandler extends InputHandler {
	private final Game game;
	private final StringBuilder sb;
	
	public ChatInputHandler(Game game) {
		this.game = game;
		setActions();
		sb = new StringBuilder();
	}
	
	private void setActions() {
		addAction(new AddCharacterAction(), DEVICE_KEYBOARD, BUTTON_ALL, AXIS_NONE, false);
	}
	
	private void send() {
		String msg = sb.toString();
		sb.delete(0, sb.length());	
		game.sendChatMessage(msg);
		game.setChatMode(false);
	}
	
	private class AddCharacterAction implements InputActionInterface {
		public void performAction(InputActionEvent evt) {
			char c = evt.getTriggerCharacter();
			if (evt.getTriggerPressed() && c != 0) {
				if (evt.getTriggerIndex() == KeyInput.KEY_RETURN) {
					send();
				} else {
					sb.append(c);
				}
			}
		}
	}
}
