package common;

public interface ClientPacketType {
	
	/**
	 * Informs server that a client is leaving.
	 * A LEAVING packet contains:
	 * ClientPacketType - byte - 1 byte <br>
	 * Sequence Number - byte - 1 byte <br>
	 */
	public static final byte LEAVING		 = 64; 
	
	
	/**
	 * Informs server where we are headed
	 * A PLAYER_MOVEMENT packer contains:
	 * ClientPacketType - byte - 1 byte <br>
	 * Sequence Number - byte - 1 byte <br>
	 * Time - long - 8 byte <br>
	 * dir - Vector3f - 12 byte <br> 
	 * ort - Vector3f - 12 byte <br>
	 */
	public static final byte PLAYER_MOVEMENT = 65;


	public static final byte JOINING = 66;

}
