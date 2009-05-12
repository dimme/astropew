package server;

import java.net.SocketAddress;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import server.clientdb.Client;
import server.clientdb.ClientDB;
import server.command.Command;
import server.command.GameCommandInterface;
import server.command.SetHPCommand;
import server.world.Missile;

import com.jme.app.BaseHeadlessApp;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.system.DisplaySystem;
import com.jme.system.GameSettings;
import com.jme.util.Timer;
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
	private int objectId = 0;
	private final GameCommandInterface gci = new CommandInterface();
	private Timer timer;
	private int frameCount = 0;

	private static final long FRAME_SPACING = 10;
	private static final int RENDER_SPACING = 10;

	public Game(PacketSender ps, ClientDB cdb) {
		Random rnd = new Random();
		worldseed = rnd.nextLong();

		setConfigShowMode(ConfigShowMode.NeverShow);
		last = System.currentTimeMillis();
		this.ps = ps;
		this.cdb = cdb;
		commandQueue = new PriorityQueue<Command>();

		logic = new GameLogic(this);

		final Thread t = new Thread() {
			public void run() {
				Logger.getLogger("").setLevel(Level.WARNING);
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
		frameCount++;
		if (frameCount  == RENDER_SPACING) {
			frameCount = 0;

			ps.sendToAll(PacketDataFactory.createPosition(frameTime, logic.getShips()));

			for (WorldObject wobj : logic.getObjects()) {
				if (wobj.getHPChanged()) {
					System.out.println("hp update: " + wobj + " - " + wobj.getHP());
					wobj.resetHPChanged();
					ps.sendToAll(PacketDataFactory.createHPUpdate(wobj));
				}
			}
		}

		long cur = System.currentTimeMillis();
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

				s = new Ship(logic, objectId++,c, frameTime);
				universe.attachChild(s);
				logic.add(s);

				final byte[] data = PacketDataFactory.createPlayerJoined(c.getID(), name, s.getID());

				ps.controlledSendToAll(data);
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
				if (s.canFire(time)) {
					c.setPoints(c.getPoints()-1);
					s.setLastFireTime(time);
					s.interpolate(-1f,time);
					Vector3f pos = s.getLocalTranslation();
					Vector3f dir = s.getLocalRotation().getRotationColumn(2);
					dir.multLocal(200f);
					//dir.addLocal(s.getMovement());
					//pos = pos.add(dir.normalize().multLocal(1.5f));
					Missile m = new Missile(logic, objectId++, pos, dir, c, frameTime);
					addCommand(new SetHPCommand(m, 0, frameTime+2f));
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
			ps.controlledSendToAll(PacketDataFactory.createHPUpdate(obj));
		}

		public void spawn(Ship ship) {
			ship.getPosition().set(new Vector3f());
			ship.getOrientation().set(new Quaternion());
			ship.getMovement().set(new Vector3f());
			ship.setLastUpdate(frameTime);

			universe.attachChild(ship);
			logic.add(ship);

			ps.controlledSendToAll(PacketDataFactory.createSpawn(ship));

			ship.setHP(100, frameTime);
		}


	}

	private class PlanetFactory implements common.world.PlanetFactory {

		public Planet createPlanet(Vector3f center, float size, ColorRGBA c) {
			Planet p = new Planet(logic, objectId++, center, 3, 3, size);
			logic.add(p);
			return p;
		}
	}
}
