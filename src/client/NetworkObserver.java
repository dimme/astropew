/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

/**
 *
 * @author jonsturk
 */
public interface NetworkObserver {
	public void packetReceived(byte[] data);
}
