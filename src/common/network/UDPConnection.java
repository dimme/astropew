package common.network;

import java.net.DatagramPacket;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import common.Util;

public class UDPConnection {
	
	public final DatagramPacket dgp;
	private HashMap<Byte, byte[]> packets = new HashMap<Byte, byte[]>();
	private boolean[] acked = new boolean[256];
	private byte oldestPending = 0;
	private byte nextSeq = 0;
	
	public UDPConnection(DatagramPacket dgp) {
		this.dgp = dgp;
	}
	
	public byte addControlledPacket(byte[] data) throws SendWindowFullException {
		int diff = ((int)nextSeq) - oldestPending;
		if (diff < 0) {
			diff += 256;
		}
		
		if (diff > 128) {
			throw new SendWindowFullException("Send window overflow"); 
		}
		
		data[0] |= Util.CONTROLLED_PACKET_MASK;
		data[1] = nextSeq;
		packets.put(nextSeq, data);
		return nextSeq++;
	}
	
	public void acknowledged(byte seq) {
		int s = seq<0 ? seq+256 : seq;
		acked[s] = true;
		packets.remove(s);
		
		int o = s;
		while (acked[o]) {
			acked[oldestPending] = false;
			oldestPending++;
			o = oldestPending<0 ? oldestPending+256 : oldestPending;
		}
	}

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
