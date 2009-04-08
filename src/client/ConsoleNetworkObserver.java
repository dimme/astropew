/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

import java.net.SocketAddress;

import com.jme.math.Vector3f;

import common.PacketObserver;
import common.ServerPacketType;
import common.Util;

/**
 *
 * @author jonsturk
 */
public class ConsoleNetworkObserver implements PacketObserver {

	public void packetReceived(byte[] data, SocketAddress saddr) {
		if (data[0] == ServerPacketType.INITIALIZER) {
			String name = new String(data, 13, data.length-13);
			int id = Util.getInt(data, 9);
			long randSeed = Util.getLong(data,1);
			System.out.println("Established contact with server. Got id: "+ id +". Got name: "+name+". Got seed: "+ randSeed);
		} else if (data[0] == ServerPacketType.PLAYER_LEFT) {
			int lid = Util.getInt(data, 1);
			System.out.println("Player Left. ID = " + lid);
		} else if (data[0] == ServerPacketType.PLAYER_JOINED) {
			int nid = Util.getInt(data, 1);
			byte[] bt = new byte[data.length - 5];
			System.arraycopy(data, 5, bt, 0, bt.length);
			System.out.println("Player Joined. ID = " + nid + " Name = " + new String(bt));
		} else if (data[0] == ServerPacketType.PLAYER_POSITION){
			System.out.println("Recieved a player_position packet");
			System.out.println("The time was: "+Util.getLong(data, 1));
			System.out.println("The player was: "+Util.getInt(data, 9));
			Vector3f v = new Vector3f();
			Util.getVector3f(data, 13, v);
			System.out.println(v);
			Util.getVector3f(data, 25, v);
			System.out.println(v);
			Util.getVector3f(data, 37, v);
			System.out.println(v);
		} else if (data[0] == ServerPacketType.MESSAGE) {
			System.out.println("Recieved msg: "+new String(data,1,data.length-1));
		} else {
			System.out.println("Unhandled packet type: " + data[0] + ", length: " + data.length);
			System.out.println("\t" + "Data: " + hex(data));
		}
	}
	
	private String hex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			Util.hex(b, sb);
		}
		return sb.toString();
	}

}
