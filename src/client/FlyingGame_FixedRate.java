/*package client;

import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme.app.FixedLogicrateGame;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.light.PointLight;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Sphere;
import com.jme.scene.state.CullState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.system.GameSettings;
import com.jme.system.JmeException;
import com.jme.system.PropertiesGameSettings;

import common.world.Missile;
import common.world.Ship;
import common.world.Universe;

public class FlyingGame_FixedRate extends FixedLogicrateGame implements Game {

	protected final PriorityQueue<Command> commandQueue;
	protected final GameLogic logic;
	protected final GameClient gc;
	protected Camera cam;
	protected final Player self;
	protected final Universe rootnode;
	protected final int tps = 20;
	protected final float ticklength = 1f/tps;
	protected long lastRender = 0;
	private InputHandler inputHandler;
	private int missileCount = 0;
	private final int missileSend = 10;
	private long lastUpdateTick;

	public FlyingGame_FixedRate(int id, String name, long seed, GameClient gc) {
		super();
		this.gc = gc;
		commandQueue = new PriorityQueue<Command>(51);
		rootnode = new Universe(seed, new PlanetFactory());
		self = new Player(name, id);
		logic = new GameLogic(self);
		setConfigShowMode(ConfigShowMode.AlwaysShow);
	}

	protected void cleanup() {
		gc.stop();
	}

	protected void initGame() {
		setLogicTicksPerSecond(tps);
		
		rootnode.generate();
		
		
		final ZBufferState buf = display.getRenderer().createZBufferState();
		buf.setEnabled(true);
		buf.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);
		rootnode.setRenderState(buf);

		final CullState cs = display.getRenderer().createCullState();
		cs.setCullFace(CullState.Face.Back);
		rootnode.setRenderState(cs);
		
		// LIGHTS
		final LightState ltst = display.getRenderer().createLightState();
		ltst.setEnabled(true);

		final PointLight lt = new PointLight();
		lt.setLocation(new Vector3f(4, 2, 3));
		lt.setDiffuse(ColorRGBA.white);
		lt.setAmbient(ColorRGBA.white);
		lt.setEnabled(true);

		ltst.attach(lt);

		rootnode.setRenderState(ltst);
		
		addPlayer(self);
		inputHandler = new FlyingGameInputHandler(self.getShip(), this);
		
		rootnode.updateRenderState();
		
		lastRender = System.currentTimeMillis();
	}

	protected void initSystem() {
		
		int width = settings.getWidth();
		int height = settings.getHeight();
		int depth = settings.getDepth();
		int freq = settings.getFrequency();
		boolean fullscreen = settings.isFullscreen();
		
		cam = null;

		try {
			display = DisplaySystem.getDisplaySystem(settings.getRenderer());
			display.setTitle("AstroPew");
			display.createWindow(width, height, depth, freq, fullscreen);

			cam = display.getRenderer().createCamera(width, height);
		} catch (final JmeException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Could not create displaySystem", e);
			System.exit(1);
		}

		// set the background to black
		display.getRenderer().setBackgroundColor(ColorRGBA.black.clone());

		// initialize the camera
		cam.setFrustumPerspective(45.0f, (float) width / (float) height, 1, 5000);
		cam.setLocation(new Vector3f(0, 2, 4));
		cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);

		// Signal that we've changed our camera's location/frustum. 
		cam.update();

		display.getRenderer().setCamera(cam);

		KeyBindingManager.getKeyBindingManager().set("exit", KeyInput.KEY_ESCAPE);
	}

	protected void reinit() {
		throw new RuntimeException("I hope this method isn't called... ");
	}

	protected void render(float percentWithinTick) {
		long old = lastRender;
		lastRender = System.currentTimeMillis();
		float delta = 0.001f*(lastRender - old); // s since last render
		rootnode.updateGeometricState(delta, true);
		Ship ship = self.getShip();
		Vector3f z = ship.getLocalRotation().getRotationColumn(2).multLocal(10);
		Vector3f y = ship.getLocalRotation().getRotationColumn(1).multLocal(2);
		cam.getLocation().set(ship.getLocalTranslation());
		cam.getLocation().addLocal(y);
		cam.getLocation().addLocal(z);
		cam.lookAt(ship.getLocalTranslation(), z.normalize().multLocal(-1));
		cam.update();
		cam.apply();
		display.getRenderer().clearBuffers();
		display.getRenderer().draw(rootnode);
		//System.out.println("render " + delta);
	}
	
	public void fireMissile(){
		gc.sender.send(PacketDataFactory.createFireMissile(lastUpdateTick, self.getShip()));
	}

	protected void update(float unused) {
		lastUpdateTick = System.currentTimeMillis();
		System.out.println(lastUpdateTick);
		Command c;
		synchronized (this) {
			while (!commandQueue.isEmpty()) {
				c = commandQueue.remove();
				c.perform(this);
			}
		}
		Ship ship = self.getShip();
		ship.getRotationSpeed().set(new float[] {1,0,0,  0,1,0,  0,0,1});
		inputHandler.update(ticklength);
		
		ship.getPosition().addLocal(self.getShip().getMovement().mult(ticklength));
		ship.setLastUpdate(lastUpdateTick);
		
		gc.sender.send(PacketDataFactory.createPlayerUpdate(lastUpdateTick, ship));
		
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("exit")) {
			finished = true;
		}
		
		rootnode.updateRenderState();
		//System.out.println("update ");
	}

	protected GameSettings getNewSettings() {
		PropertiesGameSettings gs =  new PropertiesGameSettings("properties.cfg");
		gs.load();
		
		return gs;
	}

	public synchronized void addCommand(Command cmd) {
		commandQueue.add(cmd);
	}

	public void addPlayer(int id, String name) {
		addPlayer( new Player(name, id) );
	}
	
	private void addPlayer(Player p) {
		Ship s = createShip(p);
		logic.addShip(s);
	}

	private Ship createShip(Player p) {
		Ship s = new Ship(p);
		
		MaterialState ms = display.getRenderer().createMaterialState();
		System.out.println(p.getName() + " " + s.getColor());
		ms.setDiffuse(s.getColor().multLocal(0.7f));
		ms.setAmbient(s.getColor().multLocal(0.3f));
		s.setRenderState(ms);
		rootnode.attachChild(s);
		
		return s;
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
				s.resetGeometrics();
				s.setLastUpdate(tick);
			}
		}
	}

	public void startInThread() {
		final Thread t = new Thread() {
			public void run() {
				FlyingGame_FixedRate.this.start();
			}
		};
		t.start();
	}

	public void addMissile(int id, Vector3f pos, Vector3f dir, int ownerid, long time) {
		common.Player owner = logic.getPlayer(ownerid);
		Missile m = new Missile(id, pos, dir, owner, time);
		rootnode.attachChild(m);
	}
	
	private class PlanetFactory implements common.world.PlanetFactory {

		public Spatial createPlanet(String name, Vector3f center, float size, ColorRGBA c) {
			Spatial s = new Sphere(name, center, 20, 20, size);
			
			final MaterialState ms = display.getRenderer().createMaterialState();
			ms.setDiffuse(c);
			ms.setAmbient(c.multLocal(0.3f));
			s.setRenderState(ms);
			
			return s;
		}
	}

	public long getLastUpdate() {
		return lastUpdateTick;
	}
}
*/