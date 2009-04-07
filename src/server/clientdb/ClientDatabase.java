package server.clientdb;

import java.net.DatagramPacket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import common.CatastrophicException;
import common.Util;

public class ClientDatabase implements ClientDB{
	
	private HashMap<Integer, Client> idmap;
	private HashMap<SocketAddress, Client> addrmap;
	private int nextId;
	
	public ClientDatabase(){
		addrmap = new HashMap<SocketAddress, Client>();
		idmap = new HashMap<Integer, Client>();
		nextId = 1;
	}
	
	public synchronized Client createClient(String name, SocketAddress saddr){
		Client c;
		
		c = addrmap.get(saddr);
		if( c != null){
			return c;
		}
		
		try {
			DatagramPacket dp = new DatagramPacket(new byte[Util.PACKET_SIZE], Util.PACKET_SIZE, saddr);			
			c = new Client(dp,nextId);
			
			addrmap.put(saddr,c);
			idmap.put(nextId,c);
			
			nextId++;
					
			return c;
			
		} catch (SocketException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
			throw new RuntimeException(new CatastrophicException(e));
		}
	}

	public synchronized Client getClient(SocketAddress saddr) {
		return addrmap.get(saddr);
	}
	
	public synchronized Client getClient(int id) {
		return idmap.get(id);
	}

	public synchronized Collection<Client> getClients() {
		return idmap.values();
	}

	public void removeClient(SocketAddress saddr) {
		Client c = addrmap.remove(saddr);
		idmap.values().remove(c);
	}

	public void removeClient(int id) {
		Client c = idmap.remove(id);
		addrmap.values().remove(c);
	}

}
