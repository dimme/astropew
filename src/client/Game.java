package client;

import java.util.List;
import java.util.PriorityQueue;

import com.jme.app.SimpleGame;
import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Spatial;
import com.jme.scene.state.LightState;
import com.jme.scene.state.MaterialState;

import common.world.NoPlayer;
import common.world.Ship;

public class Game extends SimpleGame {
	
	protected GameLogic logic;
	private PriorityQueue<Command> commandQueue;
	
	public Game() {
		commandQueue = new PriorityQueue<Command>(51);
		logic = new GameLogic();
		
		setConfigShowMode(ConfigShowMode.ShowIfNoConfig);
		Thread t = new Thread() {
			public void run() {
				Game.this.start();
			}
		};
		t.start();
	}
	
	protected void simpleInitGame() {
		LightState ltst = display.getRenderer().createLightState();
		ltst.setEnabled(true);
		
		PointLight lt = new PointLight();
		lt.setLocation(new Vector3f(4,2,3));
		lt.setDiffuse(ColorRGBA.white);
		lt.setAmbient(ColorRGBA.white);
		lt.setEnabled(true);
		
		ltst.attach(lt);
		
		rootNode.setRenderState(ltst);
	}
	
	protected synchronized void simpleUpdate() {
		Command c;
		while (!commandQueue.isEmpty()) {
			c = commandQueue.remove();
			c.perform(logic, this);
		}
	}

	public void updatePosition(Vector3f pos, Vector3f dir, Vector3f ort, int id, long tick) {
		addCommand(new UpdatePositionCommand(id, pos, dir, ort, tick));
	}
	
	protected synchronized void addCommand(Command c) {
		commandQueue.add(c);
	}

	public void addPlayer(int id, String name) {
		addCommand(new AddPlayerCommand(id, name));
	}

	public MaterialState createMaterialState() {
		return display.getRenderer().createMaterialState();
	}

	public void attachToRoot(Spatial s) {
		rootNode.attachChild(s);
	}

}
