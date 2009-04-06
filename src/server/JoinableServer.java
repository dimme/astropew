package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

import server.clientdb.ClientDB;
import server.clientdb.ClientDatabase;

public class JoinableServer extends Thread {
	
	public static void main(String[] args) {
		
		new JoinableServer();
		
	}
	
	public JoinableServer() {
		
		try {
			
			ClientDB cdb = new ClientDatabase();
			PacketSender ps = new PacketSender();
			
			GameAdministration gadm = new GameAdministration(cdb, ps);
			
			IncomingConnectionServer ics = new IncomingConnectionServer(34567, gadm);
			ics.start();
			
		} catch (SocketException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
			System.exit(1);
		}
	}
}
