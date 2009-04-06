package server;

import java.util.ArrayList;

public class ClientHolder implements InfoRouter {
	private ArrayList<Client> clients;

	public ClientHolder(){
		clients = new ArrayList<Client>();
	}
	
	public boolean addClient(Client c){
		return clients.add(c);
	}
	
	public boolean removeClient(Client c){
		return clients.remove(c);
	}
		
	public ArrayList<Client> nearClients(Client c) {
		if (clients.contains(c))
			return clients;
		return new ArrayList<Client>();//empty
	}

}
