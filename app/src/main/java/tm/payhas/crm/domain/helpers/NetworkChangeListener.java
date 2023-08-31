package tm.payhas.crm.domain.helpers;


import static tm.payhas.crm.presentation.view.activity.ActivityMain.webSocket;

public interface NetworkChangeListener {
    default void onNetworkConnected() {
        webSocket.startWebSocketConnection();
        webSocket.reconnectStatus(true);
    }

    default void onNetworkDisconnected() {
        webSocket.stopWebSocketConnection();
        webSocket.reconnectStatus(false);
    }
}