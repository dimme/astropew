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
	 * Time - float - 4 byte <br>
	 * (PlayerID - int - 4 byte <br>
	 * pos - Vector3f - 12 byte <br>
	 * ort - Quartinion - 16 byte <br>
	 * dir - Vector3f - 12 byte) <br>
	 * - x of these - 44 * x <br>
	 */
	public static final byte PLAYER_POSITIONS = 33;

	/**
	 * Sent to all clients when a missile is created <br>
	 * ServerPacketType - byte - 1 byte <br>
	 * Sequence Number - byte - 1 byte <br>
	 * Time of Creation - long - 8 bytes <br>
	 * pos - Vector3f - 12 byte <br>
	 * dir - Vector3f - 12 byte <br>
	 */
	public static final byte MISSILE = 34;

	/**
	 * Sent to tell clients to destroy an object <br>
	 * PacketType - 1 byte <br>
	 * Seq - 1 byte <br>
	 * object id - int - 4 bytes <br>
	 */
	public static final byte DESTROY_OBJECT = 39;
	
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
	
	/**
	 * Sent to inform of a spawned ship
	 * ServerPacketType - byte - 1 byte <br>
	 * Sequence Number - byte - 1 byte <br>
	 * time - float - 4 bytes <br>
	 * PlayerID - int - 4 bytes <br>
	 * Position - Vector3f - 12 bytes <br>
	 * Orientation - Quaternion - 16 bytes <br>
	 * Direction - Vector3f - 12 bytes <br>
	 */
	public static final byte SPAWN = 40;
}
