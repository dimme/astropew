/*package client;

import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme.app.BaseGame;
import com.jme.app.FixedLogicrateGame;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.light.PointLight;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Spatial;
import com.jme.scene.state.LightState;
import com.jme.scene.state.MaterialState;
import com.jme.system.DisplaySystem;
import com.jme.system.GameSettings;
import com.jme.system.JmeException;
import com.jme.util.Timer;

public class FlyingGame extends FixedLogicrateGame implements Game {

	protected final PriorityQueue<Command> commandQueue;
	protected final GameLogic logic;
	protected GameClient gc;
	protected Camera cam;

	public FlyingGame() {
		commandQueue = new PriorityQueue<Command>(51);
		logic = new GameLogic();
	}

	protected void cleanup() {
		// TODO Auto-generated method stub
	}

	protected void initGame() {
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

	protected void initSystem() {
		// store the settings information
		int width = settings.getWidth();
		int height = settings.getHeight();
		int depth = settings.getDepth();
		int freq = settings.getFrequency();
		boolean fullscreen = settings.isFullscreen();
		
		cam = null;

		try {
			display = DisplaySystem.getDisplaySystem(settings.getRenderer());
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
		cam.setLocation(new Vector3f(200, 1000, 200));

		// Signal that we've changed our camera's location/frustum. 
		cam.update();

		display.getRenderer().setCamera(cam);

		KeyBindingManager.getKeyBindingManager().set("exit", KeyInput.KEY_ESCAPE);
	}

	@Override
	protected void reinit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void render(float percentWithinTick) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void update(float interpolation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected GameSettings getNewSettings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addPlayer(int id, String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addSelf(int id, String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void attachToRoot(Spatial s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MaterialState createMaterialState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeFromRoot(Spatial s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removePlayer(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGameClient(GameClient gc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updatePosition(Vector3f pos, Quaternion ort, Vector3f dir,
			int id, long tick) {
		// TODO Auto-generated method stub
		
	}
	
	
}*/
