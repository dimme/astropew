package common;

public interface PacketType {
	
	/**
	 * Some kind of message to be shown.
	 */
	public static final byte MESSAGE		 = 32;
	
	/**
	 * Player position, direction and orientation
	 */
	public static final byte PLAYER_POSITION = 33;
	
	/**
	 * Object position and direction
	 */
	public static final byte OBJECT_POSITION = 34;
	
	/**
	 * Sent to joining client to acknowledge their joining
	 * An INITIALIZER packet contains: 
	 * Code - byte - 1 byte
	 * Random Seed - long - 8 bytes
	 * ID - int - 4 byte
	 * Requested name - String - arbitrary length
	 */
	public static final byte INITIALIZER	 = 35; // Sent to joining client
	
	/**
	 * Sends info about a joining player to other players
	 * A PLAYER_JOINED packet contains: 
	 * Code - byte - 1 byte
	 * ID - int - 4 byte
	 * Requested name - String - arbitrary length
	 */
	public static final byte PLAYER_JOINED   = 36;
	
	/**
	 * Informs players that someone left.
	 * A PLAYER_LEFT packet contains:
	 * Code - byte - 1 byte
	 * ID - int - 4 byte
	 */
	public static final byte PLAYER_LEFT	 = 37;
	
	/**
	 * Informs server that a client is leaving.
	 */
	public static final byte LEAVING		 = 38; 
}
