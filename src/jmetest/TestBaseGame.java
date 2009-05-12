package jmetest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme.app.BaseGame;
import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.input.ChaseCamera;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.thirdperson.ThirdPersonMouseLook;
import com.jme.light.DirectionalLight;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Skybox;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Sphere;
import com.jme.scene.state.CullState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.system.JmeException;
import com.jme.util.TextureManager;
import com.jme.util.Timer;

public class TestBaseGame extends BaseGame {
	private static final Logger logger = Logger.getLogger(TestBaseGame.class
			.getName());

	// fence that will keep us in.
	private Skybox skybox;
	// the new player object
	private Ship battleShip;
	// private ChaseCamera chaser;
	protected InputHandler input;

	// the timer
	protected Timer timer;

	// Our camera object for viewing the scene
	private Camera cam;
	// The chase camera, this will follow our player as he zooms around the
	// level
	private ChaseCamera chaser;

	// the root node of the scene graph
	private Node scene;

	// display attributes for the window. We will keep these values
	// to allow the user to change them
	private int width, height, depth, freq;
	private boolean fullscreen;

	/**
	 * Main entry point of the application
	 */
	public static void main(String[] args) {
		final TestBaseGame app = new TestBaseGame();
		// We will load our own "fantastic" AstroPew logo. Yes, I'm an artist.
		try {
			app.setConfigShowMode(ConfigShowMode.AlwaysShow, new URL(
					"file:files/pew.jpeg"));
		} catch (final MalformedURLException e) {
			e.printStackTrace();
		}
		app.start();
	}

	/**
	 * During an update we look for the escape button and update the timer to
	 * get the framerate. Things are now starting to happen, so we will update
	 *
	 * @see com.jme.app.BaseGame#update(float)
	 */
	protected void update(float interpolation) {
		// update the time to get the framerate
		timer.update();
		interpolation = timer.getTimePerFrame();
		// update the keyboard input (move the player around)
		input.update(interpolation);
		// update the chase camera to handle the player moving around.
		chaser.update(interpolation);

		// we want to keep the skybox around our eyes, so move it with
		// the camera
		skybox.setLocalTranslation(cam.getLocation());

		// if escape was pressed, we exit
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("exit")) {
			finished = true;
		}

