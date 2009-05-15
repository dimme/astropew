package client;

import java.awt.Font;
import java.util.Formatter;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import client.command.Command;
import client.command.GameCommandInterface;
import client.command.Message;
import client.world.OtherShip;
import client.world.SelfShip;
import client.world.TargetSprite;

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
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Skybox;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.CullState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.system.GameSettings;
import com.jme.system.JmeException;
import com.jme.system.PropertiesGameSettings;
import com.jme.util.TextureManager;
import com.jme.util.Timer;
import com.jmex.font2d.Font2D;
import com.jmex.font2d.Text2D;
import com.jmex.game.state.BasicGameState;
import com.jmex.game.state.GameStateManager;
import common.world.Missile;
import common.world.NoPlayer;
import common.world.Planet;
import common.world.Ship;
import common.world.Universe;
import common.world.WorldObject;

public class FlyingGame extends VariableTimestepGame implements Game {

	protected final PriorityQueue<Command> commandQueue;
	protected final GameLogic logic;
	protected final GameClient gc;
	protected Camera cam;
	protected final Player self;
	protected final int selfShipId;
	protected final Universe universe;
	private InputHandler inputHandler;
	private float lastUpdateTime;
	private CameraController cameraController;
	private Timer timer;
	private final GameCommandInterface gci;

	private ZBufferState targetSpriteZbufs;
	private TextureState targetSpriteTexture;
	private BlendState targetSpriteBlend;

	private BasicGameState playing;
	private BasicGameState connected;
	private BasicGameState messages;

	private Skybox skybox;

	private final long serverltime;
	private final float serverftime;
	private float timediff;
	private Text2D txtHP;
	private Text2D txtPoints;
	private Text2D txtSpeed;
	private TextBox scoreNode;
	private MessageBox msgbox;
	private float lastPosSend = 0;
	private final Vector3f oldMovement = new Vector3f();
	private final Quaternion oldRotation = new Quaternion();

	public FlyingGame(int id, String name, int selfshipid, long seed, GameClient gc, long serverltime, float serverftime) {
		super();
		this.gci = new CommandInterface();
		this.serverltime = serverltime;
		this.serverftime = serverftime;
		this.selfShipId = selfshipid;
		this.gc = gc;
		commandQueue = new PriorityQueue<Command>(51);
		universe = new Universe(seed);
		self = new Player(name, id);
		logic = new GameLogic(this, self);
		setConfigShowMode(ConfigShowMode.AlwaysShow);
	}

	protected void cleanup() {
		gc.stop();
	}

	protected void initGame() {

		playing = new BasicGameState("PlayingState");
		connected = new BasicGameState("ConnectedState");
		messages = new BasicGameState("MessageState");
		playing.setActive(false);
		connected.setActive(true);
		messages.setActive(true);

		GameStateManager gsm = GameStateManager.create();
		gsm.attachChild(playing);
		gsm.attachChild(connected);
		gsm.attachChild(messages);

		Node playingRoot = playing.getRootNode();

		timer = Timer.getTimer();
		timer.update();
		long curt = System.currentTimeMillis();
		timediff = 0.001f * (curt - serverltime) + serverftime - timer.getTimeInSeconds();

		System.out.println("sl: " + serverltime);
		System.out.println("sf: " + serverftime);
		System.out.println("cl: " + curt);
		System.out.println("cf: " + timer.getTimeInSeconds());
		System.out.println("timediff: " + timediff);

		skybox = buildSkyBox();
		universe.attachChild(skybox);

		Font2D f2d = new Font2D();
		txtHP = f2d.createText("000%", 1f, Font.PLAIN);
		txtHP.setLocalTranslation(display.getRenderer().getWidth()-130f, 0f, 0f);
		universe.attachChild(txtHP);
		txtSpeed = f2d.createText("200 pixels/hour :)", 1f, Font.PLAIN);
		txtSpeed.setLocalTranslation(display.getRenderer().getWidth()-130f, 20f, 0f);
		universe.attachChild(txtSpeed);
		txtPoints = f2d.createText("10000", 1f, Font.PLAIN);
		txtPoints.setLocalTranslation(0f, display.getHeight() - 20f, 0f);
		universe.attachChild(txtPoints);

		scoreNode = new TextBox("ScoreDisplay", 20f, display.getHeight()-20f);
		connected.getRootNode().attachChild(scoreNode);

		msgbox = new MessageBox("MessageBox", 5f, 0f);
		msgbox.setGrowUpwards(true);
		messages.getRootNode().attachChild(msgbox);

		universe.generate(new PlanetFactory());

		playingRoot.attachChild(universe);

		initTargetSpriteStates();

		final ZBufferState buf = display.getRenderer().createZBufferState();
		buf.setEnabled(true);
		buf.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);
		playingRoot.setRenderState(buf);

