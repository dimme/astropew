package common;

public interface OffsetConstants {

	// SHARED

	// FROM SERVER

	public static final byte MESSAGE_MESSAGE_TYPE_OFFSET = 2;
	public static final byte MESSAGE_STRING_OFFSET = 3;

	public static final byte PLAYER_POSITION_TICK_OFFSET = 2;
	public static final byte PLAYER_POSITION_ID_OFFSET = 10;
	public static final byte PLAYER_POSITION_POS_OFFSET = 14;
	public static final byte PLAYER_POSITION_DIR_OFFSET = 26;
	public static final byte PLAYER_POSITION_ORT_OFFSET = 38;
	public static final byte PLAYER_POSITION_SIZE = 50;

	public static final byte INITIALIZER_RANDOM_SEED_OFFSET = 2;
	public static final byte INITIALIZER_ID_OFFSET = 10;
	public static final byte INITIALIZER_STRING_OFFSET = 14;

	public static final byte PLAYER_JOINED_ID_OFFSET = 2;
	public static final byte PLAYER_JOINED_STRING_OFFSET = 6;

	public static final byte PLAYER_LEFT_ID_OFFSET = 2;
	public static final byte PLAYER_LEFT_SIZE = 6;

	// FROM CLIENT

	public static final byte PLAYER_MOVEMENT_TICK_OFFSET = 2;
	public static final byte PLAYER_MOVEMENT_DIR_OFFSET = 10;
	public static final byte PLAYER_MOVEMENT_ORT_OFFSET = 22;
	public static final byte PLAYER_MOVEMENT_SIZE = 34;

}
