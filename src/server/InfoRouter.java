package server;

import java.util.ArrayList;

public interface InfoRouter {
	
	public ArrayList<Client> nearClients(Client c);
}