		// Because we are changing the scene (moving the skybox and player) we
		// need to update
		// the graph.
		scene.updateGeometricState(interpolation, true);
	}

	/**
	 * draws the scene graph
	 *
	 * @see com.jme.app.BaseGame#render(float)
	 */
	protected void render(float interpolation) {
		// Clear the screen
		display.getRenderer().clearBuffers();
		display.getRenderer().draw(scene);
	}

	/**
	 * initializes the display and camera.
	 *
	 * @see com.jme.app.BaseGame#initSystem()
	 */
	protected void initSystem() {
		// store the settings information
		width = settings.getWidth();
		height = settings.getHeight();
		depth = settings.getDepth();
		freq = settings.getFrequency();
		fullscreen = settings.isFullscreen();

		try {
			display = DisplaySystem.getDisplaySystem(settings.getRenderer());
			display.createWindow(width, height, depth, freq, fullscreen);

			cam = display.getRenderer().createCamera(width, height);
		} catch (final JmeException e) {
			logger.log(Level.SEVERE, "Could not create displaySystem", e);
			System.exit(1);
		}

		// set the background to black
		display.getRenderer().setBackgroundColor(ColorRGBA.black.clone());

		// initialize the camera
		cam.setFrustumPerspective(45.0f, (float) width / (float) height, 1,
				5000);
		cam.setLocation(new Vector3f(200, 1000, 200));

		/** Signal that we've changed our camera's location/frustum. */
		cam.update();

		/** Get a high resolution timer for FPS updates. */
		timer = Timer.getTimer();

		display.getRenderer().setCamera(cam);

		KeyBindingManager.getKeyBindingManager().set("exit",
				KeyInput.KEY_ESCAPE);
	}

	/**
	 * initializes the scene
	 *
	 * @see com.jme.app.BaseGame#initGame()
	 */
	protected void initGame() {
		display.setTitle("AstroPew");

		scene = new Node("Scene graph node");
		/**
		 * Create a ZBuffer to display pixels closest to the camera above
		 * farther ones.
		 */
		final ZBufferState buf = display.getRenderer().createZBufferState();
		buf.setEnabled(true);
		buf.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);
		scene.setRenderState(buf);

		// Time for a little optimization. We don't need to render back face
		// triangles, so lets
		// not. This will give us a performance boost for very little effort.
		final CullState cs = display.getRenderer().createCullState();
		cs.setCullFace(CullState.Face.Back);
		scene.setRenderState(cs);

		// Light the world
		buildLighting();
		// Add the skybox
		buildSkyBox();
		// Build the player
		buildPlayer();
		// Build the universe
		buildUniverse();
		// build the chase cam
		buildChaseCamera();
		// build the player input
		buildInput();

		// update the scene graph for rendering
		scene.updateGeometricState(0.0f, true);
		scene.updateRenderState();
	}

	/**
	 * we are going to build the player object here. Now, we will load a .3ds
	 * model and convert it to .jme in realtime. The next lesson will show how
	 * to store as .jme so this conversion doesn't have to take place every
	 * time.
	 *
	 * We now have a Vehicle object that represents our player. The vehicle
	 * object will allow us to have multiple vehicle types with different
	 * capabilities.
	 *
	 */
	private void buildPlayer() {
		final Box b = new Box("box", new Vector3f(), 0.35f, 0.25f, 0.5f);
		b.setModelBound(new BoundingBox());
		b.updateModelBound();

		// set the vehicles attributes (these numbers can be thought
		// of as Unit/Second).
		battleShip = new Ship("The Ship", b);
		battleShip.setAcceleration(50);
		battleShip.setBraking(100);
		battleShip.setTurnSpeed(2.5f);
		battleShip.setMaxSpeed(250);
		battleShip.setMinSpeed(0);

		battleShip.setLocalTranslation(new Vector3f(100, 0, 100));
		scene.attachChild(battleShip);
		scene.updateGeometricState(0, true);
		battleShip.setRenderQueueMode(Renderer.QUEUE_OPAQUE);
	}

	private void buildUniverse() {
		final Random rand = new Random();

		final TextureState ts1 = display.getRenderer().createTextureState();
		final Texture texture1 = TextureManager.loadTexture("files/earth.jpg",
				Texture.MinificationFilter.Trilinear,
				Texture.MagnificationFilter.Bilinear, 1.0f, true);
		ts1.setTexture(texture1);
		ts1.setEnabled(true);

		final TextureState ts2 = display.getRenderer().createTextureState();
		final Texture texture2 = TextureManager.loadTexture("files/moon.jpg",
				Texture.MinificationFilter.Trilinear,
				Texture.MagnificationFilter.Bilinear, 1.0f, true);
		ts2.setTexture(texture2);
		ts2.setEnabled(true);

		for (int i = 0; i < 300; i++) {
			final float x = rand.nextInt(10000) - 5000;
			final float y = rand.nextInt(10000) - 5000;
			final float z = rand.nextInt(10000) - 5000;

			final Vector3f location = new Vector3f(x, y, z);
			final int radius = rand.nextInt(200) + 10;

			// float r = rand.nextFloat();
			// float g = rand.nextFloat();
			// float b = rand.nextFloat();
			// ColorRGBA color = new ColorRGBA(r,g,b,1);
			// MaterialState ms = display.getRenderer().createMaterialState();
			// ms.setEmissive(color);
			// ms.setAmbient(color);

			final Sphere s = new Sphere("Planet: " + i, location, 40, 40,
					radius);

			if (i % 2 == 0) {
				s.setRenderState(ts1);
			} else {
				s.setRenderState(ts2);
			}

			scene.attachChild(s);
		}

	}

	/**
	 * creates a light for the terrain.
	 */
	private void buildLighting() {
		/** Set up a basic, default light. */
		final DirectionalLight light1 = new DirectionalLight();
		light1.setDiffuse(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
		light1.setAmbient(new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));
		light1.setDirection(new Vector3f(1, -1, 0));
		light1.setEnabled(true);

		final DirectionalLight light2 = new DirectionalLight();
		light2.setDiffuse(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
		light2.setAmbient(new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));
		light2.setDirection(new Vector3f(1, 1, 0));
		light2.setEnabled(true);

		/** Attach the light to a lightState and the lightState to rootNode. */
		final LightState lightState = display.getRenderer().createLightState();
		lightState.setEnabled(true);
		lightState.attach(light1);
		lightState.attach(light2);
		scene.setRenderState(lightState);
	}

	/**
	 * buildSkyBox creates a new skybox object with all the proper textures. The
	 * textures used are the standard skybox textures from all the tests.
	 *
	 */
	private void buildSkyBox() {
		skybox = new Skybox("skybox", 10, 10, 10);

		final Texture tx = TextureManager.loadTexture("files/galaxy.jpg",
				Texture.MinificationFilter.BilinearNearestMipMap,
				Texture.MagnificationFilter.Bilinear);

		skybox.setTexture(Skybox.Face.North, tx);
		skybox.setTexture(Skybox.Face.West, tx);
		skybox.setTexture(Skybox.Face.South, tx);
		skybox.setTexture(Skybox.Face.East, tx);
		skybox.setTexture(Skybox.Face.Up, tx);
		skybox.setTexture(Skybox.Face.Down, tx);
		skybox.preloadTextures();
		scene.attachChild(skybox);
	}

	/**
	 * set the basic parameters of the chase camera. This includes the offset.
	 * We want to be behind the vehicle and a little above it. So we will the
	 * offset as 0 for x and z, but be 1.5 times higher than the node.
	 *
	 * We then set the roll out parameters (2 units is the closest the camera
	 * can get, and 5 is the furthest).
	 *
	 */
	private void buildChaseCamera() {
		final Vector3f targetOffset = new Vector3f();
		targetOffset.y = ((BoundingBox) battleShip.getWorldBound()).yExtent * 1.5f;
		final HashMap<String, Object> props = new HashMap<String, Object>();
		props.put(ThirdPersonMouseLook.PROP_MAXROLLOUT, "6");
		props.put(ThirdPersonMouseLook.PROP_MINROLLOUT, "3");
		props.put(ThirdPersonMouseLook.PROP_MAXASCENT, "" + 45
				* FastMath.DEG_TO_RAD);
		props.put(ChaseCamera.PROP_INITIALSPHERECOORDS, new Vector3f(5, 0,
				30 * FastMath.DEG_TO_RAD));
		props.put(ChaseCamera.PROP_TARGETOFFSET, targetOffset);
		chaser = new ChaseCamera(cam, battleShip, props);
		chaser.setMaxDistance(8);
		chaser.setMinDistance(2);
	}

	/**
	 * create our custom input handler.
	 *
	 */
	private void buildInput() {
		input = new FlagRushHandler(battleShip, settings.getRenderer());
	}

	/**
	 * will be called if the resolution changes
	 *
	 * @see com.jme.app.BaseGame#reinit()
	 */
	protected void reinit() {
		display.recreateWindow(width, height, depth, freq, fullscreen);
	}

	/**
	 * close the window and also exit the program.
	 */
	protected void quit() {
		super.quit();
		System.exit(0);
	}

	/**
	 * clean up the textures.
	 *
	 * @see com.jme.app.BaseGame#cleanup()
	 */
	protected void cleanup() {

	}
}