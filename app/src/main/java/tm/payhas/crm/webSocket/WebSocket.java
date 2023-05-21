package tm.payhas.crm.webSocket;

import static tm.payhas.crm.api.network.Network.BASE_URL_SOCKET;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import dev.gustavoavila.websocketclient.WebSocketClient;
import tm.payhas.crm.interfaces.MessageCallBack;
import tm.payhas.crm.preference.AccountPreferences;

public class WebSocket {

    WebSocketClient webSocketClient;
    Context context;
    Activity activity;
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

        try {
            uri = new URI(BASE_URL_SOCKET + accountPreferences.getTokenForWebSocket());
            Log.e(TAG, "createWebSocketClient: " + accountPreferences.getTokenForWebSocket());
        } catch (URISyntaxException e) {
            Log.e(TAG, "createWebSocketClient: " + e.getMessage());
            return;
        }

        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                Log.e(TAG, "onOpen: ");
            }

            @Override
            public void onTextReceived(String emit) {

//                try {
//                    JSONObject messageJson = new JSONObject(emit);
//                    Log.e(TAG, "onTextReceived: " + messageJson);
//                } catch (JSONException e) {
//                    Log.e(TAG, "onTextReceived: " + e);
//                }
                Log.e("TAG", "onTextReceived: " + emit);
                JSONObject dataObj;
                JSONArray dataArr;
                try {

                    JSONObject messageJson = new JSONObject(emit);
                    String channel = messageJson.getString("type");
                    switch (channel) {
//
//                        case STRING:
//                            dataObj = messageJson.getJSONObject("data");
////                            newMessage(dataObj);
//                            break;
//
//                        case PHOTO:
//                            dataObj = messageJson.getJSONObject("data");
////                            newMessagePhoto(dataObj);
//                            break;
//                        case VOICE:
//                            dataObj = messageJson.getJSONObject("data");
////                            newMessageVoice(dataObj);
//                            break;
//
//                        case FILE:
//                            dataObj = messageJson.getJSONObject("data");
////                            newMessageFile();
//                            break;

                    }


                } catch (JSONException e) {

                    Log.e("TAG", "onTextReceived:" + e.getMessage());

                }


            }

            @Override
            public void onBinaryReceived(byte[] data) {
                System.out.println("onBinaryReceived");
            }

            @Override
            public void onPingReceived(byte[] data) {
                System.out.println("onPingReceived");
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
        Log.e(TAG, "sendMessage: " + "messageSent");
        Log.e(TAG, "sendMessage: "+s );
    }
}
