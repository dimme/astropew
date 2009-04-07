/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

import java.net.SocketAddress;

import common.PacketObserver;
import common.PackageType;
import common.Util;

/**
 *
 * @author jonsturk
 */
public class ConsoleNetworkObserver implements PacketObserver {

	public void packetReceived(byte[] data, SocketAddress saddr) {
		if (data[0] == PackageType.INITIALIZER) {
			int id = Util.getInt(data, 1);
			long randSeed = Util.getLong(data,5);
			System.out.println("Established contact with server. Got id: "+ id +". Got seed: "+ randSeed);
		}
		else if (data[0] == PackageType.PLAYER_LEFT) {
			int lid = Util.getInt(data, 1);
			System.out.println("PLayer Left. ID = " + lid);
		}
		else if (data[0] == PackageType.PLAYER_JOINED) {
			int nid = Util.getInt(data, 1);
			byte[] bt = new byte[data.length - 5];
			System.arraycopy(data, 5, bt, 0, bt.length);
			System.out.println("Player Joined. ID = " + nid + " Name = " + new String(bt));
		}
		else {
			System.out.println("Unhandled packet type: " + data[0]);
		}
	}

}