		final CullState cs = display.getRenderer().createCullState();
		cs.setCullFace(CullState.Face.Back);
		playingRoot.setRenderState(cs);

		//Wireframe
		/*final RenderState wfs = display.getRenderer().createWireframeState();
		playingRoot.setRenderState(wfs);*/

		// LIGHTS
		final LightState ltst = display.getRenderer().createLightState();
		ltst.setEnabled(true);

		ColorRGBA ltclr = ColorRGBA.white.clone().multLocal(0.3f);

		final PointLight lt = new PointLight();
		lt.setLocation(new Vector3f(1000, 0, 0));
		lt.setDiffuse(ltclr);
		lt.setAmbient(ltclr);
		lt.setEnabled(true);
		ltst.attach(lt);

		final PointLight lt2 = new PointLight();
		lt2.copyFrom(lt);
		lt2.setLocation(new Vector3f(0,1000,0));
		ltst.attach(lt2);

		final PointLight lt3 = new PointLight();
		lt3.copyFrom(lt);
		lt3.setLocation(new Vector3f(0,-1000,-1000));
		ltst.attach(lt3);

		final PointLight lt4 = new PointLight();
		lt4.copyFrom(lt);
		lt4.setLocation(new Vector3f(-1000,-1000,0));
		ltst.attach(lt4);

		playingRoot.setRenderState(ltst);

		addPlayer(selfShipId, self);
		inputHandler = new FlyingGameInputHandler(self.getShip(), this);

		playingRoot.updateRenderState();

		GameControlManager gcm = new GameControlManager();
        GameControl toggleCameraControl = gcm.addControl("toggleCamera");
        toggleCameraControl.addBinding(new KeyboardBinding(KeyInput.KEY_M));
        cameraController = new CameraController(self.getShip(), cam, toggleCameraControl);

        playingRoot.addController(cameraController);

        FollowCameraPerspective p1 = new FollowCameraPerspective(new Vector3f(0f,1f,-5f));

