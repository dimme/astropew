package server.clientdb;

import java.net.DatagramPacket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import server.CatastrophicException;

public class ClientDatabase implements ClientDB{
	
	private HashMap<Integer, Client> id2Client;
	private HashMap<SocketAddress, Integer> addr2Id;
	private int nextId;
	
	public ClientDatabase(){
		addr2Id = new HashMap<SocketAddress, Integer>();
		id2Client = new HashMap<Integer, Client>();
		nextId = 0;
	}
	
	public synchronized Client createClient(String name, SocketAddress saddr){
		if(addr2Id.containsKey(saddr)){
			return id2Client.get(addr2Id.get(saddr));
		}
		
		int size = 65000;
		byte[] b = new byte[size];
		
		DatagramPacket dp = null;
		try {
			dp = new DatagramPacket(b, size, saddr);			
			
		} catch (SocketException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
			throw new RuntimeException(new CatastrophicException(e));
		}
		
		Client c = new Client(dp,nextId);
		
		addr2Id.put(saddr, nextId);
		id2Client.put(nextId,c);
		
		nextId++;
				
		return c;
	}

	public synchronized Client getClient(SocketAddress saddr) {
		if(addr2Id.containsKey(saddr)){
			return id2Client.get(addr2Id.get(saddr));
		}
		return null;
	}

	public synchronized Collection<Client> getClients() {
		return id2Client.values();
	}

}
