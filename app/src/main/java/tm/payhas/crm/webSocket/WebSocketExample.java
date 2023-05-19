//package salam.messengertm;
//
//import static android.net.Uri.encode;
//import static tm.payhas.crm.api.network.Network.BASE_URL_SOCKET;
//
//import android.accounts.Account;
//import android.app.Activity;
//import android.app.ActivityManager;
//import android.app.Notification;
//import android.app.PendingIntent;
//import android.content.ContentResolver;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Handler;
//import android.util.Log;
//
//import androidx.core.app.NotificationCompat;
//import androidx.core.app.NotificationManagerCompat;
//import androidx.fragment.app.Fragment;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonObject;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.File;
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.nio.channels.Channel;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Objects;
//import java.util.regex.Pattern;
//
//import dev.gustavoavila.websocketclient.WebSocketClient;
//import okhttp3.MediaType;
//import okhttp3.MultipartBody;
//import okhttp3.RequestBody;
//import retrofit2.Call;
//import tm.payhas.crm.interfaces.MessageCallBack;
//import tm.payhas.crm.preference.AccountPreferences;
//
//
//public class WebSocketExample {
//
//    WebSocketClient webSocketClient;
//    Context context;
//    Activity activity;
//    NotificationManagerCompat notificationManagerCompat;
//    public Boolean connected = false;
//    public static ArrayList<String> messages;
//    public static ArrayList<Integer> ids;
//    private Handler handler = new Handler();
//    public MessageCallBack call;
//    public PostCallBack postCallBack;
//
//    public WebSocketExample(Context context, Activity activity) {
//        this.context = context;
//        this.activity = activity;
//        messages = new ArrayList<>();
//        ids = new ArrayList<>();
//        notificationManagerCompat = NotificationManagerCompat.from(context);
//    }
//
//    public void createWebSocketClient(Account accountPreferences) {
//        URI uri;
//
//        try {
//            uri = new URI(BASE_URL_SOCKET);
//        } catch (URISyntaxException e) {
//            Log.e("TAG", "createWebSocketClient: " + e.getMessage());
//            return;
//        }
//
//        webSocketClient = new WebSocketClient(uri) {
//            @Override
//            public void onOpen() {
//
//                Log.e("TAG", "onOpen: ");
//                sendFirebaseToken();
//
//                connected = true;
//                getUserRooms("0");
//            }
//
//            @Override
//            public void onTextReceived(String emit) {
//
//                Log.e("TAG", "onTextReceived: " + emit);
//                JSONObject dataObj;
//                JSONArray dataArr;
//                try {
//
//                    JSONObject messageJson = new JSONObject(emit);
//                    String channel = messageJson.getString("channel");
//
//                    switch (channel) {
//
//                        case Channel.NEW_ROOM:
//                            dataObj = messageJson.getJSONObject("data");
//                            newRoom(dataObj);
//                            break;
//
//                        case Channel.NEW_MESSAGE:
//                            dataObj = messageJson.getJSONObject("data");
//                            newMessage(dataObj);
//                            break;
//                        case Channel.USER_DETAIL:
//                            dataObj = messageJson.getJSONObject("data");
//                            updateUserDetail(dataObj);
//                            break;
//
//                        case Channel.NEW_MESSAGE_ARRAY:
//                            dataArr = messageJson.getJSONArray("data");
//                            newMessageArray(dataArr);
//                            break;
//                        case Channel.USER_ROOMS:
//                            dataArr = messageJson.getJSONArray("data");
//                            userRooms(dataArr);
//                            break;
//                        case Channel.MESSAGE_IN_SERVER:
//                            dataObj = messageJson.getJSONObject("data");
//                            messageInServer(dataObj);
//                            break;
//                        case Channel.ROOM_EVENTS:
//                            dataObj = messageJson.getJSONObject("data");
//                            handleEvent(dataObj);
//                            break;
//                        case Channel.MESSAGE_IN_USER:
//                            dataObj = messageJson.getJSONObject("data");
//                            messageInUser(dataObj);
//                            break;
//                        case Channel.FRIEND_STATUS:
//                            dataObj = messageJson.getJSONObject("data");
//                            updateFriendStatus(dataObj);
//                            break;
//
//                        case Channel.SEND_SDP:
//                            dataObj = new JSONObject(messageJson.getString("data"));
//                            onSdpReceive(dataObj);
//                            break;
//                        case Channel.NEW_POST:
//                            if (postCallBack != null) {
//                                postCallBack.onNewPost();
//                            }
//
//                            break;
//                        case Channel.NEW_FOLLOWER_COUNT:
//
//                            int data = messageJson.getInt("data");
//                            newFollowerCount(data);
//                            break;
//                        case Channel.LOGOUT:
//                            logout();
//                            break;
//                        case Channel.APP_UPDATE:
//                            logout();
//                            accountPreferences.saveUpdated(false);
//                            break;
//                        case Channel.UPDATE_ROOM:
//                            dataArr = messageJson.getJSONArray("data");
//                            updateRoom(dataArr);
//                            break;
//
//
//                    }
//
//
//                } catch (JSONException e) {
//
//                    Log.e("TAG", "onTextReceived:" + e.getMessage());
//
//                }
//            }
//
//            @Override
//            public void onBinaryReceived(byte[] data) {
//                System.out.println("onBinaryReceived");
//            }
//
//            @Override
//            public void onPingReceived(byte[] data) {
//                System.out.println("onPingReceived");
//            }
//
//            @Override
//            public void onPongReceived(byte[] data) {
//                System.out.println("onPongReceived");
//            }
//
//            @Override
//            public void onException(Exception e) {
//
//                Log.e("TAG", "onException: !!!" + e.getMessage());
//            }
//
//            @Override
//            public void onCloseReceived(int reason, String description) {
//
//            }
//
//            @Override
//            public void onCloseReceived() {
//                Log.e("TAG", "onCloseReceived: socket connection closed");
//            }
//        };
//
//        webSocketClient.setConnectTimeout(5000);
//        webSocketClient.setReadTimeout(70000);
//        webSocketClient.enableAutomaticReconnection(5000);
//        webSocketClient.connect();
//    }
//
//    private void updateRoom(JSONArray dataArr) {
//
//        try {
//            for (int i = 0; i < dataArr.length(); i++) {
//
//                JSONObject dataObj = dataArr.getJSONObject(i);
//                String uuid = "";
//                int status = 0;
//
//                try {
//                    uuid = dataObj.getString("room_id");
//                    status = dataObj.getInt("status");
//
//
//                } catch (JSONException e) {
//                    Log.e("TAG", "updateRoom: " + e.getMessage());
//                }
//
//                vmRoom.updateRoomStatus(uuid, status);
//            }
//
//        } catch (JSONException e) {
//            Log.e("TAG", "userRooms: " + e.getMessage());
//        }
//
//
//    }
//
//    private void logout() {
//
//        accountPreferences.editor.clear().apply();
//        webSocket.closeWebSocketClient();
//        webSocketClient.close();
//        Intent intent = new Intent(activity, ActivitySplashScreen.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
//        activity.finish();
//        AsyncTask.execute(() -> SalamDB.getInstance(context).clearAllTables());
//
//    }
//
//    private void newFollowerCount(int data) {
//        if (data > 0) {
//            if (mainFragmentManager != null) {
//                Fragment flow = mainFragmentManager.findFragmentByTag(FlowFragment.class.getName());
//
//                if (flow instanceof FirebaseInterface) {
//                    ((FirebaseInterface) flow).coming("2");
//                }
//
//                Fragment add = mainFragmentManager.findFragmentByTag(FragmentProfile.class.getName());
//                if (add instanceof FirebaseInterface) {
//                    ((FirebaseInterface) add).coming("2");
//                } else {
//                    StaticVariables.isProfileBadge = true;
//                }
//            }
//
//        }
//    }
//
//    private void updateUserDetail(JSONObject dataObj) {
//        String fullName = "";
//        String nickname = "";
//        String avatar = "";
//        String colorCode = "";
//        String aboutMe = "";
//        String birthday = "";
//        int genderType = 0;
//        int messageNote = 0;
//
//        try {
//            nickname = dataObj.getString("nickname");
//            fullName = dataObj.getString("fullName");
//            avatar = dataObj.getString("avatarName");
//            colorCode = dataObj.getString("colorCode");
//            birthday = dataObj.getString("birthday");
//            aboutMe = dataObj.getString("aboutMe");
//            messageNote = dataObj.getInt("messageNote");
//            genderType = dataObj.getInt("genderType");
//
//        } catch (JSONException e) {
//            Log.e("TAG", "onSdpReceive: " + e.getMessage());
//        }
//
//
//        accountPreferences.saveUserAbout(aboutMe);
//
//        accountPreferences.saveRegisterName(fullName);
//
//        accountPreferences.saveUserType(genderType);
//
//        accountPreferences.saveNickname(nickname);
//
//        accountPreferences.saveColorCode(colorCode);
//
//        accountPreferences.saveRegisterImage(avatar);
//
//        accountPreferences.saveUserBirthday(birthday);
//
//        accountPreferences.saveMessageNote(messageNote);
//
//    }
//
//    private void onSdpReceive(JSONObject dataObj) {
//        String uuid = "";
//        String name = "";
//        String avatar = "";
//        String colorCode = "";
//        String type = "";
//        String roomUUID = "";
//        String fullName = "";
//        int canSendMessage = 0;
//        int isOpen = 0;
//        int isBlocked = 0;
//        int peopleType = 0;
//        String candidate = "";
//        String sessionDescription = "";
//        String callType = "";
//        JSONObject friend;
//
//        try {
//            friend = new JSONObject(dataObj.getString("friend"));
//            type = dataObj.getString("type");
//            callType = dataObj.getString("call_type");
//            candidate = dataObj.getString("candidate");
//            sessionDescription = dataObj.optString("sessionDescription");
//            uuid = friend.getString("uuid");
//            name = friend.getString("name");
//            avatar = friend.getString("avatar");
//            colorCode = friend.getString("colorCode");
//            roomUUID = friend.getString("roomUUID");
//            fullName = friend.getString("fullName");
//            canSendMessage = friend.getInt("canSendMessage");
//            isOpen = friend.getInt("isOpen");
//            isBlocked = friend.getInt("isBlocked");
//            peopleType = friend.getInt("peopleType");
//
//        } catch (JSONException e) {
//            Log.e("TAG", "onSdpReceive: " + e.getMessage());
//        }
//
//        Gson gson = new Gson();
//        JSONObject sessionDescription1;
//        String description = null;
//        switch (type) {
//            case OFFER:
//                try {
//                    sessionDescription1 = new JSONObject(sessionDescription);
//                    description = sessionDescription1.optString("sdp");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                if (inCall) return;
//
//                if (activity != null) {
//                    UIUtil.hideKeyboard(activity);
//                }
//                addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentVideoCall.newInstance(callType,
//                        avatar, name, uuid, colorCode, roomUUID, fullName, canSendMessage, isOpen, isBlocked,
//                        peopleType, OFFER, description, candidate));
//
//                break;
//            case CANDIDATE:
//                IceCandidate candidate1 = gson.fromJson(candidate, IceCandidate.class);
////                rtcClient.response("id", candidate1.sdpMid, candidate1.sdpMLineIndex, candidate1.sdp);
//                if (rtcClient != null) {
//                    rtcClient.addIceCandidate(new IceCandidate(candidate1.sdpMid, candidate1.sdpMLineIndex, candidate1.sdp));
//                }
//
//                break;
//
//            case ANSWER:
//
//                try {
//                    sessionDescription1 = new JSONObject(sessionDescription);
//                    description = sessionDescription1.optString("sdp");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                rtcClient.onRemoteSessionReceived(new SessionDescription(SessionDescription.Type.ANSWER, description));
//                break;
//
//        }
//
//
//    }
//
//    private void sendFirebaseToken() {
//        JSONObject messageJson = new JSONObject();
//        JSONObject messageJson2 = new JSONObject();
//
//        try {
//            messageJson.put("channel", "fcm_token");
//            messageJson2.put("fcm_token", FirebaseAuthPreferences.newInstance(context).getFirebaseToken());
//            messageJson.put("data", messageJson2);
//
//
//        } catch (JSONException e) {
//            Log.e("TAG", "initListeners: " + e.getMessage());
//        }
//        sendMessage(messageJson);
//    }
//
//    private void handleEvent(JSONObject dataObj) {
//        String roomUUID = "";
//        String date = "";
//        int event = 0;
//
//        try {
//            event = dataObj.getInt("event");
//            roomUUID = dataObj.getString("room_uuid");
//            date = dataObj.getString("date");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        switch (event) {
//            case Channel.NOTIFICATION_CLOSE:
//                vmRoom.updateNotification(0, roomUUID);
//                webSocket.updateRoom(roomUUID);
//                break;
//            case Channel.NOTIFICATION_OPEN:
//                vmRoom.updateNotification(1, roomUUID);
//                webSocket.updateRoom(roomUUID);
//                break;
//            case Channel.READ_ROOM:
//                if (currentRoomId.equals(roomUUID) && call != null) {
//                    call.readRoom(roomUUID, date);
//                }
//                vmMessage.updateSeenMessage(roomUUID, date);
//                webSocket.updateRoom(roomUUID);
//                break;
//            case Channel.DELETE_ROOM:
//                vmRoom.updateLastMessageId(roomUUID, 0);
//                vmMessage.deleteRoomMessages(roomUUID, date);
//                webSocket.removeRoom(roomUUID);
//                break;
//
//        }
//    }
//
//    private void updateFriendStatus(JSONObject data) {
//        int active_status = 0;
//        String roomUUID = "";
//        String friendUUID = "";
//        String lastActiveDate = "";
//        try {
//            active_status = data.getInt("active_status");
//            roomUUID = data.getString("room_uuid");
//            friendUUID = data.getString("friend_uuid");
//            lastActiveDate = data.getString("last_active_date");
//
//            vmRoom.updateFriendStatusAndLastActiveDate(active_status, lastActiveDate, roomUUID, friendUUID);
//            updateRoom(roomUUID);
//
//        } catch (JSONException e) {
//            Log.e("TAG", "newRoom: " + e.getMessage());
//        }
//    }
//
//    private void userRooms(JSONArray rooms) {
//        try {
//            for (int i = 0; i < rooms.length(); i++) {
//
//                JSONObject room = rooms.getJSONObject(i);
//                insertRoom(room);
//                updateRoom(room.getString("uuid"));
//            }
//
//        } catch (JSONException e) {
//            Log.e("TAG", "userRooms: " + e.getMessage());
//        }
//    }
//
//    public void closeWebSocketClient() {
//        webSocketClient.close();
//    }
//
//    public void sendMessage(JSONObject jsonObject) {
//        String message = jsonObject.toString();
//        webSocketClient.send(message);
//        Log.e("TAG", "sendMessage: " + message);
//    }
//
//    public void newRoom(JSONObject messageJson) {
//        try {
//            int localRoomId = messageJson.getInt("local_room_id");
//            int localMessageId = messageJson.getInt("local_message_id");
//            String roomUUID = messageJson.getString("room_uuid");
//            String messageUUID = messageJson.getString("message_uuid");
//            String messageDate = messageJson.getString("message_date");
//
//            vmRoom.updateUUID(localRoomId, roomUUID);
//            vmMessage.updateRoomUUID(String.valueOf(localRoomId), roomUUID);
//            vmMessage.updateSentMessage(roomUUID, localMessageId, messageUUID, messageDate);
//            if (call == null) return;
//            call.messageInServer(localMessageId, messageUUID, messageDate);
//
//            newRoom(String.valueOf(localRoomId), roomUUID);
//            handler.postDelayed(() -> AsyncTask.execute(() -> updateRoom(roomUUID)), 150);
//
//
//        } catch (JSONException e) {
//            Log.e("TAG", "newRoom: " + e.getMessage());
//        }
//
//    }
//
//    public void newMessage(JSONObject dataJson) {
//
//        try {
//            JSONObject room = dataJson.getJSONObject("room");
//
//            insertRoom(room);
//
//            JSONObject message = dataJson.getJSONObject("message");
//            insertMessages(message);
//            if (room.getInt("accept") == 0) {
//                Fragment fragment = mainFragmentManager.findFragmentByTag(FragmentChats.class.getName());
//                if (fragment instanceof MessageRequest) {
//                    ((MessageRequest) fragment).onNewRequest();
//                }
//            }
//
//
//            if (room.getInt("notification") == 1
//                    && !message.getString("sender_uuid").equals(accountPreferences.getPrefUserUUID())
//                    && accountPreferences.getMessageNote() != 0
//                    && !Objects.equals(currentRoomId, room.getString("uuid"))) {
//                pushNotification(room, room.getString("nickname"), message.getString("content"), message.getInt("type"));
//            }
//
//        } catch (JSONException e) {
//            Log.e("TAG", "newMessage: " + e.getMessage());
//        }
//
//    }
//
//    private void newMessageArray(JSONArray messages) {
//        try {
//
//            for (int j = 0; j < messages.length(); j++) {
//                insertMessages(messages.getJSONObject(j));
//            }
//
//        } catch (JSONException e) {
//            Log.e("TAG", "newMessageArray: " + e.getMessage());
//        }
//    }
//
//    private void messageInUser(JSONObject dataObj) {
//
//        String roomUUID = "";
//        String messageUUID = "";
//
//        try {
//
//            roomUUID = dataObj.getString("room_uuid");
//            messageUUID = dataObj.getString("message_uuid");
//            vmMessage.updateMessageInUser(roomUUID, messageUUID);
//            if (currentRoomId.equals(roomUUID) && call != null) {
//                call.messageInUser(messageUUID);
//            }
//            updateRoom(roomUUID);
//
//        } catch (JSONException e) {
//            Log.e("TAG", "messageInUser: " + e.getMessage());
//        }
//    }
//
//    private void messageInServer(JSONObject dataJson) {
//        int localMessageId = 0;
//        String roomUUID = "";
//        String messageUUID = "";
//        String messageDate = "";
//
//        try {
//            localMessageId = dataJson.getInt("local_message_id");
//            roomUUID = dataJson.getString("room_uuid");
//            messageUUID = dataJson.getString("message_uuid");
//            messageDate = dataJson.getString("message_date");
//
//            vmMessage.updateSentMessage(roomUUID, localMessageId, messageUUID, messageDate);
//
//
//            if (currentRoomId.equals(roomUUID)) {
//                if (call == null) return;
//                call.messageInServer(localMessageId, messageUUID, messageDate);
//            }
//
//            updateRoom(roomUUID);
//
//
//        } catch (JSONException e) {
//
//            Log.e("TAG", "messageInServer: " + e.getMessage());
//
//        }
//
//    }
//
//    private void insertRoom(JSONObject room) {
//
//        String roomUUID = "";
//        String friendId = "";
//        String name = "";
//        String nickname = "";
//        String avatar = "";
//        String colorCode = "";
//        String lastActiveDate = "";
//        int activeStatus = 0;
//        int notification = 1;
//        int roomStatus = 0;
//        int accept = 0;
//        int blocked = 0;
//        int isOpen = 0;
//        int canSendMessage = 0;
//        int peopleType = 0;
//        //int lastMessageId = 0;
//        try {
//            roomUUID = room.getString("uuid");
//            friendId = room.getString("friend_uuid");
//            name = room.getString("name");
//            nickname = room.getString("nickname");
//            avatar = room.getString("avatar");
//            colorCode = room.getString("color_code");
//            roomStatus = room.getInt("status");
//            //lastMessageId = room.getInt("last_message_id");
//            notification = room.getInt("notification");
//            lastActiveDate = room.getString("last_active_date");
//            activeStatus = room.getInt("active_status");
//            accept = room.getInt("accept");
//            blocked = room.getInt("blocked");
//            isOpen = room.getInt("isOpen");
//            canSendMessage = room.getInt("canSendMessage");
//            peopleType = room.getInt("peopleType");
//
//        } catch (JSONException e) {
//            Log.e("TAG", "insertRoom: " + e.getMessage());
//        }
//
//        EntityRoom entityRoom1 = vmRoom.getEntityRoomByFriendId(friendId);
//        if (entityRoom1 != null) {
//
//            EntityRoom entityRoom = new EntityRoom(
//                    roomUUID,
//                    friendId,
//                    name,
//                    nickname,
//                    avatar,
//                    colorCode,
//                    entityRoom1.getLastMessageId(),
//                    roomStatus,
//                    lastActiveDate,
//                    activeStatus,
//                    notification,
//                    accept,
//                    blocked,
//                    isOpen,
//                    canSendMessage,
//                    peopleType,
//                    "");
//
//            entityRoom.setId(entityRoom1.getId());
//
//            vmRoom.update(entityRoom);
//            if (!entityRoom.getUuid().equals(entityRoom1.getUuid())) {
//                vmMessage.updateRoomUUID(entityRoom1.getUuid(), entityRoom.getUuid());
//            }
//
//
//        } else {
//
//            if (friendId.equals(accountPreferences.getPrefUserUUID())) return;
//            vmRoom.insertRoom(new EntityRoom(
//                    roomUUID,
//                    friendId,
//                    name,
//                    nickname,
//                    avatar,
//                    colorCode,
//                    0,
//                    roomStatus,
//                    lastActiveDate,
//                    activeStatus,
//                    notification,
//                    accept,
//                    blocked,
//                    isOpen,
//                    canSendMessage,
//                    peopleType,
//                    ""));
//        }
//
//
//    }
//
//    private void insertMessages(JSONObject message) {
//        String messageUUID = "";
//        int type = 0;
//        String senderId = "";
//        String roomId = "";
//        int status = 2;
//        String date = "";
//        String repliedMessage = "";
//        String content = "";
//        String fileName = "";
//        String singerName = "";
//        String thumbNail = "";
//        int postType = 0;
//        int accountType = 0;
//        int size = 0;
//        int duration = 0;
//        String localFileUrl = "";
//
//        try {
//            content = message.getString("content");
//            messageUUID = message.getString("uuid");
//            type = message.getInt("type");
//            senderId = message.getString("sender_uuid");
//            roomId = message.getString("room_uuid");
////            status = message.getInt("status");
//            date = message.getString("date");
//            repliedMessage = message.getString("replied_content");
//            fileName = message.getString("file_name");
//            singerName = message.getString("singer_name");
//            thumbNail = message.getString("thumbnail");
//            postType = message.getInt("post_type");
//            accountType = message.getInt("account_type");
//            size = message.getInt("file_size");
//            duration = message.getInt("duration");
//        } catch (JSONException e) {
//            Log.e("TAG", "insertMessages: " + e.getMessage());
//
//        }
//
//
//        if (type == TEXT) {
//            content = shared.decrypt(content, roomId);
//        }
//
//
//        EntityMessage newMessage = new EntityMessage(
//                messageUUID,
//                type,
//                senderId,
//                roomId,
//                status,
//                date,
//                repliedMessage,
//                content,
//                fileName,
//                singerName,
//                thumbNail,
//                postType,
//                accountType,
//                size,
//                duration,
//                localFileUrl,
//                "",
//                "",
//                ""
//        );
//
//
//        EntityMessage existingMessage = vmMessage.getMessageByUuid(messageUUID);
//
//        if (existingMessage == null) {
//            int id = (int) vmMessage.insert(newMessage);
//            newMessage.setId(id);
//
//            if (currentRoomId.equals(newMessage.getRoomId()) && call != null) {
//                call.addNewMessage(newMessage);
//            }
//
//
//            vmRoom.updateLastMessageId(roomId, id);
//
//
//            if (!senderId.equals(accountPreferences.getPrefUserUUID())) {
//                JSONObject messageJson = new JSONObject();
//                JSONObject messageJson2 = new JSONObject();
//
//
//                try {
//                    messageJson2.put("message_uuid", messageUUID);
//                    messageJson.put("channel", Channel.MESSAGE_IN_USER);
//                    messageJson2.put("room_uuid", roomId);
//                    messageJson.put("data", messageJson2);
//                } catch (JSONException e) {
//                    Log.e("TAG", "messageInUser: " + e.getMessage());
//                }
//
//
//                sendMessage(messageJson);
//            }
//        } else {
//            newMessage.setId(existingMessage.getId());
//            vmMessage.update(newMessage);
//        }
//
//        String finalRoomId = roomId;
//        handler.postDelayed(() -> AsyncTask.execute(() -> {
//            webSocket.moveRoom(finalRoomId);
//        }), 200);
//
//
//    }
//
//    public void pushNotification(JSONObject room, String name, String content, int type) {
//        String message = "";
//        switch (type) {
//            case VOICE:
//                message = context.getResources().getString(R.string.voice_message);break;
//            case IMAGE:
//                message = context.getResources().getString(R.string.photo);
//                break;
//            case FILE:
//                message = context.getString(R.string.file);
//                break;
//            case MUSIC:
//                message = context.getString(R.string.music);
//                break;
//            case POST:
//                message = context.getString(R.string.publications);
//                break;
//            case ACCOUNT:
//                message = context.getString(R.string.contact);
//                ;
//                break;
//            case GEOLOCATION:
//                message = context.getString(R.string.geolocation);
//                break;
//            case VIDEO:
//                message = context.getString(R.string.video);
//                break;
//            case STORY:
//                String path, comment, uuid, sender;
//                String[] parts = content.split(Pattern.quote(","));
//
//                sender = parts[parts.length - 1];
//                uuid = parts[parts.length - 2];
//                path = parts[parts.length - 3];
//                List<String> list = new ArrayList<>(Arrays.asList(parts));
//                list.remove(sender);
//                list.remove(uuid);
//                list.remove(path);
//
//                comment = String.join(",", list);
//
//                if (comment.isEmpty()) {
//                    message = context.getResources().getString(R.string.story_friends);
//
//                } else {
//                    message = comment;
//
//                }
//
//                break;
//
//            default:
//                try {
//                    message = shared.decrypt(content, room.getString("uuid"));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                break;
//        }
//        Intent activityIntent;
//        PendingIntent contentIntent;
//
//
//        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
//
//        int messageCount = 0;
//        boolean isFirstPos = false;
//        int firstPos = 0;
//
//        int sender = name.hashCode();
//        messages.add(message);
//        ids.add(sender);
//
//
//        for (int i = 0; i < messages.size(); i++) {
//            if (sender == ids.get(i)) {
//                if (!isFirstPos) {
//                    firstPos = i;
//                    isFirstPos = true;
//                }
//                messageCount++;
//            }
//        }
//
//        if (messageCount > 6) {
//            ids.remove(firstPos);
//            messages.remove(firstPos);
//        }
//
//        for (int i = 0; i < messages.size(); i++) {
//            if (sender == ids.get(i)) {
//                inboxStyle.addLine(messages.get(i));
//            }
//        }
//
//
//        activityIntent = new Intent(context, ActivityMain.class);
//
//        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        activityIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//
//        activityIntent.putExtra("notification", "8");
//        activityIntent.putExtra("json", room.toString());
//
//
//        contentIntent = PendingIntent.getActivity(context, 0, activityIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
//
//
//        Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/" + R.raw.coming_message_sounds);
//
//
//        Notification notification = new NotificationCompat.Builder(context, "salam")
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
//                .setContentTitle(name)
//                .setContentText(message)
//                .setPriority(NotificationCompat.PRIORITY_LOW)
//                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//                .setVibrate(new long[]{1500, 1000, 1500, 1000})
//                .setAutoCancel(true)
//                .setSound(soundUri)
//                .setStyle(inboxStyle)
//                .setWhen(System.currentTimeMillis())
//                .setContentIntent(contentIntent)
//                .build();
//
//        notificationManagerCompat.notify(sender, notification);
//    }
//
//    public void sendFileNewRoom(MultipartBody.Part image, int roomId, int localMessageId,
//                                int type, int duration, String friendId, String repliedContent, String singerName, String filename) {
//        String token = accountPreferences.getToken();
//
//        MessageService request = (MessageService) ApiClient.createRequest(MessageService.class);
//
//        Call<DataMessage> messageCall = request.sendFileNewRoom(
//                token,
//                image,
//                RequestBody.create(MediaType.parse("multipart/form-data"), friendId),
//                type,
//                roomId,
//                localMessageId,
//                duration,
//                RequestBody.create(MediaType.parse("multipart/form-data"), repliedContent),
//                RequestBody.create(MediaType.parse("multipart/form-data"), filename),
//                RequestBody.create(MediaType.parse("multipart/form-data"), singerName));
//
//        messageCall.enqueue(new RetrofitCallback<DataMessage>() {
//            @Override
//            public void onResponse(DataMessage response) {
//
//                if (response.isStatus()) {
//                    MessageDTO messageDTO = response.getData();
//
//                    AsyncTask.execute(() -> {
//
//                        vmMessage.updateSentFile(
//                                messageDTO.getRoomUUID(),
//                                messageDTO.getLocalMessageId(),
//                                messageDTO.getMessageUUID(),
//                                messageDTO.getMessageDate(),
//                                messageDTO.getFilePath());
//
//                        vmRoom.updateUUID(messageDTO.getLocalRoomId(), messageDTO.getRoomUUID());
//                        vmMessage.updateRoomUUID(String.valueOf(messageDTO.getLocalRoomId()), messageDTO.getRoomUUID());
//                        vmMessage.updateSentMessage(messageDTO.getRoomUUID(), localMessageId, messageDTO.getMessageUUID(), messageDTO.getMessageDate());
//                        newRoom(String.valueOf(messageDTO.getLocalRoomId()), messageDTO.getRoomUUID());
//                        handler.postDelayed(() -> AsyncTask.execute(() -> updateRoom(messageDTO.getRoomUUID())), 150);
//                        handler.postDelayed(() -> {
//                            if (currentRoomId.equals(messageDTO.getRoomUUID())) {
//                                call.fileInServer(messageDTO.getLocalMessageId(), messageDTO.getMessageUUID(), messageDTO.getMessageDate(), messageDTO.getFilePath());
//
//                            }
//
//                        }, 200);
//
//                    });
//
//
//                } else {
//
//                    /*AsyncTask.execute(() -> {
//                        vmMessage.updateUnsentMessage(localMessageId);
//                    });*/
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//
//                Log.e("TAG", "onFailure: " + t);
//                AsyncTask.execute(() -> {
//                    vmMessage.updateUnsentMessage(localMessageId);
//                    updateRoom(String.valueOf(roomId));
//
//                });
//                if (currentRoomId.equals(String.valueOf(roomId)) && call != null) {
//                    call.unsendMessage(localMessageId);
//                }
//
//
//            }
//        });
//    }
//
//    public void sendFile(MultipartBody.Part image, String roomUUID, int localMessageId, int type, int duration, String repliedContent, String singerName, String filename) {
//        String token = accountPreferences.getToken();
//
//
//        MessageService request = (MessageService) ApiClient.createRequest(MessageService.class);
//
//        Call<DataMessage> messageCall = request.sendFile(
//                token,
//                image,
//                RequestBody.create(MediaType.parse("multipart/form-data"), roomUUID),
//                type,
//                localMessageId,
//                duration,
//                RequestBody.create(MediaType.parse("multipart/form-data"), repliedContent),
//                RequestBody.create(MediaType.parse("multipart/form-data"), filename),
//                RequestBody.create(MediaType.parse("multipart/form-data"), singerName));
//
//
//        messageCall.enqueue(new RetrofitCallback<DataMessage>() {
//            @Override
//            public void onResponse(DataMessage response) {
//
//                if (response.isStatus()) {
//                    MessageDTO messageDTO = response.getData();
//
//
//                    AsyncTask.execute(() -> {
//                        if (currentRoomId.equals(messageDTO.getRoomUUID())) {
//                            call.fileInServer(messageDTO.getLocalMessageId(), messageDTO.getMessageUUID(), messageDTO.getMessageDate(), messageDTO.getFilePath());
//                        }
//
//                        vmMessage.updateSentFile(
//                                messageDTO.getRoomUUID(),
//                                messageDTO.getLocalMessageId(),
//                                messageDTO.getMessageUUID(),
//                                messageDTO.getMessageDate(),
//                                messageDTO.getFilePath());
//
//
//                        updateRoom(messageDTO.getRoomUUID());
//
//                    });
//
//                    Log.e("TAG", "onResponse: " + messageDTO.getFilePath());
//
//                } else {
//
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//
//
//                Log.e("TAG", "onFailure: " + t);
//                Handler handler = new Handler();
//                handler.postDelayed(() -> {
//                    AsyncTask.execute(() -> {
//                        vmMessage.updateUnsentMessage(localMessageId);
//                        updateRoom(roomUUID);
//
//                    });
//                    if (currentRoomId.equals(roomUUID) && call != null) {
//                        call.unsendMessage(localMessageId);
//                    }
//
//                }, 30000);
//            }
//        });
//    }
//
//    public void changeNotificationStatus(String roomUUID, int notification) {
//
//
//        if (notification == 1) {
//
//            notification = Channel.NOTIFICATION_CLOSE;
//
//        } else {
//            notification = Channel.NOTIFICATION_OPEN;
//        }
//        DateTime dateTimeUtc = DateTime.now().withZone(DateTimeZone.UTC);
//
//        JSONObject messageJson = new JSONObject();
//        JSONObject messageJson2 = new JSONObject();
//        try {
//
//            messageJson2.put("room_uuid", roomUUID);
//            messageJson2.put("date", dateTimeUtc + "");
//            messageJson2.put("event", notification);
//            messageJson.put("data", messageJson2);
//            messageJson.put("channel", Channel.ROOM_EVENTS);
//
//
//        } catch (JSONException e) {
//            Log.e("TAG", "initListeners: " + e.getMessage());
//        }
//
//        sendMessage(messageJson);
//
//        int finalNotification = notification;
//
//        AsyncTask.execute(() -> vmRoom.updateNotification(finalNotification, roomUUID));
//
//
//    }
//
//    public void deleteRoom(String roomUUID) {
//        DateTime dateTimeUtc = DateTime.now().withZone(DateTimeZone.UTC);
//
//        JSONObject messageJson = new JSONObject();
//        JSONObject messageJson2 = new JSONObject();
//        try {
//
//            messageJson2.put("room_uuid", roomUUID);
//            messageJson2.put("date", dateTimeUtc + "");
//            messageJson2.put("event", Channel.DELETE_ROOM);
//            messageJson.put("data", messageJson2);
//            messageJson.put("channel", Channel.ROOM_EVENTS);
//
//
//        } catch (JSONException e) {
//            Log.e("TAG", "initListeners: " + e.getMessage());
//        }
//
//        sendMessage(messageJson);
//
//    }
//
//    public void readMessage(String roomUuid, String date) {
//
//        if (currentRoomId.equals(roomUuid) && call != null) {
//            call.readRoom(roomUuid, date);
//        }
//
//        JSONObject messageJson = new JSONObject();
//        JSONObject messageJson2 = new JSONObject();
//        try {
//
//            messageJson2.put("room_uuid", roomUuid);
//            messageJson2.put("date", date);
//            messageJson2.put("event", Channel.READ_ROOM);
//            messageJson.put("data", messageJson2);
//            messageJson.put("channel", "room_events");
//
//
//        } catch (JSONException e) {
//            Log.e("TAG", "initListeners: " + e.getMessage());
//        }
//        AsyncTask.execute(() -> {
//            vmMessage.updateSeenMessage(roomUuid, date);
//            updateRoom(roomUuid);
//            sendMessage(messageJson);
//        });
//
//
//    }
//
//    public void readAllMessages(Room room) {
//
//        JSONObject messageJson = new JSONObject();
//        JSONObject messageJson2 = new JSONObject();
//        try {
//
//            messageJson2.put("room_uuid", room.getUuid());
//            messageJson2.put("date", room.getDate());
//            messageJson2.put("event", Channel.READ_ROOM);
//            messageJson.put("data", messageJson2);
//            messageJson.put("channel", "room_events");
//
//
//        } catch (JSONException e) {
//            Log.e("TAG", "initListeners: " + e.getMessage());
//        }
//        AsyncTask.execute(() -> {
//            vmMessage.updateSeenMessage(room.getUuid(), room.getDate());
//            room.setMessageStatus(Message.STATUS_SEEN);
//            sendMessage(messageJson);
//        });
//
//
//    }
//
//    public void sendSdp(String type, String sessionDescriptionStr, String candidateStr, String friendStr, String callType) {
//        JSONObject jsonObject = new JSONObject();
//        JSONObject jsonObject2 = new JSONObject();
//        JSONObject jsonObject3 = new JSONObject();
//
//        try {
//
//            jsonObject2.put("type", type);
//            jsonObject2.put("candidate", candidateStr);
//            jsonObject2.put("sessionDescription", sessionDescriptionStr);
//            jsonObject2.put("friend", friendStr);
//            jsonObject2.put("call_type", callType);
//            jsonObject3.put("sdp_data", jsonObject2.toString());
//            jsonObject.put("channel", Channel.SEND_SDP);
//            jsonObject.put("data", jsonObject3);
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        sendMessage(jsonObject);
//    }
//
//    public void resendMessage(EntityMessage message, Room room) {
//
//
//        switch (message.getType()) {
//            case TEXT:
//            case GEOLOCATION:
//            case ACCOUNT:
//            case POST:
//            case STORY:
//                resendMessageToSocket(message, room);
//                break;
//            case FILE:
//            case IMAGE:
//            case Message.VIDEO:
//            case Message.MUSIC:
//            case VOICE:
//
//                if (message.getLocalFileUri().equals("")) {
//                    resendMessageToSocket(message, room);
//                } else {
//                    resendFile(message, room);
//                }
//
//                break;
//
//        }
//
//    }
//
//    public void resendFile(EntityMessage message, Room room) {
//        File file = new File(message.getLocalFileUri());
//
//
//        if (file.exists()) {
//
//
//            RequestBody requestFile =
//                    RequestBody.create(
//                            MediaType.parse("multipart/form-data"),
//                            file
//                    );
//
//            MultipartBody.Part resultFile =
//                    MultipartBody.Part.createFormData("files", encode(file.getName(), "UTF-8"), requestFile);
//            if (file.length() > 52428800) {
//
//                activity.runOnUiThread(() -> showToast(activity, R.string.size_too_large));
//                AsyncTask.execute(() -> {
//                    vmMessage.updateUnsentMessage(message.getId());
//                    updateRoom(room.getUuid());
//
//                });
//                if (currentRoomId.equals(room.getUuid())) {
//                    call.unsendMessage(message.getId());
//                }
//                return;
//            }
//            AsyncTask.execute(() -> {
//                vmRoom.updateLastMessageId(message.getRoomId(), message.getId());
//                vmMessage.resendMessage(message.getId(), message.getDate());
//                moveRoom(room.getUuid());
//
//            });
//            if (!room.getUuid().equals(String.valueOf(room.getId()))) {
//                sendFile(resultFile, room.getUuid(), message.getId(), message.getType(), message.getDuration(), message.getReplyMessage(), message.getSingerName(), message.getFileName());
//            } else {
//                sendFileNewRoom(resultFile, room.getId(), message.getId(), message.getType(), message.getDuration(), room.getFriendId(), message.getReplyMessage(), message.getSingerName(), message.getFileName());
//            }
//
//        }
//
//    }
//
//    public void resendMessageToSocket(EntityMessage message, Room room) {
//        String text = message.getContent();
//        JSONObject messageJson = new JSONObject();
//        JSONObject messageJson2 = new JSONObject();
//        if (text.equals("")) {
//            return;
//        }
//        if (message.getType() == TEXT) {
//            text = shared.encrypt(message.getContent(), room.getUuid());
//        }
//
//        try {
//
//
//            if (room.getUuid().equals(String.valueOf(room.getId()))) {
//
//                messageJson.put("channel", "new_room");
//                messageJson2.put("local_room_id", room.getId());
//            } else {
//                messageJson.put("channel", "new_message");
//                messageJson2.put("room_uuid", room.getUuid());
//            }
//
//            if (!message.getReplyMessage().equals("")) {
//                JSONObject json_reply_message = new JSONObject(message.getReplyMessage());
//                messageJson2.put("replied_message_uuid", json_reply_message.get("message_id"));
//                messageJson2.put("replied_content", message.getReplyMessage());
//            }
//
//            messageJson2.put("thumbnail", message.getThumbnail());
//            messageJson2.put("account_type", message.getAccountType());
//            messageJson2.put("post_type", message.getAccountType());
//            messageJson2.put("type", message.getType());
//            messageJson2.put("content", text);
//            messageJson2.put("friend_uuid", room.getFriendId());
//            messageJson2.put("local_message_id", message.getId());
//            messageJson2.put("file_name", message.getFileName());
//            messageJson2.put("singer_name", message.getSingerName());
//            messageJson2.put("file_size", message.getSize());
//            messageJson2.put("duration", message.getDuration());
//            messageJson2.put("replied_content", message.getReplyMessage());
//
//            messageJson.put("data", messageJson2);
//
//
//        } catch (JSONException e) {
//            Log.e("TAG", "initListeners: " + e.getMessage());
//        }
//
//
//        webSocket.sendMessage(messageJson);
//
//        AsyncTask.execute(() -> {
//            vmRoom.updateLastMessageId(message.getRoomId(), message.getId());
//            vmMessage.resendMessage(message.getId(), message.getDate());
//            moveRoom(room.getUuid());
//
//        });
//
//        activity.runOnUiThread(() -> handler.postDelayed(() -> {
//            AsyncTask.execute(() -> {
//                int status = vmMessage.getMessageStatus(message.getId());
//                if (status == Message.STATUS_LOCAL) {
//                    vmMessage.updateUnsentMessage(message.getId());
//                    moveRoom(room.getUuid());
//                    if (currentRoomId.equals(message.getRoomId()) && call != null) {
//                        call.unsendMessage(message.getId());
//                    }
//                }
//            });
//
//        }, 30000));
//
//    }
//
//    public Room getRoom(PosterDTO poster) {
//        Room room;
//
//        String roomUUID = vmRoom.getRoomByFriendId(poster.getUUID());
//
//
//        if (roomUUID == null) {
//            roomUUID = "";
//        }
//
//
//        if (roomUUID.equals("")) {
//
//            room = getEmptyRoom();
//            room.setUuid(roomUUID);
//            room.setFriendId(poster.getUUID());
//            room.setName(poster.getFullName());
//            room.setNickname(poster.getNickname());
//            room.setAvatar(poster.getAvatar());
//            room.setColorCode(poster.getColorCode());
//            room.setCanSendMessage(poster.getCanSendMessage());
//            room.setBlocked(poster.getIsBlocked());
//            room.setPeopleType(poster.getPeopleType());
//            room.setIsOpen(poster.getIsOpen());
//
//        } else {
//            room = vmRoom.getRoomByUUID(roomUUID);
//            if (room == null) {
//                room = getEmptyRoom();
//                room.setUuid(roomUUID);
//                room.setFriendId(poster.getUUID());
//                room.setName(poster.getFullName());
//                room.setNickname(poster.getNickname());
//                room.setAvatar(poster.getAvatar());
//                room.setColorCode(poster.getColorCode());
//                room.setBlocked(poster.getIsBlocked());
//                room.setPeopleType(poster.getPeopleType());
//                room.setIsOpen(poster.getIsOpen());
//                room.setCanSendMessage(poster.getCanSendMessage());
//            }
//        }
//
//        if (room.getUuid().equals("")) {
//
//
//            int roomId = 0;
//
//            String roomUuid = "";
//
//
//            roomId = (int) vmRoom.insert(new EntityRoom(
//                    room.getUuid(),
//                    room.getFriendId(),
//                    room.getName(),
//                    room.getNickname(),
//                    room.getAvatar(),
//                    room.getColorCode(),
//                    room.getMessageId(),
//                    room.getRoomStatus(),
//                    room.getLastActiveDate(),
//                    room.getActiveStatus(),
//                    room.getNotification(),
//                    room.getAccept(),
//                    room.getBlocked(),
//                    room.getIsOpen(),
//                    room.getCanSendMessage(),
//                    room.getPeopleType(), "")
//            );
//            roomUuid = String.valueOf(roomId);
//            room.setId(roomId);
//            room.setUuid(roomUuid);
//
//
//            if (roomId != 0) {
//                vmRoom.updateUUID(room.id, room.getUuid());
//
//            }
//
//        }
//
//        return room;
//    }
//
//    public void updateRoom(String uuid) {
//
//
//        Fragment chat = mainFragmentManager.findFragmentByTag(FragmentChats.class.getName());
//        Fragment chat1 = mainFragmentManager.findFragmentByTag(FragmentChats.class.getName() + "1");
//        if (chat instanceof RoomCallBack) {
//            ((RoomCallBack) chat).updateRoom(uuid);
//        }
//        if (chat1 instanceof RoomCallBack) {
//            if (chat1 == null) return;
//            ((RoomCallBack) chat1).updateRoom(uuid);
//        }
//    }
//
//    public void insertRoom(Room room) {
//        Fragment chat = mainFragmentManager.findFragmentByTag(FragmentChats.class.getName());
//        Fragment chat1 = mainFragmentManager.findFragmentByTag(FragmentChats.class.getName() + "1");
//
//        if (chat instanceof RoomCallBack) {
//            ((RoomCallBack) chat).insertRoom(room);
//        }
//
//        if (chat1 instanceof RoomCallBack) {
//            if (chat1 == null) return;
//            ((RoomCallBack) chat1).insertRoom(room);
//        }
//    }
//
//    public void moveRoom(String uuid) {
//        Fragment chat = mainFragmentManager.findFragmentByTag(FragmentChats.class.getName());
//        Fragment chat1 = mainFragmentManager.findFragmentByTag(FragmentChats.class.getName() + "1");
//
//        if (chat instanceof RoomCallBack) {
//            ((RoomCallBack) chat).moveRoom(uuid);
//        }
//        if (chat1 instanceof RoomCallBack) {
//            if (chat1 == null) return;
//            ((RoomCallBack) chat1).moveRoom(uuid);
//        }
//    }
//
//    public void removeRoom(String uuid) {
//        Fragment chat = mainFragmentManager.findFragmentByTag(FragmentChats.class.getName());
//        Fragment chat1 = mainFragmentManager.findFragmentByTag(FragmentChats.class.getName() + "1");
//
//        if (chat instanceof RoomCallBack) {
//            ((RoomCallBack) chat).deleteRoom(uuid);
//        }
//        if (chat1 instanceof RoomCallBack) {
//            if (chat1 == null) return;
//            ((RoomCallBack) chat1).deleteRoom(uuid);
//        }
//    }
//
//    public void newRoom(String id, String uuid) {
//        Fragment chat = mainFragmentManager.findFragmentByTag(FragmentChats.class.getName());
//        Fragment chat1 = mainFragmentManager.findFragmentByTag(FragmentChats.class.getName() + "1");
//
//        if (chat instanceof RoomCallBack) {
//            ((RoomCallBack) chat).newRoom(id, uuid);
//        }
//        if (chat instanceof RoomCallBack) {
//            if (chat1 == null) return;
//            ((RoomCallBack) chat1).newRoom(id, uuid);
//        }
//
//    }
//
//    public void acceptRoom(Room room) {
//        Fragment chat = mainFragmentManager.findFragmentByTag(FragmentChats.class.getName());
//        Fragment chat1 = mainFragmentManager.findFragmentByTag(FragmentChats.class.getName() + "1");
//
//        if (chat instanceof RoomCallBack) {
//            ((RoomCallBack) chat).acceptRoom(room);
//        }
//        if (chat instanceof RoomCallBack) {
//            if (chat1 == null) return;
//            ((RoomCallBack) chat1).acceptRoom(room);
//        }
//    }
//
//    public void notifyList() {
//        Fragment chat = mainFragmentManager.findFragmentByTag(FragmentChats.class.getName());
//        Fragment chat1 = mainFragmentManager.findFragmentByTag(FragmentChats.class.getName() + "1");
//
//        if (chat instanceof RoomCallBack) {
//            ((RoomCallBack) chat).notifyList();
//        }
//        if (chat instanceof RoomCallBack) {
//            if (chat1 == null) return;
//            ((RoomCallBack) chat1).notifyList();
//        }
//    }
//
//    public void setCallBack(MessageCallBack call) {
//        this.call = call;
//    }
//
//    public void setCallBack(PostCallBack postCallBack) {
//        this.postCallBack = postCallBack;
//    }
//
//    public void sendMessage(EntityMessage message, Room room, String repliedMessageUuid) {
//
//        JSONObject messageJson = new JSONObject();
//        JSONObject messageJson2 = new JSONObject();
//
//        try {
//
//
//            if (room.getUuid().equals(String.valueOf(room.getId()))) {
//
//                messageJson.put("channel", "new_room");
//                messageJson2.put("local_room_id", room.getId());
//            } else {
//                messageJson.put("channel", "new_message");
//                messageJson2.put("room_uuid", room.getUuid());
//            }
//
//            if (!message.getReplyMessage().isEmpty()) {
//                messageJson2.put("replied_message_uuid", repliedMessageUuid);
//                messageJson2.put("replied_content", message.getReplyMessage());
//            }
//            String content = message.getContent();
//            if (message.getType() == TEXT) {
//                content = StaticVariables.shared.encrypt(message.getContent(), room.getUuid());
//            }
//
//            messageJson2.put("type", message.getType());
//            messageJson2.put("content", content);
//            messageJson2.put("friend_uuid", room.getFriendId());
//            messageJson2.put("account_type", message.getAccountType());
//            messageJson2.put("local_message_id", message.getId());
//            messageJson2.put("file_name", message.getFileName());
//            messageJson2.put("replied_content", message.getReplyMessage());
//            messageJson2.put("singer_name", message.getSingerName());
//            messageJson2.put("file_size", message.getSize());
//            messageJson2.put("duration", message.getDuration());
//            messageJson.put("data", messageJson2);
//
//
//        } catch (JSONException e) {
//            Log.e("TAG", "initListeners: " + e.getMessage());
//        }
//
//
//        vmRoom.updateLastMessageId(room.getUuid(), message.getId());
//        moveRoom(room.getUuid());
//        updateRoom(room.getUuid());
//
//        webSocket.sendMessage(messageJson);
//
//
//        activity.runOnUiThread(() -> {
//
//            handler.postDelayed(() -> {
//                AsyncTask.execute(() -> {
//                    int status = vmMessage.getMessageStatus(message.getId());
//                    if (status == Message.STATUS_LOCAL) {
//                        vmMessage.updateUnsentMessage(message.getId());
//                        updateRoom(room.getUuid());
//
//                        if (currentRoomId.equals(message.getRoomId())) {
//                            call.unsendMessage(message.getId());
//                        }
//                    }
//
//                });
//
//            }, 30000);
//
//
//        });
//    }
//
//    public void sendFile(Activity activity, Room room, String filename, int fileSize, String fullFilePath, File resultFile, int type, int duration, String author, String replyMessage) {
//
//
//        EntityMessage entityMessage = getEmptyMessage(type);
//        entityMessage.setRoomId(room.getUuid());
//        entityMessage.setFileName(filename);
//        entityMessage.setSize(fileSize);
//        entityMessage.setLocalFileUri(fullFilePath);
//        entityMessage.setSingerName(author);
//        entityMessage.setDuration(duration);
//        entityMessage.setReplyMessage(replyMessage);
//
//
//        RequestBody requestFile =
//                RequestBody.create(
//                        MediaType.parse("multipart/form-data"),
//                        resultFile
//                );
//
//        MultipartBody.Part file =
//                MultipartBody.Part.createFormData("files", encode(resultFile.getName(), "UTF-8"), requestFile);
//        int id = (int) vmMessage.insert(entityMessage);
//        entityMessage.setId(id);
//        if (currentRoomId.equals(entityMessage.getRoomId()) && call != null) {
//            call.addNewMessage(entityMessage);
//        }
//        vmRoom.updateLastMessageId(room.getUuid(), id);
//        webSocket.moveRoom(room.getUuid());
//        webSocket.updateRoom(room.getUuid());
//
//
//        if (resultFile.length() > 52428800) {
//            activity.runOnUiThread(() -> showToast(activity, R.string.size_too_large));
//            AsyncTask.execute(() -> {
//                vmMessage.updateUnsentMessage(entityMessage.getId());
//                updateRoom(room.getUuid());
//
//            });
//            if (currentRoomId.equals(room.getUuid()) && call != null) {
//                webSocket.call.unsendMessage(entityMessage.getId());
//            }
//            return;
//        }
//        activity.runOnUiThread(() -> {
//            if (!room.getUuid().equals(String.valueOf(room.getId()))) {
//                handler.postDelayed(() -> webSocket.sendFile(file, room.getUuid(), id, type, duration, replyMessage, author, filename), 150);
//
//            } else {
//                handler.postDelayed(() -> webSocket.sendFileNewRoom(file, room.getId(), id, type, duration, room.getFriendId(), replyMessage, author, filename), 300);
//
//
//            }
//        });
//    }
//
//    public interface RoomCallBack {
//        void updateRoom(String uuid);
//
//        void insertRoom(Room room);
//
//        void moveRoom(String uuid);
//
//        void deleteRoom(String uuid);
//
//        void newRoom(String id, String uuid);
//
//        void acceptRoom(Room room);
//
//        void notifyList();
//    }
//
//    public interface PostCallBack {
//        void onNewPost();
//    }
//
//    public Room getEmptyRoom() {
//        return new Room(
//                0,
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                0,
//                0,
//                0,
//                0,
//                "",
//                "",
//                "",
//                "",
//                0,
//                "",
//                0,
//                1,
//                1,
//                0,
//                1,
//                1,
//                3
//        );
//    }
//
//    public void sendAccept(String roomUuid) {
//        JSONObject jsonObject = new JSONObject();
//        JSONObject jsonObject2 = new JSONObject();
//
//        try {
//
//            jsonObject2.put("roomId", roomUuid);
//            jsonObject.put("channel", "accept");
//            jsonObject.put("data", jsonObject2);
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        webSocket.sendMessage(jsonObject);
//    }
//
//    public static boolean isAppOnForeground(Context context, String appPackageName) {
//        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
//        if (appProcesses == null) {
//            return false;
//        }
//        final String packageName = appPackageName;
//        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
//            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
//                //                Log.e("app",appPackageName);
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public void getUserRooms(String createdAt) {
//        MessageService request = (MessageService) ApiClient.createRequest(MessageService.class);
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("createdAt", createdAt);
//
//        Call<DataRooms> callDataRooms = request.getUserRooms(accountPreferences.getToken(), jsonObject);
//        callDataRooms.enqueue(new RetrofitCallback<>() {
//            @Override
//            public void onResponse(DataRooms response) {
//                Log.e("TAG", "onResponse: " + response.getMessage());
//                Gson gson = new Gson();
//                String rooms = gson.toJson(response);
//                Log.e("TAG", "onResponse: " + rooms);
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//
//                Log.e("TAG", "onFailure: " + t.getMessage());
//
//
//            }
//        });
//    }
//}
