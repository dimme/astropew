package server;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import server.clientdb.Client;
import server.clientdb.ClientDB;

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
	private final ClientDB cdb;
	private long frameTime = 0;
	private float ticklength;
	private final GameLogic logic;
	private final PriorityQueue<Command> commandQueue;
	
	private static final long FRAME_SPACING = 100;

	public Game(PacketSender ps, ClientDB cdb) {
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

	protected void simpleInitGame() {
		ticklength = 1f / timer.getResolution();
	}

	public void simpleRender() {
		long cur = System.currentTimeMillis();
		ps.sendToAll(PacketDataFactory.createPosition(frameTime, logic.getShips()));
		while (last + FRAME_SPACING > cur) {
			try {
				Thread.sleep(1);
			} catch (final InterruptedException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE,
						"Interrupted while sleeping.");
				throw new RuntimeException(e);
			}
			cur = System.currentTimeMillis();
		}
		last += FRAME_SPACING;
	}

	public synchronized void simpleUpdate() {
		final long old = frameTime;
		frameTime = timer.getTime();
		float delta = ticklength * (frameTime - old);
		while (!commandQueue.isEmpty()) {
			final Command c = commandQueue.remove();
			c.perform(cdb, delta);
		}
		/*Matrix3f rot = new Matrix3f();
		rot.fromAngleNormalAxis(delta, Vector3f.UNIT_Z);
		for (final Ship s : logic.getShips()) {
			s.getLocalRotation().apply(rot);
			s.getLocalTranslation().addLocal(
					s.getMovement().mult(delta));
		}*/
	}

	public void clientJoining(String name, SocketAddress saddr) {
		//TODO: move to command.
		Client c = cdb.getClient(saddr);

		if (c == null) {
			c = cdb.createClient(name, saddr);

			final Ship s = new Ship(c);
			rootNode.attachChild(s);
			logic.addShip(s);

			final byte[] data = PacketDataFactory.createPlayerJoined(c.getID(),
					name);

			ps.sendToAll(data);
		}

		ps.controlledSend(PacketDataFactory.createInitializer(12345, c.getID(), name), c);
		sendPlayersInfo(c);
	}
	
	private void sendPlayersInfo(Client c) {
		final byte[] tmp = PacketDataFactory.createPlayersInfo(cdb, c);
		if (tmp.length > 2) {
			ps.controlledSend(tmp, c);
		}
	}

	public void clientLeaving(SocketAddress saddr) {
		//TODO: move to command
		final Client removed = cdb.removeClient(saddr);

		final Ship s = logic.removeShip(removed);
		s.removeFromParent();

		final byte[] data = PacketDataFactory.createPlayerLeft(removed.getID());
		ps.sendToAll(data);
	}

	public GameSettings getNewSettings() {
		return new ServerGameSettings();
	}

	public void updatePlayer(int id, Vector3f pos, Quaternion ort, Vector3f dir, long time) {
		addCommand(new PlayerUpdateCommand(id, pos, ort, dir, time) );
	}

	private synchronized void addCommand(Command cmd) {
		commandQueue.add(cmd);
	}
}
