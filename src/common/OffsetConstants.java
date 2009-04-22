package common;

public interface OffsetConstants {

	// SHARED

	// FROM SERVER

	public static final byte MESSAGE_MESSAGE_TYPE_OFFSET = 2;
	public static final byte MESSAGE_STRING_OFFSET = 3;

	public static final byte PLAYER_POSITIONS_TICK_OFFSET = 2;

	public static final byte INITIALIZER_RANDOM_SEED_OFFSET = 2;
	public static final byte INITIALIZER_ID_OFFSET = 10;
	public static final int INITIALIZER_SHIPID_OFFSET = 14;
	public static final byte INITIALIZER_STRING_OFFSET = 18;

	public static final byte PLAYER_JOINED_ID_OFFSET = 2;
	public static final byte PLAYER_JOINED_SHIPID_OFFSET = 6;
	public static final byte PLAYER_JOINED_STRING_OFFSET = 10;

	public static final byte PLAYER_LEFT_ID_OFFSET = 2;
	public static final byte PLAYER_LEFT_SIZE = 6;
	
	public static final int MISSILE_TIME_OFFSET = 2;
	public static final int MISSILE_POS_OFFSET = 10;
	public static final int MISSILE_DIR_OFFSET = 22;
	public static final int MISSILE_ID_OFFSET = 34;
	public static final int MISSILE_OWNER_OFFSET = 38;
	public static final int MISSILE_SIZE = 42;

	// FROM CLIENT

	public static final byte PLAYER_UPDATE_TIME_OFFSET = 2;
	public static final byte PLAYER_UPDATE_POS_OFFSET = 10;
	public static final byte PLAYER_UPDATE_ORT_OFFSET = 22;
	public static final byte PLAYER_UPDATE_DIR_OFFSET = 38;
	public static final byte PLAYER_UPDATE_SIZE = 50;
	
	public static final int FIRE_MISSILE_SIZE = 10;
	public static final int FIRE_MISSILE_TIME_OFFSET = 2;

}
