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

import common.world.Missile;
import common.world.Planet;
import common.world.Ship;
import common.world.Universe;
import common.world.WorldObject;

public class Game extends BaseHeadlessApp {

	private long last = 0;
	private final PacketSender ps;
	final ClientDB cdb;
	private long frameTime = 0;
	private float delta;
	private final GameLogic logic;
	private final PriorityQueue<Command> commandQueue;
	private Universe rootnode;
	private final long worldseed;
	private int missile_id = 0;
	
	private static final long FRAME_SPACING = 50;

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
		rootnode = new Universe(worldseed);
		rootnode.generate(new PlanetFactory());
	}
	
	protected void initSystem() {
		display = DisplaySystem.getDisplaySystem( "dummy" );
	}

	public void render(float unused) {
		long cur = System.currentTimeMillis();
		
		
		rootnode.updateGeometricState(delta, true);
		
		ps.sendToAll(PacketDataFactory.createPosition(frameTime, logic.getShips()));
		while (last + FRAME_SPACING > cur) {
			try {
				Thread.sleep(1);
			} catch (final InterruptedException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Interrupted while sleeping.");
				throw new RuntimeException(e);
			}
			cur = System.currentTimeMillis();
		}
		last += FRAME_SPACING;
	}

	public synchronized void update(float unused) {
		boolean future = false;
		final long old = frameTime;
		frameTime = System.currentTimeMillis();
		delta = 0.001f * (frameTime - old);
		
		while (!future && !commandQueue.isEmpty()) {
			final Command c = commandQueue.remove();
			if (c.getTime() > frameTime) {
				future=true;
				commandQueue.add(c);
			} else {
				c.perform(this, delta);
			}
		}
		
		rootnode.interpolate(delta, frameTime);
		
		for (Ship s : logic.getShips()) {
			Collection<WorldObject> collisions = new LinkedList<WorldObject>();
			rootnode.findCollisions(s, collisions);
			
			for (WorldObject wobj : collisions) {
				System.out.println(s.getOwner().getName() + " collided with " + wobj);
			}
		}
	}

	public void addClientJoiningCommand(String name, SocketAddress saddr) {
		addCommand( new ClientJoiningCommand(name, saddr) );
	}
	
	public void clientJoining(String name, SocketAddress saddr) {
		Client c = cdb.getClient(saddr);

		if (c == null) {
			c = cdb.createClient(name, saddr);

			final Ship s = new Ship(c);
			rootnode.attachChild(s);
			logic.addShip(s);

			final byte[] data = PacketDataFactory.createPlayerJoined(c.getID(),
					name);

			ps.sendToAll(data);
		}

		ps.controlledSend(PacketDataFactory.createInitializer(worldseed, c.getID(), name), c);
		sendPlayersInfo(c);
	}
	
	private void sendPlayersInfo(Client c) {
		final byte[] tmp = PacketDataFactory.createPlayersInfo(cdb, c);
		if (tmp.length > 2) {
			ps.controlledSend(tmp, c);
		}
	}

	public void addClientLeavingCommand(SocketAddress saddr) {
		addCommand(new ClientLeavingCommand(saddr));
	}
	
	public void clientLeaving(SocketAddress saddr) {
		final Client removed = cdb.removeClient(saddr);
		
		if (removed != null) {
			final Ship s = logic.removeShip(removed);
			rootnode.removeChild(s);

			final byte[] data = PacketDataFactory.createPlayerLeft(removed.getID());
			ps.sendToAll(data);
		}
	}

	public GameSettings getNewSettings() {
		return new ServerGameSettings();
	}

	public void updatePlayer(SocketAddress sender, Vector3f pos, Quaternion ort, Vector3f dir, long time) {
		addCommand(new PlayerUpdateCommand(sender, pos, ort, dir, time) );
	}

	private synchronized void addCommand(Command cmd) {
		commandQueue.add(cmd);
	}

	protected void cleanup() {
	}

	protected void reinit() {
	}

	public void addFireMissileCommand(SocketAddress sender, long t) {
		addCommand(new FireMissileCommand(sender, t) );
	}

	public void fireMissile(SocketAddress sender, long time) {
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
				Missile m = new Missile(missile_id++, pos, dir, c, time);
				rootnode.attachChild(m);
				ps.sendToAll(PacketDataFactory.createMissile(m));
			}
		}
	}
	
	private class PlanetFactory implements common.world.PlanetFactory {

		public Planet createPlanet(Vector3f center, float size, ColorRGBA c) {
			return new Planet(center, 3, 3, size);
		}
	}
}
