package tm.payhas.crm.fragment;

import static tm.payhas.crm.helpers.StaticMethods.hideSoftKeyboard;
import static tm.payhas.crm.helpers.StaticMethods.navigationBarHeight;
import static tm.payhas.crm.helpers.StaticMethods.setPadding;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.payhas.crm.R;
import tm.payhas.crm.activity.ActivityMain;
import tm.payhas.crm.api.data.response.ResponseSignIn;
import tm.payhas.crm.databinding.FragmentLoginBinding;
import tm.payhas.crm.helpers.Common;
import tm.payhas.crm.helpers.StaticMethods;
import tm.payhas.crm.preference.AccountPreferences;


public class FragmentLogin extends Fragment {
    private FragmentLoginBinding b;
    private AccountPreferences accountPreferences;

    public static FragmentLogin newInstance() {
        FragmentLogin fragment = new FragmentLogin();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(() -> setPadding(b.getRoot(), 0, 30, 0, navigationBarHeight), 100);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentLoginBinding.inflate(inflater);
        accountPreferences = new AccountPreferences(getContext());
        setBackground();
        initListeners();
        return b.getRoot();
    }

    private void initListeners() {
        b.btnLoginCont.setOnClickListener(view -> {
            if (getActivity() != null) {
                logIn();
            }
        });
        b.container.setOnClickListener(view -> hideSoftKeyboard(getActivity()));
        b.inputTaker.setOnClickListener(view -> hideSoftKeyboard(getActivity()));
        b.loginContent.setOnClickListener(view -> hideSoftKeyboard(getActivity()));
    }

    private void setBackground() {
        StaticMethods.setBackgroundDrawable(getContext(), b.btnLogin, R.color.primary, 0, 8, false, 0);
        StaticMethods.setBackgroundDrawable(getContext(), b.btnRegister, 0, R.color.black, 8, false, 1);
        StaticMethods.setBackgroundDrawable(getContext(), b.nameCont, 0, R.color.inactive_stroke, 8, false, 1);
        StaticMethods.setBackgroundDrawable(getContext(), b.passwordCont, 0, R.color.inactive_stroke, 8, false, 1);
    }

    private void logIn() {
        String username = b.edtUsername.getText().toString();
        String password = b.edtPassword.getText().toString();
        JsonObject info = new JsonObject();
        info.addProperty("username", username);
        info.addProperty("password", password);
        Call<ResponseSignIn> call = Common.getApi().signIn(info);
        call.enqueue(new Callback<ResponseSignIn>() {
            @Override
            public void onResponse(Call<ResponseSignIn> call, Response<ResponseSignIn> response) {
                if (response.code() == 201) {
                    accountPreferences.setPrefUserName(response.body().getData().getPersonalData().getName());
                    accountPreferences.setToken(response.body().getData().getToken().getToken());
                    accountPreferences.setPrefLastname(response.body().getData().getPersonalData().getLastName());
                    accountPreferences.setPrefBirthday(response.body().getData().getPersonalData().getBirthday());
                    accountPreferences.setPrefBirthPlace(response.body().getData().getPersonalData().getBirthPlace());
                    accountPreferences.setPrefSurname(response.body().getData().getPersonalData().getSurname());
                    accountPreferences.setPrefAvatarUrl(response.body().getData().getAvatar());
                    accountPreferences.setIsLoggedIn();
                    accountPreferences.setPrefAuthorId(response.body().getData().getId());

                    if (getActivity() == null) return;
                    getActivity().finish();
                    Intent intent = new Intent(getContext(), ActivityMain.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<ResponseSignIn> call, Throwable t) {

            }
        });
    }
}