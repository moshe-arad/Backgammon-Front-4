package org.moshe.arad.kafka;

public class KafkaUtils {

	public static final String SERVERS = "192.168.1.4:9092,192.168.1.4:9093,192.168.1.4:9094";
	public static final String CREATE_NEW_USER_COMMAND_GROUP = "CreateNewUserCommandGroup";
	public static final String STRING_DESERIALIZER = "org.apache.kafka.common.serialization.StringDeserializer";
	public static final String STRING_SERIALIZER = "org.apache.kafka.common.serialization.StringSerializer";
	public static final String CREATE_NEW_USER_COMMAND_DESERIALIZER = "org.moshe.arad.kafka.deserializers.CreateNewUserCommandDeserializer";
	public static final String NEW_USER_CREATED_EVENT_SERIALIZER = "org.moshe.arad.kafka.serializers.NewUserCreatedEventSerializer";
	public static final String CREATE_NEW_USER_COMMAND_TOPIC = "Create-New-User-Command";
	public static final String CHECK_USER_EMAIL_AVAILABILITY_COMMANDS_TOPIC = "Check-User-Email-Availability-Command";
	public static final String CHECK_USER_NAME_AVAILABILITY_COMMAND_TOPIC = "Check-User-Name-Availability-Command";
	public static final String CREATE_NEW_USER_COMMAND_SERIALIZER = "org.moshe.arad.kafka.serializers.CreateNewUserCommandSerializer";
	public static final String CHECK_USER_NAME_AVAILABILITY_COMMAND_SERIALIZER = "org.moshe.arad.kafka.serializers.CheckUserNameAvailabilityCommandSerializer";
	public static final String USER_NAME_AVAILABILITY_CHECKED_EVENT_TOPIC = "User-Name-Availability-Checked-Event";
	public static final String USER_NAME_AVAILABILITY_CHECKED_EVENT_GROUP = "UserNameAvailabilityCheckedEventGroup";
	public static final String USER_NAME_AVAILABILITY_CHECKED_EVENT_DESERIALIZER = "org.moshe.arad.kafka.deserializers.UserNameAvailabilityCheckedEventDeserializer";
	public static final String CHECK_EMAIL_AVAILABILITY_COMMAND_SERIALIZER = "org.moshe.arad.kafka.serializers.CheckUserEmailAvailabilityCommandSerializer";
	public static final String CHECK_USER_EMAIL_AVAILABILITY_COMMAND_TOPIC = "Check-User-Email-Availability-Command";
	public static final String USER_EMAIL_AVAILABILITY_CHECKED_EVENT_GROUP = "UserEmailAvailabilityCheckedEventGroup";
	public static final String USER_EMAIL_AVAILABILITY_CHECKED_EVENT_DESERIALIZER = "org.moshe.arad.kafka.deserializers.UserEmailAvailabilityCheckedEventDeserializer";
	public static final String EMAIL_AVAILABILITY_CHECKED_EVENT_TOPIC = "Email-Availability-Checked-Event";
	public static final String NEW_USER_CREATED_ACK_EVENT_GROUP = "NewUserCreatedAckEventGroup";
	public static final String NEW_USER_CREATED_ACK_EVENT_TOPIC = "New-User-Created-Ack-Event";
	public static final String LOG_IN_USER_COMMAND_TOPIC = "Log-In-User-Command";
	public static final String LOG_IN_USER_ACK_EVENT_TOPIC = "Log-In-User-Ack-Event";
	public static final String LOG_OUT_USER_ACK_EVENT_GROUP = "LogOutUserAckEventGroup";
	public static final String LOGGED_IN_EVENT_TOPIC = "Logged-In-Event";
	public static final String LOG_OUT_USER_COMMAND_TOPIC = "Log-Out-User-Command";
	public static final String LOGGED_OUT_EVENT_TOPIC = "Logged-Out-Event";
	public static final String LOG_OUT_USER_ACK_EVENT_TOPIC = "Log-Out-User-Ack-Event";
	public static final String LOG_IN_USER_ACK_EVENT_GROUP = "LogInUserAckEventGroup";
	public static final String OPEN_NEW_GAME_ROOM_COMMAND_TOPIC = "Open-New-Game-Room-Command";
	public static final String NEW_GAME_ROOM_OPENED_EVENT_ACK_GROUP = "NewGameRoomOpenedEventAckGroup";
	public static final String NEW_GAME_ROOM_OPENED_EVENT_ACK_TOPIC = "New-Game-Room-Opened-Event-Ack";
	public static final String CLOSE_GAME_ROOM_COMMAND_TOPIC = "Close-Game-Room-Command";
	public static final String CLOSE_GAME_ROOM_EVENT_ACK_GROUP = "CloseGameRoomEventAckGroup";
	public static final String CLOSE_GAME_ROOM_EVENT_ACK_TOPIC = "Close-Game-Room-Event-Ack";
	public static final String ADD_USER_AS_WATCHER_COMMAND_TOPIC = "Add-User-As-Watcher-Command";
	public static final String USER_ADDED_AS_WATCHER_EVENT_ACK_GROUP = "UserAddedAsWatcherEventAckGroup";
	public static final String USER_ADDED_AS_WATCHER_EVENT_ACK_TOPIC = "User-Added-As-Watcher-Event-Ack";
	public static final String GET_ALL_GAME_ROOMS_COMMAND_TOPIC = "Get-All-Game-Rooms-Command";
	public static final String GET_ALL_GAME_ROOMS_EVENT_ACK_TOPIC = "Get-All-Game-Rooms-Event-Ack";
	public static final String GET_ALL_GAME_ROOMS_EVENT_ACK_GROUP = "GetAllGameRoomsEventAck";
	public static final String GET_LOBBY_UPDATE_VIEW_COMMAND_TOPIC = "Get-Lobby-Update-View-Command";
	public static final String GET_LOBBY_UPDATE_VIEW_ACK_EVENT_GROUP = "GetLobbyUpdateViewAckEventGroup";
	public static final String GET_LOBBY_UPDATE_VIEW_ACK_EVENT_TOPIC = "Get-Lobby-Update-View-Ack-Event";
}
