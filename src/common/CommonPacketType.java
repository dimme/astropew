package common;

public interface CommonPacketType {
	/**
	 * Informs the other party that a packet has been received without problem.
	 * ClientPacketType - byte - 1 byte <br>
	 * Sequence Number to ACK - byte - 1 byte <br>
	 */
	public static final byte ACK = 96; 
}
