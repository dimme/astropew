package server;

import java.net.SocketAddress;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import server.clientdb.Client;
import server.clientdb.ClientDB;

import com.jme.app.BaseHeadlessApp;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.system.DisplaySystem;
import com.jme.system.GameSettings;
import common.world.Ship;

public class Game extends BaseHeadlessApp {

	private long last = 0;
	private final PacketSender ps;
	private final ClientDB cdb;
	private long frameTime = 0;
	private float delta;
	private final GameLogic logic;
	private final PriorityQueue<Command> commandQueue;
	private Node rootnode;
	
	private static final long FRAME_SPACING = 1000;

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

	protected void initGame() {
		rootnode = new Node("root");
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
		final long old = frameTime;
		frameTime = System.currentTimeMillis();
		delta = 0.001f * (frameTime - old);
		while (!commandQueue.isEmpty()) {
			final Command c = commandQueue.remove();
			c.perform(cdb, delta);
		}
	}

	public void clientJoining(String name, SocketAddress saddr) {
		//TODO: move to command.
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
		
		if (removed != null) {
			final Ship s = logic.removeShip(removed);
			s.removeFromParent();

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
}
