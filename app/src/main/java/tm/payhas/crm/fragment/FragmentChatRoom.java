package tm.payhas.crm.fragment;

import static tm.payhas.crm.activity.ActivityMain.webSocket;
import static tm.payhas.crm.helpers.StaticMethods.setBackgroundDrawable;
import static tm.payhas.crm.helpers.StaticMethods.setPadding;
import static tm.payhas.crm.statics.StaticConstants.MESSAGE_SENT;
import static tm.payhas.crm.statics.StaticConstants.STRING;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.payhas.crm.R;
import tm.payhas.crm.adapters.AdapterSingleChat;
import tm.payhas.crm.api.data.response.ResponseChatRoom;
import tm.payhas.crm.api.request.RequestNewMessage;
import tm.payhas.crm.dataModels.DataMessageTarget;
import tm.payhas.crm.databinding.FragmentChatRoomBinding;
import tm.payhas.crm.helpers.Common;
import tm.payhas.crm.helpers.SoftInputAssist;
import tm.payhas.crm.interfaces.NewMessage;
import tm.payhas.crm.preference.AccountPreferences;

public class FragmentChatRoom extends Fragment {
    private FragmentChatRoomBinding b;
    private AdapterSingleChat adapterSingleChat;
    private SoftInputAssist softInputAssist;
    private boolean isMessage = false;
    private final String TAG = "chatRoom";
    private boolean isSet = false;
    private int roomId;
    private int userId;
    private AccountPreferences accountPreferences;
    private ArrayList<DataMessageTarget> listMessages = new ArrayList<DataMessageTarget>();

    public static FragmentChatRoom newInstance(int roomId, int userId) {
        FragmentChatRoom fragment = new FragmentChatRoom();
        Bundle args = new Bundle();
        args.putInt("roomId", roomId);
        args.putInt("userId", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountPreferences = new AccountPreferences(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentChatRoomBinding.inflate(inflater);
        if (getArguments() != null) {
            roomId = getArguments().getInt("roomId");
            userId = getArguments().getInt("userId");
        }
        if (getActivity() != null) {
            softInputAssist = new SoftInputAssist(getActivity());
        }

        setBackground();
        setRecycler();
        initListeners();
        getMessages();

        Log.e(TAG, "onCreateView: roomId" + roomId);
        Log.e(TAG, "onCreateView: userId" + userId);
        return b.getRoot();
    }

    private void getMessages() {
        Call<ResponseChatRoom> messagesRooms = Common.getApi().getMessageRoom(accountPreferences.getToken(), roomId, 1, 10);
        messagesRooms.enqueue(new Callback<ResponseChatRoom>() {
            @Override
            public void onResponse(Call<ResponseChatRoom> call, Response<ResponseChatRoom> response) {
                if (response.isSuccessful()) {
                    listMessages = response.body().getData().get(2).getMessages();
//                    adapterSingleChat.setMessages(listMessages);
                    Log.e(TAG, "onResponse: "+listMessages.size() );

                }
            }

            @Override
            public void onFailure(Call<ResponseChatRoom> call, Throwable t) {

            }
        });
    }

    private void initListeners() {
        b.back.setOnClickListener(view -> {
            b.back.setEnabled(false);
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });
        b.sendMessage.setOnClickListener(view -> sendMessage());

        b.recordVoice.setOnClickListener(view -> Toast.makeText(getContext(), "Send Voice", Toast.LENGTH_SHORT).show());
        b.input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (b.input.getText().length() != 0) {
                    isMessage = true;
                    b.recordVoice.setVisibility(View.GONE);
                    b.sendMessage.setVisibility(View.VISIBLE);
                } else {
                    isMessage = false;
                    b.recordVoice.setVisibility(View.VISIBLE);
                    b.sendMessage.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

//        b.recChatScreen.addOnLayoutChangeListener((view, i, i1, i2, i3, i4, i5, i6, i7) -> {
//            if (isSet) {
//                b.recChatScreen.smoothScrollToPosition(adapterSingleChat.getItemCount() - 1);
//            } else {
//                b.recChatScreen.scrollToPosition(adapterSingleChat.getItemCount() - 1);
//                isSet = true;
//            }
//        });
    }


    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(() -> setPadding(b.chatContent,
                0,
                50,
                0,
                0), 100);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void setBackground() {
        setBackgroundDrawable(getContext(), b.input, R.color.edit_text_background, 0, 14, false, 0);
    }


    private void setRecycler() {
        adapterSingleChat = new AdapterSingleChat(getContext());
        b.recChatScreen.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.recChatScreen.setAdapter(adapterSingleChat);
        adapterSingleChat.setActivity(getActivity());
        registerForContextMenu(b.recChatScreen);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        return super.onContextItemSelected(item);

    }


    private void sendMessage() {
        DataMessageTarget newMessageData = new DataMessageTarget();
        newMessageData.setRoomId(roomId);
        newMessageData.setType(STRING);
        newMessageData.setText(b.input.getText().toString());
        newMessageData.setFriendId(userId);
        newMessageData.setStatus(MESSAGE_SENT);
        newMessageData.setLocalId(UUID.randomUUID().toString());
        RequestNewMessage newMessage = new RequestNewMessage();
        newMessage.setEvent("createMessage");
        newMessage.setData(newMessageData);
        String s = new Gson().toJson(newMessage);

        webSocket.sendMessage(s);

        if (adapterSingleChat != null) {
            ((NewMessage) adapterSingleChat).onNewMessage(newMessageData);
        }
    }
}