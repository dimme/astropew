package common.network;

import java.net.DatagramPacket;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import common.CommonPacketType;
import common.Util;

public abstract class AckingFilter implements PacketFilter {
	protected PacketSender ps;

	protected Map<SocketAddress, Data> ackdata;

	public AckingFilter(PacketSender ps) {
		this.ps = ps;
		ackdata = new HashMap<SocketAddress, Data>();
	}

	public boolean accept(byte[] data, SocketAddress addr) {
		if (Util.packetType(data) == CommonPacketType.ACK) {
			return true;
		} else {
			if (Util.isControlledPacket(data)) {
				final byte[] sendData = new byte[2];
				sendData[0] = CommonPacketType.ACK;
				sendData[1] = data[1];
				ps.send(sendData, getDatagramPacket(addr));


				Data d = ackdata.get(addr);
				if (d == null) {
					d = new Data();
					ackdata.put(addr, d);
				}
				byte seq = data[1];

				boolean isNewPacket = d.received(seq);

				return isNewPacket;
			}
		}
		return true;
	}

	protected abstract DatagramPacket getDatagramPacket(SocketAddress saddr);

	private static class Data {
		private boolean[] received = new boolean[256];
		private int oldestPending = 0;

		/**
		 * @param seq
		 * @return true if seq was inside the window
		 */
		public boolean received(byte s) {
			int seq = s<0 ? s+256 : s;
			int op = oldestPending < 0 ? oldestPending+256 : oldestPending;
			int diff = op - seq;
			if (diff < 0) {
				diff += 256;
			}

			if (diff < 128) {
				received[seq] = true;
				while ( received[oldestPending] ) {
					received[oldestPending] = false;
					oldestPending = oldestPending == 255 ? 0 : oldestPending+1;
				}
				return true;
			}

			Logger.getLogger(getClass().getName()).log(Level.WARNING, "Received packet outside window, seq = " + seq);
			return false;
		}
	}
}
