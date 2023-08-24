package tm.payhas.crm.data.remote.webSocket;

import static tm.payhas.crm.data.remote.api.network.Network.BASE_URL_SOCKET;
import static tm.payhas.crm.domain.statics.StaticConstants.MESSAGES_RECEIVED;
import static tm.payhas.crm.domain.statics.StaticConstants.MESSAGE_STATUS;
import static tm.payhas.crm.domain.statics.StaticConstants.NEW_ROOM;
import static tm.payhas.crm.domain.statics.StaticConstants.RECEIVED_NEW_MESSAGE;
import static tm.payhas.crm.domain.statics.StaticConstants.REMOVE_MESSAGE;
import static tm.payhas.crm.domain.statics.StaticConstants.USER_STATUS;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import dev.gustavoavila.websocketclient.WebSocketClient;
import tm.payhas.crm.data.localdb.entity.EntityMessage;
import tm.payhas.crm.domain.model.DataNewRoom;
import tm.payhas.crm.domain.model.DataUserStatus;
import tm.payhas.crm.domain.useCases.UseCaseChatRoom;
import tm.payhas.crm.domain.useCases.UseCaseUsers;
import tm.payhas.crm.data.localdb.preference.AccountPreferences;

public class WebSocket {
    private WebSocketClient webSocketClient;
    private Context context;
    private final String TAG = "WebSocket";
    public Boolean connected = false;
    private UseCaseChatRoom useCaseChatRoom;
    private UseCaseUsers useCaseUsers;
    private Integer currentRoomId = 0;

    public WebSocket(Context context) {
        this.context = context;
        useCaseChatRoom = new UseCaseChatRoom(context, 0);
        useCaseUsers = new UseCaseUsers(context);
    }

    public void setCurrentRoomId(Integer currentRoomId) {
        this.currentRoomId = currentRoomId;
    }

    public void createWebSocketClient() {
        URI uri;
        try {
            uri = new URI(BASE_URL_SOCKET + AccountPreferences.newInstance(context).getTokenForWebSocket());
            Log.e(TAG, "createWebSocketClient:" + uri);
        } catch (URISyntaxException e) {
            Log.e(TAG, "createWebSocketClient: error " + e.getMessage());
            return;
        }

        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                Log.e(TAG, "onOpen: ");
                connected = true;
            }

            @Override
            public void onTextReceived(String emit) {
                Log.e(TAG, "onTextReceived: " + emit);
                try {
                    JSONObject messageJson = new JSONObject(emit);
                    String event = messageJson.getString("event");

                    switch (event) {
                        case MESSAGE_STATUS:
                            messageStatus(messageJson);
                            break;

                        case RECEIVED_NEW_MESSAGE:
                            receivedNewMessage(messageJson);
                            break;

                        case USER_STATUS:
                            receivedUserStatus(messageJson);
                            break;

                        case REMOVE_MESSAGE:
                            receivedRemoveMessage(messageJson);
                            break;

                        case NEW_ROOM:
                            receivedNewRoom(messageJson);
                            break;

                        case MESSAGES_RECEIVED:
                            receivedMessages(messageJson);
                            break;


                    }

                } catch (JSONException e) {


                    e.printStackTrace();
                }

            }

            @Override
            public void onBinaryReceived(byte[] data) {
                System.out.println("onBinaryReceived");
            }

            @Override
            public void onPingReceived(byte[] data) {
                Log.e(TAG, "onPingReceived: " + data);
            }

            @Override
            public void onPongReceived(byte[] data) {
                System.out.println("onPongReceived");
            }

            @Override
            public void onException(Exception e) {
                Log.e(TAG, "onException: !!!" + e.getMessage());
                connected = false;
            }

