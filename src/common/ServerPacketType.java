package common;

public interface ServerPacketType {
	
	/**
	 * Some kind of message to be shown.
	 * A MESSAGE packet contains:
	 * ServerPacketType - byte - 1 byte
	 * MessageType - byte - 1 byte
	 * Message - String - arbitrary length
	 */
	public static final byte MESSAGE		 = 32;
	
	/**
	 * Player position, direction and orientation
	 * byte[] b = new byte[1 + 8 + 4 + 3*3*4]; //type time playerid pos/dir/ort
	 * A PLAYER_POSITION packet contains:
	 * ServerPacketType - byte - 1 byte
	 * Time - long - 8 byte
	 * ID - int - 4 byte
	 * pos - Vector3f - 12 byte
	 * dir - Vector3f - 12 byte
	 * ort - Vector3f - 12 byte
	 */
	public static final byte PLAYER_POSITION = 33;
	
	/**
	 * Object position and direction
	 */
	public static final byte OBJECT_POSITION = 34;
	
	/**
	 * Sent to joining client to acknowledge their joining
	 * An INITIALIZER packet contains: 
	 * ServerPacketType - byte - 1 byte
	 * Random Seed - long - 8 bytes
	 * ID - int - 4 byte
	 * Requested name - String - arbitrary length
	 */
	public static final byte INITIALIZER	 = 35; // Sent to joining client
	
	/**
	 * Sends info about a joining player to other players
	 * A PLAYER_JOINED packet contains: 
	 * ServerPacketType - byte - 1 byte
	 * ID - int - 4 byte
	 * Requested name - String - arbitrary length
	 */
	public static final byte PLAYER_JOINED   = 36;
	
	/**
	 * Informs players that someone left.
	 * A PLAYER_LEFT packet contains:
	 * ServerPacketType - byte - 1 byte
	 * ID - int - 4 byte
	 */
	public static final byte PLAYER_LEFT	 = 37;
}
