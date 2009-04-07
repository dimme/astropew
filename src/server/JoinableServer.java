package server;

import common.PacketReaderThread;

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
			
			DatagramSocket sock = new DatagramSocket();
			PacketReaderThread pread = new PacketReaderThread(sock);
			PacketSender ps = new PacketSender(cdb, sock);
			
			GameAdministration gadm = new GameAdministration(cdb, ps);
			GameLogic game = new GameLogic(gadm);
			
			pread.addPacketObserver(game);
			
			IncomingConnectionServer ics = new IncomingConnectionServer(34567, gadm);
			
			pread.start();
			ics.start();
			
		} catch (SocketException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
			System.exit(1);
		}
	}
}
