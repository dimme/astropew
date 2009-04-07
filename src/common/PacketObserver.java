/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common;

/**
 *
 * @author jonsturk
 */
public interface PacketObserver {
	public void packetReceived(byte[] data);
}