        cameraController.addPerspective(p1);
	}

	private void initTargetSpriteStates() {
		targetSpriteZbufs = display.getRenderer().createZBufferState();
		targetSpriteZbufs.setEnabled(true);
		targetSpriteZbufs.setFunction(ZBufferState.TestFunction.Always);
		targetSpriteZbufs.setWritable(false);

		targetSpriteTexture = display.getRenderer().createTextureState();
		final Texture targettext = TextureManager.loadTexture("../files/target.png",
				Texture.MinificationFilter.Trilinear,
				Texture.MagnificationFilter.Bilinear, 1.0f, true);
		targetSpriteTexture.setTexture(targettext);
		targetSpriteTexture.setEnabled(true);

		targetSpriteBlend = display.getRenderer().createBlendState();
		targetSpriteBlend.setBlendEnabled( true );
		targetSpriteBlend.setSourceFunctionAlpha( BlendState.SourceFunction.SourceAlpha );
		targetSpriteBlend.setDestinationFunctionAlpha( BlendState.DestinationFunction.OneMinusSourceAlpha );
		targetSpriteBlend.setBlendEquation( BlendState.BlendEquation.Add );
		targetSpriteBlend.setTestEnabled( false );
		targetSpriteBlend.setEnabled( true );
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
		GameStateManager.getInstance().render(interpolation);
		//display.getRenderer().draw(universe);
		//System.out.println("render " + interpolation);

	}

	protected void update(float interpolation) {
		lastUpdateTime = timer.getTimeInSeconds() + timediff;
		Command c;
		synchronized (this) {
			while (!commandQueue.isEmpty()) {
				c = commandQueue.remove();
				c.perform(gci);
			}
		}

		if (KeyBindingManager.getKeyBindingManager().isValidCommand("exit")) {
			finished = true;
		}

		if (playing.isActive()) {
			GameStateManager.getInstance().update(interpolation);

			Ship ship = self.getShip();

			oldMovement.set(ship.getMovement());
			oldRotation.set(ship.getLocalRotation());
			inputHandler.update(interpolation);

			logic.interpolate(interpolation, lastUpdateTime);

			universe.updateGeometricState(interpolation, true);
			universe.updateRenderState();

			if (lastPosSend + 0.5f < lastUpdateTime ||
					(lastPosSend + 0.05f < lastUpdateTime &&
					(!oldMovement.equals(ship.getMovement()) ||
					 !oldRotation.equals(ship.getLocalRotation())))) {
				lastPosSend = lastUpdateTime;
				gc.sender.send(PacketDataFactory.createPlayerUpdate(lastUpdateTime, ship));
			}

			updateHUD();
		}
		if (connected.isActive()) {
			String[] lines = new String[logic.getShips().size()+2];;

			lines[0] = "Scores:";
			lines[1] = "";

			int pos = 2;
			for (Ship s : logic.getShips()) {
				lines[pos] = s.getOwner().getName() + " " + s.getOwner().getPoints() + "p";
				pos++;
			}

			scoreNode.updateText(lines);
		}
		
		msgbox.update(lastUpdateTime);

		//System.out.println("update ");
	}

	private void updateHUD() {
		Ship ship = self.getShip();

		StringBuilder sb = new StringBuilder();
		Formatter fmt = new Formatter(sb);

		fmt.format("Hull:  %03d%%", Math.round(ship.getHP()));
		txtHP.setText(fmt.out().toString());
		sb.delete(0, sb.length());

		txtPoints.setText(self.getPoints() + "p");

		fmt.format("Speed: %03d", Math.round(10*ship.getMovement().length()));
		txtSpeed.setText(fmt.out().toString());
		//sb.delete(0, sb.length());
	}

	protected GameSettings getNewSettings() {
		PropertiesGameSettings gs =  new PropertiesGameSettings("properties.cfg");
		gs.load();

		return gs;
	}

	public synchronized void addCommand(Command cmd) {
		commandQueue.add(cmd);
	}

	private Ship createShip(int id, Player p) {
		Ship s;

		if (p == self) {
			s = new SelfShip(this, logic, id, p, lastUpdateTime);
			skybox.setLocalTranslation(s.getLocalTranslation());
		} else {
			TargetSprite ts = new TargetSprite(p.getName());
			s = new client.world.OtherShip(this, logic, id, p, lastUpdateTime, ts);
			s.attachChild(ts);


			MaterialState ms = display.getRenderer().createMaterialState();
			ColorRGBA c = s.getColor().clone();
			float max = Math.max(c.r, Math.max(c.g, c.b)) * 0.8f;
			c.r /= max;  c.g /= max;  c.b /= max;
			c.clamp();
			ms.setAmbient(c);
			ms.setDiffuse(ColorRGBA.black);
			ms.setEmissive(ColorRGBA.black);
			ms.setSpecular(ColorRGBA.black);
			ts.setRenderState(ms);
			ts.setRenderState(targetSpriteZbufs);
			ts.setRenderState(targetSpriteTexture);
			ts.setRenderState(targetSpriteBlend);
			ts.setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);
		}

		MaterialState ms = display.getRenderer().createMaterialState();

		ColorRGBA diffuse = s.getColor().clone();
		diffuse.clamp();
		ms.setDiffuse(diffuse);
		ms.setAmbient(s.getColor().clone().multLocal(0.2f));
		ms.setSpecular(ColorRGBA.white.clone().multLocal(0.1f));
		ms.setShininess(128f);
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

	public void startInThread() {
		final Thread t = new Thread() {
			public void run() {
				Logger.getLogger("").setLevel(Level.WARNING);
				FlyingGame.this.start();
			}
		};
		t.start();
	}

	private void addPlayer(int shipid, Player p) {
		if (logic.getPlayer(p.getID()) == null) {
			Ship s = createShip(shipid, p);
			logic.add(s);
		}
	}

	public Universe getUniverse() {
		return universe;
	}

	public void fireMissile(){
		Ship s = self.getShip();
		if (s.canFire(lastUpdateTime)) {
			s.setLastFireTime(lastUpdateTime);
			gc.sender.controlledSend(PacketDataFactory.createFireMissile(lastUpdateTime+0.2f));
		}
	}

	public void setPlaying(boolean b) {
		playing.setActive(b);
		connected.setActive(!b);
	}

	private class PlanetFactory implements common.world.PlanetFactory {
		private int object_id = 0;

		private final MaterialState ms;
		//private final TextureState ts;

		public PlanetFactory() {
			ms = display.getRenderer().createMaterialState();
			ms.setDiffuse(ColorRGBA.white);
			ms.setAmbient(ColorRGBA.white.clone().multLocal(0.3f));

			/*ts = display.getRenderer().createTextureState();
			final Texture texture = TextureManager.loadTexture("../files/moon.jpg",
					Texture.MinificationFilter.Trilinear,
					Texture.MagnificationFilter.Bilinear, 1.0f, true);
			ts.setTexture(texture);
			ts.setEnabled(true);*/
		}

		public Planet createPlanet(Vector3f center, float size, ColorRGBA c) {
			Planet p = new Planet(logic, object_id++, center, 20, 20, size);


			p.setRenderState(ms);
			//p.setRenderState(ts);

			logic.add(p);

			return p;
		}
	}

	private class CommandInterface implements GameCommandInterface {

		public void addMissile(int id, Vector3f pos, Vector3f dir, int ownerid, float time) {
			common.Player owner = logic.getPlayer(ownerid);
			Ship s = owner.getShip();
			Missile m = new Missile(logic, id, pos, dir, owner, time);

			MaterialState ms = display.getRenderer().createMaterialState();

			ms.setDiffuse(s.getColor().clone().multLocal(0.7f));
			ms.setAmbient(s.getColor());
			m.setRenderState(ms);

			universe.attachChild(m);
			logic.add(m);
		}

		public common.Player removePlayer(int id) {
			Ship removed = logic.remove(logic.getPlayer(id));
			universe.removeChild(removed);
			if (removed instanceof OtherShip) {
				((OtherShip)removed).removeTargetSprite();
			}
			return removed.getOwner();
		}

		public void updatePosition(Vector3f pos, Quaternion ort, Vector3f dir, int pid, float time) {
			final Ship s = logic.getShipByPlayerID(pid);
			if (s != null) {
				if (s.shouldUpdate(time)) {
					s.getPosition().set(pos);
					s.getOrientation().set(ort);
					s.getMovement().set(dir);
					s.setLastUpdate(time);
				}
			}
		}

		public void addPlayer(int id, String name, int shipid) {
			FlyingGame.this.addPlayer( shipid, new Player(name, id) );
		}

		public void updateObjectHP(int objid, int instigatorpid, float hp, float time) {
			WorldObject wobj = logic.getObject(objid);
			common.Player inst = logic.getPlayer(instigatorpid);

			if (wobj == null) {
				Logger.getLogger(getClass().getName()).log(Level.WARNING, "Update HP: Object was null!\n\t" +
					wobj + "(" + objid + "), " +
					inst + "(" + instigatorpid + "), " +
					hp + "hp, " +
					"@" + time);
			} else {

				if (inst == null) {
					inst = NoPlayer.instance;
					Logger.getLogger(getClass().getName()).log(Level.WARNING, "Update HP: Instigator was null!\n\t" +
						wobj + "(" + objid + "), " +
						inst + "(" + instigatorpid + "), " +
						hp + "hp, " +
						"@" + time);
				}

				wobj.setHP(hp, inst, time);
			}
		}

		public void spawn(int playerid, Vector3f pos, Quaternion ort, Vector3f dir, float time) {

			Ship s = logic.getShipByPlayerID(playerid);
			if ( s!=null ) {
				s.getPosition().set(pos);
				s.getOrientation().set(ort);
				s.getMovement().set(dir);
				s.getLocalTranslation().set(s.getPosition());
				s.getLocalRotation().set(s.getOrientation());
				s.setLastUpdate(time);

				universe.attachChild(s);
				logic.add(s);
				s.setHP(100, NoPlayer.instance, time);
				if (playerid == self.getID()) {
					//skybox.setLocalTranslation(s.getLocalTranslation());
					playing.setActive(true);
					connected.setActive(false);
				}
			}
		}

		public void updatePoints(int pid, int points) {
			common.Player pl = logic.getPlayer(pid);
			if (pl != null) {
				pl.setPoints(points);
			}
		}

		public void addMessage(Message m) {
			msgbox.addMessage(m);
		}

		public float getCurrentTime() {
			return lastUpdateTime;
		}
	}

	public float getLastUpdateTime() {
		return lastUpdateTime;
	}

}
