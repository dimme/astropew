package server.clientdb;

import java.net.DatagramPacket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import common.CatastrophicException;

public class ClientDatabase implements ClientDB {

	private final HashMap<Integer, Client> idmap;
	private final HashMap<SocketAddress, Client> addrmap;
	private int nextId;

	public ClientDatabase() {
		addrmap = new HashMap<SocketAddress, Client>();
		idmap = new HashMap<Integer, Client>();
		nextId = 1;
	}

	public synchronized Client createClient(String name, SocketAddress saddr) {
		Client c;

		c = addrmap.get(saddr);
		if (c != null) {
			return c;
		}

		try {
			final DatagramPacket dp = new DatagramPacket(new byte[1], 0, 0,
					saddr);
			c = new Client(dp, nextId, name);

			addrmap.put(saddr, c);
			idmap.put(nextId, c);

			nextId++;

			return c;

		} catch (final SocketException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					e.getMessage(), e);
			throw new RuntimeException(new CatastrophicException(e));
		}
	}

	public synchronized Client getClient(SocketAddress saddr) {
		return addrmap.get(saddr);
	}

	public synchronized Client getClient(int id) {
		return idmap.get(id);
	}

	public Collection<Client> getClients() {
		notify(); // To throw IllegalMonitorStateException if we're using this
		// without synchronizing on the ClientDatabase.
		return idmap.values();
	}

	public synchronized Client removeClient(SocketAddress saddr) {
		final Client c = addrmap.remove(saddr);
		idmap.values().remove(c);
		return c;
	}

	public synchronized Client removeClient(int id) {
		final Client c = idmap.remove(id);
		addrmap.values().remove(c);
		return c;
	}

	public synchronized Iterator<Client> iterator() {
		return idmap.values().iterator();
	}

}
