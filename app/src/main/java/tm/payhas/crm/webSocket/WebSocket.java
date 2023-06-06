package tm.payhas.crm.webSocket;

import static tm.payhas.crm.activity.ActivityMain.mainFragmentManager;
import static tm.payhas.crm.api.network.Network.BASE_URL_SOCKET;
import static tm.payhas.crm.statics.StaticConstants.MESSAGE_STATUS;
import static tm.payhas.crm.statics.StaticConstants.RECEIVED_NEW_MESSAGE;
import static tm.payhas.crm.statics.StaticConstants.USER_STATUS;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import dev.gustavoavila.websocketclient.WebSocketClient;
import tm.payhas.crm.dataModels.DataMessageTarget;
import tm.payhas.crm.dataModels.DataUserStatus;
import tm.payhas.crm.fragment.FragmentChatRoom;
import tm.payhas.crm.interfaces.ChatRoomInterface;
import tm.payhas.crm.interfaces.MessageCallBack;
import tm.payhas.crm.preference.AccountPreferences;

public class WebSocket {

    private WebSocketClient webSocketClient;
    private Context context;
    private Activity activity;
    private final String TAG = "WebSocket";
    NotificationManagerCompat notificationManagerCompat;
    private AccountPreferences accountPreferences;
    public Boolean connected = false;
    public static ArrayList<String> messages;
    public static ArrayList<Integer> ids;
    private Handler handler = new Handler();
    public MessageCallBack call;

    public WebSocket(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        messages = new ArrayList<>();
        ids = new ArrayList<>();
        notificationManagerCompat = NotificationManagerCompat.from(context);
        accountPreferences = AccountPreferences.newInstance(context);
    }

    public void createWebSocketClient() {
        URI uri;
        if (connected)
            return;
        try {
            uri = new URI(BASE_URL_SOCKET + accountPreferences.getTokenForWebSocket());
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
                            JSONArray receivedArray = messageJson.getJSONArray("data");
                            ArrayList<DataMessageTarget> list = new ArrayList<>();
                            for (int i = 0; i < receivedArray.length(); i++) {
                                DataMessageTarget newMessage = new Gson().fromJson(String.valueOf(receivedArray.get(i)), DataMessageTarget.class);
                                activity.runOnUiThread(() -> {
                                    Fragment chatRoom = mainFragmentManager.findFragmentByTag(FragmentChatRoom.class.getSimpleName());
                                    if (chatRoom instanceof ChatRoomInterface) {
                                        ((ChatRoomInterface) chatRoom).newMessage(newMessage);
                                    }
                                });
                                list.add(newMessage);
                            }
                            Log.e(TAG, "onTextReceived: " + list.size());
                            break;
                        case RECEIVED_NEW_MESSAGE:
                            JSONObject receivedMessage = messageJson.getJSONObject("data");
                            DataMessageTarget newMessage = new Gson().fromJson(String.valueOf(receivedMessage), DataMessageTarget.class);
                            Log.e(TAG, "onTextReceived: " + "MessageReceived");
                            activity.runOnUiThread(() -> {
                                Fragment chatRoom = mainFragmentManager.findFragmentByTag(FragmentChatRoom.class.getSimpleName());
                                if (chatRoom instanceof ChatRoomInterface) {
                                    ((ChatRoomInterface) chatRoom).newMessage(newMessage);
                                }
                            });
                            break;
                        case USER_STATUS:
                            boolean status;
                            JSONObject statusInfo = messageJson.getJSONObject("data");
                            DataUserStatus statusUser = new Gson().fromJson(String.valueOf(statusInfo), DataUserStatus.class);
                            status = statusUser.isActive();
                            activity.runOnUiThread(() -> {
                                Fragment chatRoom = mainFragmentManager.findFragmentByTag(FragmentChatRoom.class.getSimpleName());
                                if (chatRoom instanceof ChatRoomInterface) {
                                    ((ChatRoomInterface) chatRoom).userStatus(status);
                                }
                            });
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
            }

            @Override
            public void onCloseReceived(int reason, String description) {
                Log.e(TAG, "onCloseReceived: socket connection closed");
            }
        };
        webSocketClient.setConnectTimeout(5000);
        webSocketClient.setReadTimeout(70000);
        webSocketClient.enableAutomaticReconnection(5000);
        webSocketClient.connect();
    }

    public void sendMessage(String s) {
        webSocketClient.send(s);
        Log.e(TAG, "sendMessage: " + s);
    }

    public void setUserStatus(EmmitUserStatus s) {
        webSocketClient.send(new Gson().toJson(s));
    }
}
