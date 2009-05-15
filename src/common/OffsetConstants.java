package common;

public interface OffsetConstants {

	// SHARED

	// FROM SERVER

	public static final int MESSAGE_MESSAGE_TYPE_OFFSET = 2;
	public static final int MESSAGE_TIME_OFFSET = 3;
	public static final int MESSAGE_NUM_OBJECTS_OFFSET = 7;
	public static final int MESSAGE_OBJECT_IDS_OFFSET = 11;
	public static final int MESSAGE_OVERHEAD_SIZE = MESSAGE_OBJECT_IDS_OFFSET;
	public static final int MESSAGE_OBJECT_ID_LENGTH = 4;

	public static final int PLAYER_POSITIONS_TIME_OFFSET = 2;
	public static final int PLAYER_POSITIONS_DATA_START = 6;
	public static final int PLAYER_POSITIONS_ID_OFFSET = 0;
	public static final int PLAYER_POSITIONS_POS_OFFSET = 4;
	public static final int PLAYER_POSITIONS_ORT_OFFSET = 16;
	public static final int PLAYER_POSITIONS_DIR_OFFSET = 32;
	public static final int PLAYER_POSITIONS_POINTS_OFFSET = 44;
	public static final int PLAYER_POSITIONS_ONE_SIZE = 48;

	public static final int PLAYERS_INFO_DATA_START = 2;
	public static final int PLAYERS_DATA_ID_OFFSET = 0;
	public static final int PLAYERS_DATA_SHIPID_OFFSET = 4;
	public static final int PLAYERS_DATA_NAME_LENGTH_OFFSET = 8;
	public static final int PLAYERS_DATA_NAME_OFFSET = 9;

	public static final int INITIALIZER_RANDOM_SEED_OFFSET = 2;
	public static final int INITIALIZER_ID_OFFSET = 10;
	public static final int INITIALIZER_SHIPID_OFFSET = 14;
	public static final int INITIALIZER_CURRENT_TIME_MILLIS_OFFSET = 18;
	public static final int INITIALIZER_CURRENT_TIME_FLOAT_OFFSET = 26;
	public static final int INITIALIZER_STRING_OFFSET = 30;

	public static final int PLAYER_JOINED_ID_OFFSET = 2;
	public static final int PLAYER_JOINED_SHIPID_OFFSET = 6;
	public static final int PLAYER_JOINED_STRING_OFFSET = 10;

	public static final int PLAYER_LEFT_ID_OFFSET = 2;
	public static final int PLAYER_LEFT_SIZE = 6;

	public static final int MISSILE_TIME_OFFSET = 2;
	public static final int MISSILE_POS_OFFSET = 6;
	public static final int MISSILE_DIR_OFFSET = 18;
	public static final int MISSILE_ID_OFFSET = 30;
	public static final int MISSILE_OWNER_OFFSET = 34;
	public static final int MISSILE_SIZE = 38;

	public static final int SPAWN_TIME_OFFSET = 2;
	public static final int SPAWN_PLAYERID_OFFSET = 6;
	public static final int SPAWN_POS_OFFSET = 10;
	public static final int SPAWN_ORT_OFFSET = 22;
	public static final int SPAWN_DIR_OFFSET = 38;
	public static final int SPAWN_SIZE = 50;

	// FROM CLIENT

	public static final int PLAYER_UPDATE_TIME_OFFSET = 2;
	public static final int PLAYER_UPDATE_POS_OFFSET = 6;
	public static final int PLAYER_UPDATE_ORT_OFFSET = 18;
	public static final int PLAYER_UPDATE_DIR_OFFSET = 34;
	public static final int PLAYER_UPDATE_SIZE = 46;

	public static final int FIRE_MISSILE_TIME_OFFSET = 2;
	public static final int FIRE_MISSILE_SIZE = 6;

	public static final int OBJECT_HP_TIME_OFFSET = 2;
	public static final int OBJECT_HP_ID_OFFSET = 6;
	public static final int OBJECT_HP_INSTIGATING_PLAYER_ID_OFFSET = 10;
	public static final int OBJECT_HP_VALUE_OFFSET = 14;
	public static final int OBJECT_HP_SIZE = 18;

}
