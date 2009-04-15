package server;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import server.clientdb.Client;

import com.jme.app.SimpleHeadlessApp;
import com.jme.math.Matrix3f;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.system.AbstractGameSettings;
import com.jme.system.GameSettings;
import common.world.Ship;

public class Game extends SimpleHeadlessApp {

	private long last = 0;
	private final PacketSender ps;
	private long frameTime = 0;
	private float ticklength;
	private final GameLogic logic;

	public Game(PacketSender ps) {
		setConfigShowMode(ConfigShowMode.NeverShow);
		last = System.currentTimeMillis();
		this.ps = ps;

		logic = new GameLogic();

		final Thread t = new Thread() {
			public void run() {
				Game.this.start();
			}
		};
		t.start();
	}

	protected void simpleInitGame() {
		ticklength = 1f / timer.getResolution();
	}

	public void simpleRender() {
		long cur = System.currentTimeMillis();
		while (last + 10 > cur) {
			try {
				Thread.sleep(1);
			} catch (final InterruptedException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE,
						"Interrupted while sleeping.");
				throw new RuntimeException(e);
			}
			cur = System.currentTimeMillis();
		}
		last += 10;
		ps.sendToAll(PacketDataFactory.createPosition(frameTime, logic.getShips()));
	}

	public void simpleUpdate() {
		final long old = frameTime;
		frameTime = timer.getTime();
		float delta = ticklength * (frameTime - old);
		Matrix3f rot = new Matrix3f();
		rot.fromAngleNormalAxis(delta, Vector3f.UNIT_Z);
		for (final Ship s : logic.getShips()) {
			s.getLocalRotation().apply(rot);
			s.getLocalTranslation().addLocal(
					s.getMovement().mult(delta));
		}
	}

	public void newClient(Client c) {
		final Ship s = new Ship(c);
		rootNode.attachChild(s);
		logic.addShip(s);
	}

	public void clientLeaving(Client c) {
		final Ship s = logic.removeShip(c);
		s.removeFromParent();
	}

	public GameSettings getNewSettings() {
		return new AbstractGameSettings() {
			public void clear() throws IOException {
			}

			public String get(String name, String defaultValue) {
				return defaultValue;
			}

			public int getAlphaBits() {
				return 0;
			}

			public boolean getBoolean(String name, boolean defaultValue) {
				return defaultValue;
			}

			public byte[] getByteArray(String name, byte[] bytes) {
				return null;
			}

			public int getDepth() {
				return 0;
			}

			public int getDepthBits() {
				return 0;
			}

			public double getDouble(String name, double defaultValue) {
				return defaultValue;
			}

			public float getFloat(String name, float defaultValue) {
				return defaultValue;
			}

			public int getFramerate() {
				return 0;
			}

			public int getFrequency() {
				return 0;
			}

			public int getHeight() {
				return 0;
			}

			public int getInt(String name, int defaultValue) {
				return defaultValue;
			}

			public long getLong(String name, long defaultValue) {
				return defaultValue;
			}

			public Object getObject(String name, Object obj) {
				return obj;
			}

			public String getRenderer() {
				return "";
			}

			public int getSamples() {
				return 0;
			}

			public int getStencilBits() {
				return 0;
			}

			public int getWidth() {
				return 0;
			}

			public boolean isFullscreen() {
				return false;
			}

			public boolean isMusic() {
				return false;
			}

			public boolean isSFX() {
				return false;
			}

			public boolean isVerticalSync() {
				return false;
			}

			public void save() throws IOException {
			}

			public void set(String name, String value) {
			}

			public void setAlphaBits(int alphaBits) {
			}

			public void setBoolean(String name, boolean value) {
			}

			public void setByteArray(String name, byte[] bytes) {
			}

			public void setDepth(int depth) {
			}

			public void setDepthBits(int depthBits) {
			}

			public void setDouble(String name, double value) {
			}

			public void setFloat(String name, float value) {
			}

			public void setFramerate(int framerate) {
			}

			public void setFrequency(int frequency) {
			}

			public void setFullscreen(boolean fullscreen) {
			}

			public void setHeight(int height) {
			}

			public void setInt(String name, int value) {
			}

			public void setLong(String name, long value) {
			}

			public void setMusic(boolean musicEnabled) {
			}

			public void setObject(String name, Object obj) {
			}

			public void setRenderer(String renderer) {
			}

			public void setSFX(boolean sfxEnabled) {
			}

			public void setSamples(int samples) {
			}

			public void setStencilBits(int stencilBits) {
			}

			public void setVerticalSync(boolean vsync) {
			}

			public void setWidth(int width) {
			}
		};
	}
}
