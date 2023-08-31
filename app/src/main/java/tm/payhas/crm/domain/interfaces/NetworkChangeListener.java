package tm.payhas.crm.domain.interfaces;

import static tm.payhas.crm.presentation.view.activity.ActivityMain.webSocket;

public interface NetworkChangeListener {
    default void onNetworkConnected() {
        webSocket.startWebSocketConnection();
        webSocket.reconnectStatus(true);
    }

    default void onNetworkDisconnected() {
        webSocket.startWebSocketConnection();
        webSocket.reconnectStatus(false);
    }
}