package server;

import java.net.SocketAddress;
import java.util.Collection;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import server.clientdb.Client;
import server.clientdb.ClientDB;

import com.jme.app.BaseHeadlessApp;
import com.jme.bounding.BoundingSphere;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Sphere;
import com.jme.scene.state.MaterialState;
import com.jme.system.DisplaySystem;
import com.jme.system.GameSettings;
import com.jme.util.Timer;

import server.world.Missile;
import common.world.MobileObject;
import common.world.Planet;
import common.world.Ship;
import common.world.Universe;
import common.world.WorldObject;

public class Game extends BaseHeadlessApp {

	private long last = 0;
	private final PacketSender ps;
	final ClientDB cdb;
	private float frameTime = 0;
	private long frameTimeL = 0;
	private final GameLogic logic;
	private final PriorityQueue<Command> commandQueue;
	private Universe universe;
	private final long worldseed;
	private int object_id = 0;
	private final GameCommandInterface gci = new CommandInterface();
	private Timer timer;
	
	private int frame_counter = 0;
	
	private static final long FRAME_SPACING = 10;

	public Game(PacketSender ps, ClientDB cdb) {
		Random rnd = new Random();
		worldseed = rnd.nextLong();
		
		setConfigShowMode(ConfigShowMode.NeverShow);
		last = System.currentTimeMillis();
		this.ps = ps;
		this.cdb = cdb;
		commandQueue = new PriorityQueue<Command>();

		logic = new GameLogic();

		final Thread t = new Thread() {
			public void run() {
				Game.this.start();
			}
		};
		t.start();
	}

	protected void initGame() {
		universe = new Universe(worldseed);
		universe.generate(new PlanetFactory());
	}
	
	protected void initSystem() {
		display = DisplaySystem.getDisplaySystem( "dummy" );
		timer = Timer.getTimer();
	}

	public void render(float unused) {
		
		frame_counter++;
		if (frame_counter == 5) {
			frame_counter = 0;
			long cur = System.currentTimeMillis();
			
			ps.sendToAll(PacketDataFactory.createPosition(frameTime, logic.getShips()));
			while (last + FRAME_SPACING > cur) {
				try {
					Thread.sleep(last+FRAME_SPACING - cur);
				} catch (final InterruptedException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Interrupted while sleeping.");
					throw new RuntimeException(e);
				}
				cur = System.currentTimeMillis();
			}
			last += FRAME_SPACING;
		}
	}

	public float getFrameTime() {
		return frameTime;
	}
	
	public synchronized void update(float unused) {
		boolean future = false;
		timer.update();
		frameTimeL = System.currentTimeMillis();
		frameTime = timer.getTimeInSeconds();
		final float delta = timer.getTimePerFrame();
		
		while (!future && !commandQueue.isEmpty()) {
			final Command c = commandQueue.remove();
			if (c.getTime() > frameTime) {
				future=true;
				commandQueue.add(c);
			} else {
				c.perform(gci);
			}
		}
		
		logic.interpolate(delta, frameTime);
		universe.updateGeometricState(delta, true);
		
		logic.handleCollisions(universe);
	}

	private void sendPlayersInfo(Client c) {
		final byte[] tmp = PacketDataFactory.createPlayersInfo(cdb, c);
		if (tmp.length > 2) {
			ps.controlledSend(tmp, c);
		}
	}

	public GameSettings getNewSettings() {
		return new ServerGameSettings();
	}

	public synchronized void addCommand(Command cmd) {
		commandQueue.add(cmd);
	}

	protected void cleanup() {
	}

	protected void reinit() {
	}

	private class CommandInterface implements GameCommandInterface {
		
		public void clientJoining(String name, SocketAddress saddr) {
			Client c = cdb.getClient(saddr);
			final Ship s;
			
			if (c == null) {
				c = cdb.createClient(name, saddr);

				s = new Ship(object_id++,c, frameTime);
				universe.attachChild(s);
				logic.add(s);

				final byte[] data = PacketDataFactory.createPlayerJoined(c.getID(), name, s.getID());

				ps.sendToAll(data);
			} else {
				s = c.getShip();
			}

			ps.controlledSend(PacketDataFactory.createInitializer(worldseed, c.getID(), s.getID(), name, frameTimeL, frameTime), c);
			sendPlayersInfo(c);
		}
		
		public void clientLeaving(SocketAddress saddr) {
			final Client removed = cdb.removeClient(saddr);
			
			if (removed != null) {
				final Ship s = logic.remove(removed);
				universe.removeChild(s);

				final byte[] data = PacketDataFactory.createPlayerLeft(removed.getID());
				ps.sendToAll(data);
			}
		}
		
		public void fireMissile(SocketAddress sender, float time) {
			Client c = cdb.getClient(sender);
			if (c != null) {
				Ship s = c.getShip();
				if (s.canFire(time) || true) {
					s.setLastFireTime(time);
					s.interpolate(-1f,time);
					Vector3f pos = s.getLocalTranslation();
					Vector3f dir = s.getLocalRotation().getRotationColumn(2);
					dir.multLocal(200f);
					dir.addLocal(s.getMovement());
					//pos = pos.add(dir.normalize().multLocal(1.5f));
					Missile m = new Missile(Game.this, object_id++, pos, dir, c, frameTime);
					universe.attachChild(m);
					logic.add(m);
					ps.sendToAll(PacketDataFactory.createMissile(m));
				}
			}
		}

		public void playerUpdate(Vector3f pos, Quaternion ort, Vector3f dir, float time, SocketAddress sender) {
			Client c = cdb.getClient(sender);
			if (c != null) {
				Ship s = c.getShip();
				
				if (s.shouldUpdate(time)) {
					s.getPosition().set(pos);
					s.getOrientation().set(ort);
					s.getMovement().set(dir);
					s.setLastUpdate(time);
				}
			}
		}

		public void destroy(WorldObject obj) {
			universe.removeChild(obj);
			logic.remove(obj);
			byte[] data = PacketDataFactory.createDestroyObject(obj.getID());
			ps.sendToAll(data);
		}
		
		
	}
	
	private class PlanetFactory implements common.world.PlanetFactory {

		public Planet createPlanet(Vector3f center, float size, ColorRGBA c) {
			Planet p = new Planet(object_id++, center, 3, 3, size);
			logic.add(p);
			return p;
		}
	}
}
