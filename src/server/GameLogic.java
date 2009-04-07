package server;

import java.net.SocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import common.PackageType;
import common.PacketObserver;

public class GameLogic implements PacketObserver {
	
	private GameAdministration gadm;
	
	public GameLogic(GameAdministration gadm) {
		this.gadm = gadm;
	}
	
	public void packetReceived(byte[] data, SocketAddress sender) {
		byte ptype = data[0];
		
		switch (ptype) {
			case PackageType.LEAVING:
				//TODO: Should this be moved? To GameAdministration, perhaps?
				// Pro: Wouldn't need a reference to gadm.
				// Con: more observers to call...
				gadm.leaving(sender);
				break;
			default:
				Logger.getLogger(getClass().getName()).log(Level.WARNING,"Unhandled packet type: " + ptype);
		}
	}
	
}
