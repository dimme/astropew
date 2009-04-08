package common;

public interface ClientPacketType {
	
	/**
	 * Informs server that a client is leaving.
	 * A LEAVING packet contains:
	 * ClientPacketType - byte - 1 byte
	 */
	public static final byte LEAVING		 = 64; 
	
	
	/**
	 * Informs server where we are headed
	 * A PLAYER_MOVEMENT packer contains:
	 * ClientPacketType - byte - 1 byte
	 * Time - long - 8 byte
	 * dir - Vector3f - 12 byte
	 * ort - Vector3f - 12 byte
	 */
	public static final byte PLAYER_MOVEMENT = 65;

}
