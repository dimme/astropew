package server.clientdb;

import java.net.SocketAddress;
import java.util.Collection;

import server.CatastrophicException;

public interface ClientDB {

	public Client getClient(SocketAddress saddr);
	
	public Collection<Client> getClients();
	
	/**
	 * Check to see if saddr already exists. If not, create it and add to the DB.
	 * Calculate and set id. 
	 * @param name requested player name
	 * @param saddr SocketAddr for the client
	 * @throws CatastrophicException 
	 */
	public Client createClient(String name, SocketAddress saddr);
	
}
