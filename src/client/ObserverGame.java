package client;

import java.util.PriorityQueue;

import com.jme.app.SimpleGame;
import com.jme.light.PointLight;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.state.LightState;
import com.jme.scene.state.MaterialState;
import common.world.Ship;

public class ObserverGame extends SimpleGame implements Game {

	protected GameLogic logic;
	protected final PriorityQueue<Command> commandQueue;
	protected final GameClient gc;
	protected final Player self;
	private long lastUpdate;

	public ObserverGame(int id, String name, GameClient gc) {
		commandQueue = new PriorityQueue<Command>(51);
		this.gc = gc;
		
		lastUpdate = System.currentTimeMillis();
		
		self = new Player(name, id);
		logic = new GameLogic(self);
		
		setConfigShowMode(ConfigShowMode.ShowIfNoConfig);
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
		
		addPlayer(self);
	}

	protected synchronized void simpleUpdate() {
		Command c;
		long cur = System.currentTimeMillis();
		float delta = 0.001f * (cur - lastUpdate);
		lastUpdate = cur;
		while (!commandQueue.isEmpty()) {
			c = commandQueue.remove();
			c.perform(this, delta);
		}
	}

	/* (non-Javadoc)
	 * @see client.Game#cleanup()
	 */
	public void cleanup() {
		gc.stop();
	}

	public synchronized void addCommand(Command c) {
		commandQueue.add(c);
	}

	private Ship createShip(common.Player p) {
		final Ship s = new Ship(p);
		
		ColorRGBA c = s.getColor();
		
		if ( p == self ) {
			System.out.println("----------CREATING SELF-SHIP----------");
			c = ColorRGBA.cyan;
		} else {
			System.out.println("----------CREATING SHIP----------");
		}
		
		//TODO: Why don't we get a special color on the mothership?
		final MaterialState ms = display.getRenderer().createMaterialState();
		ms.setDiffuse(c);
		ms.setEmissive(c.multLocal(0.5f));
		ms.setAmbient(c.multLocal(0.5f));
		s.setRenderState(ms);
		rootNode.attachChild(s);
		
		return s;
	}

	public void addPlayer(int id, String name) {
		addPlayer( new Player(name, id) );
	}
	
	private void addPlayer(Player p) {
		Ship s = createShip(p);
		logic.addShip(s);
	}

	public void removePlayer(int id) {
		Ship removed = logic.removeShip(logic.getPlayer(id));
		removed.removeFromParent();
	}

	public void updatePosition(Vector3f pos, Quaternion ort, Vector3f dir, int id, long tick) {
		final Ship s = logic.getShip(id);
		if (s != null) {
			if (s.shouldUpdate(tick)) {
				s.getPosition().set(pos);
				s.getOrientation().set(ort);
				s.getMovement().set(dir);
				s.setLastUpdate(tick);
			}
		}
	}

	public void startInThread() {
		final Thread t = new Thread() {
			public void run() {
				ObserverGame.this.start();
			}
		};
		t.start();
	}

	public void addMissile(int id, Vector3f pos, Vector3f dir, int ownerid, long creationtime) {
		// TODO Auto-generated method stub
		
	}

	public void fireMissile() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getLastUpdate() {
		return System.currentTimeMillis();
	}

}
