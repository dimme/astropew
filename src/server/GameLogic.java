package server;

import java.net.SocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import common.PacketType;
import common.PacketObserver;

public class GameLogic extends common.GameLogic implements PacketObserver {
	
	private GameAdministration gadm;
	
	public GameLogic(GameAdministration gadm) {
		super(new server.world.World(gadm.ps));
		this.gadm = gadm;
		Thread t = new Thread() {
			public void run() {
				world.start();
			}
		};
		t.start();
	}
	
	public void packetReceived(byte[] data, SocketAddress sender) {
		byte ptype = data[0];
		
		switch (ptype) {
			case PacketType.LEAVING:
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
