package common.network;

import java.net.DatagramPacket;
import java.util.HashMap;

import common.Util;

public class UDPConnection {

	public final DatagramPacket dgp;
	private final HashMap<Byte, byte[]> packets = new HashMap<Byte, byte[]>();
	private final boolean[] acked = new boolean[256];
	private byte oldestPending = 0;
	private byte nextSeq = 0;

	public UDPConnection(DatagramPacket dgp) {
		this.dgp = dgp;
	}

	public byte addControlledPacket(byte[] data) throws SendWindowFullException {
		int ns = nextSeq < 0 ? nextSeq + 256 : nextSeq;
		int op = oldestPending < 0 ? oldestPending + 256 : oldestPending;
		int diff = ns - op;
		if (diff < 0) {
			diff += 256;
		}

		if (diff > 128) {
			throw new SendWindowFullException("Send window overflow; next=" + nextSeq + ", oldestpending=" + oldestPending);
		}

		data[0] |= Util.CONTROLLED_PACKET_MASK;
		data[1] = nextSeq;
		packets.put(nextSeq, data);
		return nextSeq++;
	}

	public void acknowledged(byte seq) {
		final int s = seq < 0 ? seq + 256 : seq;
		acked[s] = true;
		packets.remove(seq);

		int o = s;
		while (acked[o]) {
			acked[o] = false;
			o = o == 255 ? 0 : o+1;
		}

		oldestPending = (byte)o;
	}

	/**
	 *
	 * @param seq
	 * @return
	 * @throws NullPointerException if data for seq does not exist.
	 */
	public byte[] getData(byte seq) {
		return packets.get(seq);
	}

	public int getNumPending() {
		return packets.size();
	}

	public String toString() {
		return dgp.getSocketAddress().toString() + ", " + packets.size() + " pending";
	}
}
