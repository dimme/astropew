/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common.network;

import common.Util;

public abstract class AbstractPacketObserver implements PacketObserver {

	protected boolean isControlledPacket(byte[] data) {
		final int b = data[0] & Util.CONTROLLED_PACKET_MASK;

		return b != 0;
	}

	protected byte packetType(byte[] data) {
		final int b = data[0] & Util.CONTROLLED_PACKET_UNMASK;
		return (byte) b;
	}

}
