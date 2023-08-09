package tm.payhas.crm.interfaces;

import android.view.View;

public interface OnInternetStatus {
    default void setNoInternet(View progressBar, View noInternet, View main) {
        progressBar.setVisibility(View.GONE);
        noInternet.setVisibility(View.VISIBLE);
        main.setVisibility(View.GONE);
    }

    default void setConnected(View progressBar, View noInternet, View main) {
        progressBar.setVisibility(View.GONE);
        noInternet.setVisibility(View.GONE);
        main.setVisibility(View.VISIBLE);
    }
}
