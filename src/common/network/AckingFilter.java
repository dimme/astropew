package common.network;

import java.net.DatagramPacket;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;

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
		if (Util.isControlledPacket(data)) {
			final byte[] sendData = new byte[2];
			sendData[0] = CommonPacketType.ACK;
			sendData[1] = data[1];
			ps.send(sendData, getDatagramPacket(addr));
		}
		
		Data d = ackdata.get(addr);
		if (d == null) {
			d = new Data();
			ackdata.put(addr, d);
		}
		byte seq = data[1];
		
		return d.received(seq);
	}

	protected abstract DatagramPacket getDatagramPacket(SocketAddress saddr);
	
	private static class Data {
		private boolean[] received = new boolean[256];
		private byte oldestPending = 0;
		
		/**
		 * @param seq
		 * @return true if seq was inside the window
		 */
		public boolean received(byte seq) {
			int diff = oldestPending - (int)seq;
			if (diff < 0) {
				diff += 256;
			}
			if (diff < 128) {
				received[seq] = true;
				while ( received[oldestPending] ) {
					received[oldestPending] = false;
					oldestPending++;
				}
				return true;
			}
			return false;
		}
	}
}
