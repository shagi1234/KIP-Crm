package tm.payhas.crm.webSocket;

import static tm.payhas.crm.api.network.Network.BASE_URL_SOCKET;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import dev.gustavoavila.websocketclient.WebSocketClient;
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
}
