/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

import common.PackageType;
import common.Util;

/**
 *
 * @author jonsturk
 */
public class ConsoleNetworkObserver implements NetworkObserver {

    public void packetReceived(byte[] data) {
        if (data[0] == PackageType.INITIALIZER) {
            int id = Util.bytesToInt(data, 1, 4);
            long randSeed = Util.bytesToLong(data,5,8);
            System.out.println("Established contact with server. Got id: "+ id +". Got seed: "+ randSeed);
        }
        if (data[0] == PackageType.PLAYER_LEFT) {
            int lid = Util.bytesToInt(data, 1, 4);
            System.out.println("PLayer Left. ID = " + lid);
        }
        if (data[0] == PackageType.PLAYER_JOINED) {
            int nid = Util.bytesToInt(data, 1, 4);
            byte[] bt = new byte[data.length - 5];
            System.arraycopy(data, 5, bt, 0, bt.length);
            System.out.println("Player Joined. ID = " + nid + " Name = " + new String(bt));
        }
    }

}
