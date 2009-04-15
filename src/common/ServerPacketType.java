package common;

public interface ServerPacketType {

	/**
	 * Some kind of message to be shown. <br>
	 * ServerPacketType - byte - 1 byte <br>
	 * Sequence Number - byte - 1 byte <br>
	 * MessageType - byte - 1 byte <br>
	 * Message - String - arbitrary length <br>
	 */
	public static final byte MESSAGE = 32;

	/**
	 * Player position, direction and orientation<br>
	 * ServerPacketType - byte - 1 byte <br>
	 * Sequence Number - byte - 1 byte <br>
	 * Time - long - 8 byte <br>
	 * (ID - int - 4 byte <br>
	 * pos - Vector3f - 12 byte <br>
	 * dir - Quartinion - 16 byte <br>
	 * ort - Vector3f - 12 byte) <br>
	 * - x of these - 44 * x <br>
	 */
	public static final byte PLAYER_POSITIONS = 33;

	/**
	 * Object position and direction
	 */
	public static final byte OBJECT_POSITION = 34;

	/**
	 * Sent to joining client to acknowledge their joining <br>
	 * ServerPacketType - byte - 1 byte <br>
	 * Sequence Number - byte - 1 byte <br>
	 * Random Seed - long - 8 bytes <br>
	 * ID - int - 4 byte <br>
	 * Assigned name - String - arbitrary length <br>
	 */
	public static final byte INITIALIZER = 35;

	/**
	 * Sends info about a joining player to other players <br>
	 * ServerPacketType - byte - 1 byte <br>
	 * Sequence Number - byte - 1 byte <br>
	 * ID - int - 4 byte <br>
	 * Name - String - arbitrary length <br>
	 */
	public static final byte PLAYER_JOINED = 36;

	/**
	 * Informs players that someone left. <br>
	 * ServerPacketType - byte - 1 byte <br>
	 * Sequence Number - byte - 1 byte <br>
	 * ID - int - 4 byte <br>
	 */
	public static final byte PLAYER_LEFT = 37;

	/**
	 * Sends information about the players already on the server
	 * ServerPacketType - byte - 1 byte <br>
	 * Sequence Number - byte - 1 byte <br>
	 * (ID - int - 4 byte <br>
	 * Name Lenght - byte - 1 byte <br>
	 * Name - String - Name Length) <br>
	 * - x of these - (5 + Name Lenght[i])) * x
	 */

	public static final byte PLAYERS_INFO = 38;
}
