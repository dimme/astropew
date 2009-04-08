package common;

public interface ClientPacketType {
	
	/**
	 * Informs server that a client is leaving.
	 */
	public static final byte LEAVING		 = 64; 
	
	
	/**
	 * Informs server where we are headed
	 */
	public static final byte PLAYER_MOVEMENT = 65;

}
