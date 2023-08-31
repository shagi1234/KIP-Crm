package tm.payhas.crm.data.remote.webSocket;

import static tm.payhas.crm.data.remote.api.network.Network.BASE_URL_SOCKET;
import static tm.payhas.crm.domain.statics.StaticConstants.MESSAGES_RECEIVED;
import static tm.payhas.crm.domain.statics.StaticConstants.MESSAGE_STATUS;
import static tm.payhas.crm.domain.statics.StaticConstants.NEW_ROOM;
import static tm.payhas.crm.domain.statics.StaticConstants.RECEIVED_NEW_MESSAGE;
import static tm.payhas.crm.domain.statics.StaticConstants.REMOVE_MESSAGE;
import static tm.payhas.crm.domain.statics.StaticConstants.USER_STATUS;
import static tm.payhas.crm.presentation.view.fragment.FragmentFlow.BADGE;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;


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
    private static final long RECONNECT_DELAY = 5000; // 5 seconds
    private boolean shouldReconnect = true;

    public WebSocket(Context context) {
        this.context = context;
        useCaseChatRoom = new UseCaseChatRoom(context, 0);
        useCaseUsers = new UseCaseUsers(context);
        createWebSocketClient();
    }

    public void startWebSocketConnection() {
        if (webSocketClient == null) {
            createWebSocketClient();
        }
    }

    public void stopWebSocketConnection() {
        if (webSocketClient != null) {
            webSocketClient.close(1000, 100, "Normal"); // Close the WebSocket with code and reason
            webSocketClient = null;
        }
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
                if (shouldReconnect) {
                    reconnectWithDelay(RECONNECT_DELAY);
                }
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
        JSONObject messageReceivedJson = messageJson.optJSONObject("data");
        if (messageReceivedJson != null) {
            EntityMessage firstMessage = new Gson().fromJson(String.valueOf(messageReceivedJson), EntityMessage.class);
            useCaseChatRoom.insertMessage(firstMessage);
        } else {
            // Handle the case where "data" is not a valid JSON object
        }
    }

    private void receivedNewRoom(JSONObject messageJson) throws JSONException {
        JSONObject newRoom = messageJson.optJSONObject("data");
        if (newRoom != null) {
            DataNewRoom newRoomMessage = new Gson().fromJson(String.valueOf(newRoom), DataNewRoom.class);
            useCaseUsers.getAllUsers();
            BADGE.setVisible(true);
        } else {
            // Handle the case where "data" is not a valid JSON object
        }

    }

    private void receivedRemoveMessage(JSONObject messageJson) {
        JSONObject removeInfo = messageJson.optJSONObject("data");
        if (removeInfo != null) {
            EntityMessage deletedMessage = new Gson().fromJson(String.valueOf(removeInfo), EntityMessage.class);
            useCaseChatRoom.deleteMessage(deletedMessage);
        } else {
            // Handle the case where "data" is not a valid JSON object
        }
    }

    private void receivedUserStatus(JSONObject messageJson) {
        JSONObject statusInfo = messageJson.optJSONObject("data");
        if (statusInfo != null) {
            DataUserStatus statusUser = new Gson().fromJson(String.valueOf(statusInfo), DataUserStatus.class);
            if (statusUser.isActive())
                useCaseUsers.updateUserStatus(statusUser.getUserId(), statusUser.isActive(), "0");
            else
                useCaseUsers.updateUserStatus(statusUser.getUserId(), statusUser.isActive(), statusUser.getDate().toString());
        } else {
            // Handle the case where "data" is not a valid JSON object
        }

    }

    private void receivedNewMessage(JSONObject messageJson) {
        JSONObject receivedMessage = messageJson.optJSONObject("data");
        if (receivedMessage != null) {
            EntityMessage newMessage = new Gson().fromJson(String.valueOf(receivedMessage), EntityMessage.class);
            updateReceivedMessageRoom(newMessage.getText(), newMessage.getAuthorId(), newMessage.getRoomId(), newMessage.getCreatedAt());
            useCaseChatRoom.insertMessage(newMessage);
            if (currentRoomId != 0) {
                UseCaseChatRoom currentUseCase = UseCaseChatRoom.getInstance(context, currentRoomId);
                currentUseCase.readMessage(newMessage);
            }
            useCaseUsers.getAllUsers();
            BADGE.setVisible(true);
        } else {
            // Handle the case where "data" is not a valid JSON object
        }
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
        BADGE.setVisible(true);
    }

    public void sendMessage(String s) {
        webSocketClient.send(s);
    }

    public void setUserStatus(EmmitUserStatus s) {
        try {
            webSocketClient.send(new Gson().toJson(s));
        } catch (Exception e) {
            Log.e(TAG, "setUserStatus: " + e);
        }
    }


    private void reconnectWithDelay(long delay) {
        if (shouldReconnect) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (!connected) {
                    Log.d(TAG, "Reconnecting...");
                    stopWebSocketConnection();
                    startWebSocketConnection();
                    reconnectWithDelay(delay * 2); // Increase delay for next attempt
                }
            }, delay);
        }
    }

    public void reconnectStatus(boolean shouldReconnect) {
        this.shouldReconnect = shouldReconnect;
    }
}