            @Override
            public void onCloseReceived(int reason, String description) {
                Log.e(TAG, "onCloseReceived: socket connection closed");
                connected = false;

            }
        };
        webSocketClient.setConnectTimeout(10000);
        webSocketClient.setReadTimeout(60000);
        webSocketClient.enableAutomaticReconnection(5000);
        webSocketClient.connect();
    }

    private void receivedMessages(JSONObject messageJson) {
        JSONObject messageReceivedJson = null;
        try {
            messageReceivedJson = messageJson.getJSONObject("data");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        EntityMessage firstMessage = new Gson().fromJson(String.valueOf(messageReceivedJson), EntityMessage.class);
        useCaseChatRoom.insertMessage(firstMessage);
    }

    private void receivedNewRoom(JSONObject messageJson) throws JSONException {
        JSONObject newRoom = messageJson.getJSONObject("data");
        DataNewRoom newRoomMessage = new Gson().fromJson(String.valueOf(newRoom), DataNewRoom.class);
        useCaseUsers.getAllUsers();

    }

    private void receivedRemoveMessage(JSONObject messageJson) {
        Log.e(TAG, "onTextReceived: " + "delete Received");
        JSONObject removeInfo = null;
        try {
            removeInfo = messageJson.getJSONObject("data");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        EntityMessage deletedMessage = new Gson().fromJson(String.valueOf(removeInfo), EntityMessage.class);
        useCaseChatRoom.deleteMessage(deletedMessage);
    }

    private void receivedUserStatus(JSONObject messageJson) {
        JSONObject statusInfo = null;
        try {
            statusInfo = messageJson.getJSONObject("data");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        DataUserStatus statusUser = new Gson().fromJson(String.valueOf(statusInfo), DataUserStatus.class);
        if (statusUser.isActive())
            useCaseUsers.updateUserStatus(statusUser.getUserId(), statusUser.isActive(), "0");
        else
            useCaseUsers.updateUserStatus(statusUser.getUserId(), statusUser.isActive(), statusUser.getDate().toString());

    }

    private void receivedNewMessage(JSONObject messageJson) {
        JSONObject receivedMessage = null;
        try {
            receivedMessage = messageJson.getJSONObject("data");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        EntityMessage newMessage = new Gson().fromJson(String.valueOf(receivedMessage), EntityMessage.class);
        updateReceivedMessageRoom(newMessage.getText(), newMessage.getAuthorId(), newMessage.getRoomId(), newMessage.getCreatedAt());
        useCaseChatRoom.insertMessage(newMessage);
        if (currentRoomId != 0) {
            UseCaseChatRoom currentUseCase = UseCaseChatRoom.getInstance(context, currentRoomId);
            currentUseCase.readMessage(newMessage);
        }
        useCaseUsers.getAllUsers();


    }

    private void updateReceivedMessageRoom(String text, int authorId, Integer roomId, String createdAt) {
        useCaseUsers.addNewMessageCount(authorId);
        useCaseUsers.setMessageRoomText(text, authorId, roomId, createdAt);
    }

    private void messageStatus(JSONObject messageJson) {
        JSONArray receivedArray = null;
        try {
            receivedArray = messageJson.getJSONArray("data");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        List<EntityMessage> list = new Gson().fromJson(receivedArray.toString(), new TypeToken<ArrayList<EntityMessage>>() {
        }.getType());
        useCaseChatRoom.insertAll(list);

        for (EntityMessage newMessage : list) {
            // Call the updateReceivedMessageRoom function for each newMessage
            Log.e(TAG, "messageStatus: called");

            useCaseUsers.setMessageRoomText(newMessage.getText(), newMessage.getFriendId(), newMessage.getRoomId(), newMessage.getCreatedAt());
        }
        Log.e(TAG, "onTextReceived: " + list.size());
        useCaseUsers.getAllUsers();
    }

    public void sendMessage(String s) {
        webSocketClient.send(s);
    }

    public void connectToWebSocket() {
        if (!connected) createWebSocketClient();
        Log.e(TAG, "connectToWebSocket: " + connected);
    }

    public void setUserStatus(EmmitUserStatus s) {
        webSocketClient.send(new Gson().toJson(s));
    }
}
