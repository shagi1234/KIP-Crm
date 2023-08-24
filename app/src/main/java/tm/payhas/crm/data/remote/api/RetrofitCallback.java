package tm.payhas.crm.data.remote.api;

import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class RetrofitCallback<T> implements Callback<T> {

    public abstract void onResponse(T response);

    public abstract void onFailure(Throwable t);

    @Override
    public void onResponse(@NonNull Call<T> call, Response<T> response) {

        if (response.code() == 200 && response.body() != null) {
            onResponse(response.body());
        } else {
            onFailure(new Throwable(response.message()));
        }
    }

    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        onFailure(t);
    }
}
