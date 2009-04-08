package server;

import java.net.SocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import common.ClientPacketType;
import common.GameException;
import common.PacketObserver;

public class PacketDecoder implements PacketObserver {

	private GameAdministration gadm;
	private Game game;
	
	public PacketDecoder(GameAdministration gadm, Game game) {
		this.gadm = gadm;
		this.game = game;
	}
	
	public void packetReceived(byte[] data, SocketAddress sender) {
		byte ptype = data[0];
		
		switch (ptype) {
			case ClientPacketType.LEAVING:
				gadm.leaving(sender);
				break;
			default:
				Logger.getLogger(getClass().getName()).log(Level.WARNING,"Unhandled packet type: " + ptype);
		}
	}
	
	public void newConnection(byte[] data, SocketAddress sender) {
		String name = new String(data);
		
		gadm.newConnection(name, sender);
		
	}

}
