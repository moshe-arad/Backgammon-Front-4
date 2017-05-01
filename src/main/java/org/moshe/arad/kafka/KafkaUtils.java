package org.moshe.arad.kafka;

public class KafkaUtils {

	public static final String SERVERS = "192.168.1.3:9092,192.168.1.3:9093,192.168.1.3:9094";
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
}
