package common;

public interface PackageType {
	
	public static final byte MESSAGE         = 32; // A message to be shown.
	public static final byte PLAYER_POSITION = 33; // Player position/direction
	public static final byte OBJECT_POSITION = 34; // Object ---||---
	public static final byte INITIALIZER     = 35; // Sent to joining client
	public static final byte PLAYER_JOINED   = 36; // Sends a joined player's id and name to other players
	public static final byte PLAYER_LEFT     = 37; // Informs players that someone left
	public static final byte LEAVING         = 38; // Informs server that client is leaving
}
