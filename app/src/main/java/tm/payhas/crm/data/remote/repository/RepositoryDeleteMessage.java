package tm.payhas.crm.data.remote.repository;


import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.payhas.crm.data.remote.api.response.ResponseOneMessage;
import tm.payhas.crm.domain.helpers.Common;

public class RepositoryDeleteMessage {
    private final String TAG = "DeleteMessageRepository";

    public void removeMessage(String token, int messageId) {
        Call<ResponseOneMessage> call = Common.getApi().removeMessage(token, messageId);
        call.enqueue(new Callback<ResponseOneMessage>() {
            @Override
            public void onResponse(Call<ResponseOneMessage> call, Response<ResponseOneMessage> response) {
                Log.e(TAG, "onResponse: " + messageId);
            }

            @Override
            public void onFailure(Call<ResponseOneMessage> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage());
            }
        });
    }
}
