/*
 * Copyright (c) 2003-2009 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jmetest;

import jmetest.actions.AccelerateAction;
import jmetest.actions.RotateAction;

import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;

/**
 * Input Handler for the Flag Rush game. This controls a supplied spatial
 * allowing us to move it forward, backward and rotate it left and right.
 *
 * @author Mark Powell
 *
 */
public class FlagRushHandler extends InputHandler {

	Ship ship;

	public void update(float time) {
		super.update(time);
		ship.update(time);
		// we always want to allow friction to control the drift
	}

	/**
	 * Supply the node to control and the api that will handle input creation.
	 *
	 * @param node
	 *            the node we wish to move
	 * @param api
	 *            the library that will handle creation of the input.
	 */
	public FlagRushHandler(Ship node, String api) {
		setKeyBindings(api);
		setActions(node);
		ship = node;
	}

	/**
	 * creates the keyboard object, allowing us to obtain the values of a
	 * keyboard as keys are pressed. It then sets the jmetest.actions to be
	 * triggered based on if certain keys are pressed (WSAD).
	 *
	 * @param api
	 */
	private void setKeyBindings(String api) {
		final KeyBindingManager keyboard = KeyBindingManager
				.getKeyBindingManager();

		keyboard.set("forward", KeyInput.KEY_W);
		keyboard.set("backward", KeyInput.KEY_S);
		keyboard.set("right", KeyInput.KEY_D);
		keyboard.set("left", KeyInput.KEY_A);
		keyboard.set("real_right", KeyInput.KEY_RIGHT);
		keyboard.set("real_left", KeyInput.KEY_LEFT);
		keyboard.set("down", KeyInput.KEY_DOWN);
		keyboard.set("up", KeyInput.KEY_UP);
	}

	/**
	 * assigns action classes to triggers. These jmetest.actions handle moving
	 * the node forward, backward and rotating it.
	 *
	 * @param node
	 *            the node to control.
	 */
	private void setActions(Ship node) {

		final AccelerateAction forward = new AccelerateAction(node,
				AccelerateAction.FORWARD);
		addAction(forward, "forward", true);
		final AccelerateAction back = new AccelerateAction(node,
				AccelerateAction.BACKWARD);
		addAction(back, "backward", true);
		final RotateAction rotateRight = new RotateAction(node,
				RotateAction.HORIZONTAL, RotateAction.RIGHT);
		addAction(rotateRight, "right", true);
		final RotateAction rotateLeft = new RotateAction(node,
				RotateAction.HORIZONTAL, RotateAction.LEFT);
		addAction(rotateLeft, "left", true);
		final RotateAction rotateRealRight = new RotateAction(node,
				RotateAction.HORIZONTAL, RotateAction.RIGHT);
		addAction(rotateRealRight, "real_right", true);
		final RotateAction rotateRealLeft = new RotateAction(node,
				RotateAction.HORIZONTAL, RotateAction.LEFT);
		addAction(rotateRealLeft, "real_left", true);
		final RotateAction rotateUp = new RotateAction(node,
				RotateAction.VERTICAL, RotateAction.RIGHT);
		addAction(rotateUp, "up", true);
		final RotateAction rotateDown = new RotateAction(node,
				RotateAction.VERTICAL, RotateAction.LEFT);
		addAction(rotateDown, "down", true);

	}
}