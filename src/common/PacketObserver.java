/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common;

import java.net.SocketAddress;

/**
 *
 * @author jonsturk
 */
public interface PacketObserver {
	public void packetReceived(byte[] data, SocketAddress addr);
}
