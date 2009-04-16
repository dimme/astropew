package client;

import java.util.PriorityQueue;

import com.jme.app.SimpleGame;
import com.jme.light.PointLight;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Spatial;
import com.jme.scene.state.LightState;
import com.jme.scene.state.MaterialState;

public class ObserverGame extends SimpleGame implements Game {

	protected GameLogic logic;
	protected final PriorityQueue<Command> commandQueue;
	protected final GameClient gc;

	public ObserverGame(GameClient gc) {
		commandQueue = new PriorityQueue<Command>(51);
		logic = new GameLogic();
		this.gc = gc;

		setConfigShowMode(ConfigShowMode.ShowIfNoConfig);
		final Thread t = new Thread() {
			public void run() {
				ObserverGame.this.start();
			}
		};
		t.start();
	}

	protected void simpleInitGame() {
		final LightState ltst = display.getRenderer().createLightState();
		ltst.setEnabled(true);

		final PointLight lt = new PointLight();
		lt.setLocation(new Vector3f(4, 2, 3));
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

	/* (non-Javadoc)
	 * @see client.Game#finish()
	 */
	public void finish() {
		gc.stop();
		super.finish();
	}

	/* (non-Javadoc)
	 * @see client.Game#updatePosition(com.jme.math.Vector3f, com.jme.math.Quaternion, com.jme.math.Vector3f, int, long)
	 */
	public void updatePosition(Vector3f pos, Quaternion ort, Vector3f dir,
			int id, long tick) {
		addCommand(new UpdatePositionCommand(id, pos, ort, dir, tick));
	}

	protected synchronized void addCommand(Command c) {
		commandQueue.add(c);
	}

	/* (non-Javadoc)
	 * @see client.Game#addPlayer(int, java.lang.String)
	 */
	public void addPlayer(int id, String name) {
		addCommand(new AddPlayerCommand(id, name));
	}
	
	/* (non-Javadoc)
	 * @see client.Game#addSelf(int, java.lang.String)
	 */
	public void addSelf(int id, String name) {
		addCommand(new AddSelfCommand(id, name));
	}

	/* (non-Javadoc)
	 * @see client.Game#removePlayer(int)
	 */
	public void removePlayer(int id) {
		addCommand(new RemovePlayerCommand(id));
	}

	/* (non-Javadoc)
	 * @see client.Game#createMaterialState()
	 */
	public MaterialState createMaterialState() {
		return display.getRenderer().createMaterialState();
	}

	/* (non-Javadoc)
	 * @see client.Game#attachToRoot(com.jme.scene.Spatial)
	 */
	public void attachToRoot(Spatial s) {
		rootNode.attachChild(s);
	}

	/* (non-Javadoc)
	 * @see client.Game#removeFromRoot(com.jme.scene.Spatial)
	 */
	public void removeFromRoot(Spatial s) {
		rootNode.detachChild(s);
	}

}
