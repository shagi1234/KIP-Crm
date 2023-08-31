package tm.payhas.crm.domain.helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import tm.payhas.crm.domain.interfaces.NetworkChangeListener;

public class NetworkConnectivityUtil {
    private NetworkChangeListener listener;
    private BroadcastReceiver networkReceiver;

    public NetworkConnectivityUtil(Context context, NetworkChangeListener listener) {
        this.listener = listener;
        networkReceiver = new NetworkChangeReceiver();
        context.registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public void unregisterReceiver(Context context) {
        context.unregisterReceiver(networkReceiver);
    }

    private class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                listener.onNetworkConnected();
            } else {
                listener.onNetworkDisconnected();
            }
        }
    }
}
