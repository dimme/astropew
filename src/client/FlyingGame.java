package client;

import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import client.world.SelfShip;

import com.jme.app.VariableTimestepGame;
import com.jme.image.Texture;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.controls.GameControl;
import com.jme.input.controls.GameControlManager;
import com.jme.input.controls.binding.KeyboardBinding;
import com.jme.input.controls.controller.CameraController;
import com.jme.light.PointLight;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Skybox;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Sphere;
import com.jme.scene.state.CullState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.ZBufferState;
import com.jme.scene.state.ZBufferState.TestFunction;
import com.jme.system.DisplaySystem;
import com.jme.system.GameSettings;
import com.jme.system.JmeException;
import com.jme.system.PropertiesGameSettings;
import com.jme.util.TextureManager;
import com.jme.util.Timer;

import common.world.Missile;
import common.world.Planet;
import common.world.Ship;
import common.world.Universe;

public class FlyingGame extends VariableTimestepGame implements Game {

	protected final PriorityQueue<Command> commandQueue;
	protected final GameLogic logic;
	protected final GameClient gc;
	protected Camera cam;
	protected final Player self;
	protected final int selfShipId;
	protected final Universe universe;
	private InputHandler inputHandler;
	private long lastUpdateTime;
	private CameraController cameraController;
	
	private Skybox skybox;

	public FlyingGame(int id, String name, int selfshipid, long seed, GameClient gc) {
		super();
		this.selfShipId = selfshipid;
		this.gc = gc;
		commandQueue = new PriorityQueue<Command>(51);
		universe = new Universe(seed);
		self = new Player(name, id);
		logic = new GameLogic(self);
		setConfigShowMode(ConfigShowMode.AlwaysShow);
	}

	protected void cleanup() {
		gc.stop();
	}

	protected void initGame() {
		
		skybox = buildSkyBox();
		universe.attachChild(skybox);
		
		universe.generate(new PlanetFactory());
		
		
		final ZBufferState buf = display.getRenderer().createZBufferState();
		buf.setEnabled(true);
		buf.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);
		universe.setRenderState(buf);

		final CullState cs = display.getRenderer().createCullState();
		cs.setCullFace(CullState.Face.Back);
		universe.setRenderState(cs);
		
		// LIGHTS
		final LightState ltst = display.getRenderer().createLightState();
		ltst.setEnabled(true);

		final PointLight lt = new PointLight();
		lt.setLocation(new Vector3f(4, 2, 3));
		lt.setDiffuse(ColorRGBA.white);
		lt.setAmbient(ColorRGBA.white);
		lt.setEnabled(true);

		ltst.attach(lt);

		universe.setRenderState(ltst);
		
		addPlayer(selfShipId, self);
		inputHandler = new FlyingGameInputHandler(self.getShip(), this);
		
		universe.updateRenderState();

		GameControlManager gcm = new GameControlManager();
        GameControl toggleCameraControl = gcm.addControl("toggleCamera");
        toggleCameraControl.addBinding(new KeyboardBinding(KeyInput.KEY_M));
        cameraController = new CameraController(self.getShip(), cam, toggleCameraControl);

        universe.addController(cameraController);

        FollowCameraPerspective p1 = new FollowCameraPerspective(new Vector3f(0f,1f,-5f));

        cameraController.addPerspective(p1);
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

	protected void render(float interpolation) {
		cameraController.update(interpolation);
		
		display.getRenderer().clearBuffers();
		display.getRenderer().draw(universe);
		//System.out.println("render " + interpolation);

	}

	protected void update(float interpolation) {
		//Timer.getTimer().
		lastUpdateTime = System.currentTimeMillis();
		Command c;
		synchronized (this) {
			while (!commandQueue.isEmpty()) {
				c = commandQueue.remove();
				c.perform(this, interpolation);
			}
		}
		Ship ship = self.getShip();

		inputHandler.update(interpolation);
		
		logic.interpolate(interpolation, lastUpdateTime);
		
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("exit")) {
			finished = true;
			//TODO: I input handler istället
		}
		
		universe.updateGeometricState(interpolation, true);
		universe.updateRenderState();
		
		gc.sender.send(PacketDataFactory.createPlayerUpdate(lastUpdateTime, ship));
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

	public void addPlayer(int id, String name, int shipid) {
		addPlayer( shipid, new Player(name, id) );
	}
	
	private void addPlayer(int shipid, Player p) {
		Ship s = createShip(shipid, p);
		logic.add(s);
	}

	private Ship createShip(int id, Player p) {
		Ship s;
		
		if (p == self) {
			s = new SelfShip(id, p);
			skybox.setLocalTranslation(s.getLocalTranslation());
		} else {
			s = new Ship(id, p);
		}
		
		MaterialState ms = display.getRenderer().createMaterialState();
		
		ms.setDiffuse(s.getColor().clone().multLocal(0.7f));
		ms.setAmbient(s.getColor().clone().multLocal(0.3f));
		s.setRenderState(ms);
		universe.attachChild(s);
		
		return s;
	}
	
	private Skybox buildSkyBox() {
		Skybox skybox = new Skybox("skybox", 10, 10, 10);

		final Texture tx = TextureManager.loadTexture("../files/galaxy.jpg",
				Texture.MinificationFilter.BilinearNearestMipMap,
				Texture.MagnificationFilter.Bilinear);

		skybox.setTexture(Skybox.Face.North, tx);
		skybox.setTexture(Skybox.Face.West, tx);
		skybox.setTexture(Skybox.Face.South, tx);
		skybox.setTexture(Skybox.Face.East, tx);
		skybox.setTexture(Skybox.Face.Up, tx);
		skybox.setTexture(Skybox.Face.Down, tx);
		skybox.preloadTextures();
		
		return skybox;
	}

	public void removePlayer(int id) {
		Ship removed = logic.remove(logic.getPlayer(id));
		universe.removeChild(removed);
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
				FlyingGame.this.start();
			}
		};
		t.start();
	}

	public void addMissile(int id, Vector3f pos, Vector3f dir, int ownerid, long time) {
		common.Player owner = logic.getPlayer(ownerid);
		Ship s = owner.getShip();
		Missile m = new Missile(id, pos, dir, owner, time);
		
		MaterialState ms = display.getRenderer().createMaterialState();
		
		ms.setDiffuse(s.getColor().clone().multLocal(0.7f));
		ms.setAmbient(s.getColor());
		m.setRenderState(ms);
		
		universe.attachChild(m);
		logic.add(m);
	}
	
	public void fireMissile(){
		Ship s = self.getShip();
		if (s.canFire(lastUpdateTime)) {
			s.setLastFireTime(lastUpdateTime);
			gc.sender.send(PacketDataFactory.createFireMissile(lastUpdateTime, self.getShip()));
		}
	}
	
	private class PlanetFactory implements common.world.PlanetFactory {
		private int object_id = 0;
		
		public Planet createPlanet(Vector3f center, float size, ColorRGBA c) {
			Planet p = new Planet(object_id++, center, 20, 20, size);
			
			final MaterialState ms = display.getRenderer().createMaterialState();
			ms.setDiffuse(c);
			ms.setAmbient(c.clone().multLocal(0.3f));
			p.setRenderState(ms);
			
			return p;
		}
	}

	public long getLastUpdate() {
		return lastUpdateTime;
	}
}
