package tm.payhas.crm.statics;

public class StaticConstants {
    public static String APPLICATION_DIR_NAME = "CRM";
    public static String FILES_DIR = "/Files/";
    //Message Statuses
    public static final String MESSAGE_UN_SEND = "status0";
    public static final String MESSAGE_SENT = "status1";
    public static final String MESSAGE_DELIVERED = "status2";
    public static final String MESSAGE_READ = "status3";
    //Message Type
    public static final String STRING = "type0";
    public static final String PHOTO = "type1";
    public static final String VOICE = "type2";
    public static final String FILE = "type3";
    public static final String DATE = "type4";//for user
    //Task Statuses
    public static String NOT_STARTED = "notStarted";
    public static String IN_PROCESS = "inprocess";
    public static String REVIEW = "review";
    public static String PENDING = "pending";
    public static String FINISHED = "finished";
    //Priority
    public static String PRIMARY = "primary";
    public static String MEDIUM = "medium";
    public static String HIGH = "high";
    public static String NOT_IMPORTANT = "neotlozhnyy";


    //Events
    public static final String SENT_NEW_MESSAGE = "createMessage"; // sent new message
    public static final String USER_STATUS = "userStatus"; // user Status
    public static final String RECEIVED_NEW_MESSAGE = "newMessage";// new message received
    public static final String MESSAGE_STATUS = "messageStatus";// to message author when new message created
    public static final String CHANNEL_MESSAGES = "messages"; // to get Room messages
    public static final String MESSAGE_RECEIVE = "receiveMessage";// to receive message
    public static final String MESSAGES_RECEIVED = "receiveYourMessage"; // emit to receive message
    public static final String USER_STATUS_CHANNEL = "userStatusChannel"; // emit to receive message
    public static final String REMOVE_MESSAGE = "removeMessage"; // to remove message
    public static final String NEW_ROOM = "newRoom"; // to remove message

    //Message Received and Sent
    public static final int SENT_MESSAGE = 1;
    public static final int RECEIVED_MESSAGE = 2;
    // Dashboard enums
    public static final String NEWS = "news";
    public static final String HOLIDAYS = "holiday";

}


