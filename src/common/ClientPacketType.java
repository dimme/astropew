package common;

public interface ClientPacketType {

	/**
	 * Informs server that a client is leaving. A LEAVING packet contains:
	 * ClientPacketType - byte - 1 byte <br>
	 * Sequence Number - byte - 1 byte <br>
	 */
	public static final byte LEAVING = 64;

	/**
	 * ClientPacketType - byte - 1 byte <br>
	 * Sequence Number - byte - 1 byte <br>
	 * Time - long - 8 byte <br>
	 * pos - Vector3f - 12 byte <br>
	 * ort - Quartinion - 16 byte <br>
	 * dir - Vector3f - 12 byte <br>
	 */
	public static final byte PLAYER_UPDATE = 65;

	public static final byte JOINING = 66;


	/**
	 * ClientPacketType - byte - 1 byte <br>
	 * Sequence Number - byte - 1 byte <br>
	 * Time - long - 8 byte <br>
	 */
	public static final byte FIRE_MISSILE = 67;
	
	public static final byte CHAT_MESSAGE = 68;

}
