package server;

import common.network.PacketReaderThread;

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
			
			DatagramSocket sock = new DatagramSocket(34567); //TODO: configurable port and Service Announcer
			PacketReaderThread pread = new PacketReaderThread(sock);
			PacketSender ps = new PacketSender(cdb, sock, pread);
			
			Game game = new Game(ps);
			GameAdministration gadm = new GameAdministration(cdb, ps, game);
			
			PacketDecoder pd = new PacketDecoder(gadm, game);
			
			pread.addPacketObserver(pd);
			pread.addPacketObserver(new AckingObserver(ps, cdb));
			
			//IncomingConnectionServer ics = new IncomingConnectionServer(34567, pd);
			
			pread.start();
			//ics.start();
			
		} catch (SocketException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
			System.exit(1);
		}
	}
}
