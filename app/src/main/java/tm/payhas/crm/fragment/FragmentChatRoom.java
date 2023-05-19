package tm.payhas.crm.fragment;

import static tm.payhas.crm.helpers.StaticMethods.setBackgroundDrawable;
import static tm.payhas.crm.helpers.StaticMethods.setPadding;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.util.ArrayList;

import tm.payhas.crm.R;
import tm.payhas.crm.adapters.AdapterSingleChat;
import tm.payhas.crm.api.request.RequestNewMessage;
import tm.payhas.crm.databinding.FragmentChatRoomBinding;
import tm.payhas.crm.helpers.SoftInputAssist;
import tm.payhas.crm.model.ModelMessage;
import tm.payhas.crm.webSocket.WebSocket;

public class FragmentChatRoom extends Fragment {
    private FragmentChatRoomBinding b;
    private AdapterSingleChat adapterSingleChat;
    private SoftInputAssist softInputAssist;
    private boolean isMessage = false;
    private boolean isSet = false;
    private WebSocket webSocket;

    public static FragmentChatRoom newInstance() {
        FragmentChatRoom fragment = new FragmentChatRoom();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentChatRoomBinding.inflate(inflater);
        webSocket = new WebSocket(getContext(), getActivity());
        if (getActivity() != null) {
            softInputAssist = new SoftInputAssist(getActivity());
        }
        setBackground();
        setRecycler();
        initListeners();
        setWebSocket();
        return b.getRoot();
    }

    private void setWebSocket() {
        webSocket.createWebSocketClient();
    }

    private void initListeners() {
        b.back.setOnClickListener(view -> {
            b.back.setEnabled(false);
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });
        b.message.setOnClickListener(view -> Toast.makeText(getContext(), "Send Message", Toast.LENGTH_SHORT).show());
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
                    b.message.setVisibility(View.VISIBLE);
                } else {
                    isMessage = false;
                    b.recordVoice.setVisibility(View.VISIBLE);
                    b.message.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        b.recChatScreen.addOnLayoutChangeListener((view, i, i1, i2, i3, i4, i5, i6, i7) -> {
            if (isSet) {
                b.recChatScreen.smoothScrollToPosition(adapterSingleChat.getItemCount() - 1);
            } else {
                b.recChatScreen.scrollToPosition(adapterSingleChat.getItemCount() - 1);
                isSet = true;
            }
        });
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


    @SuppressLint("UseCompatLoadingForDrawables")
    private void setRecycler() {
        adapterSingleChat = new AdapterSingleChat(getContext());
        ArrayList<ModelMessage> testMessagesArray = new ArrayList<>();
        testMessagesArray.add(new ModelMessage("Bu zada in birimji chat Model testi bolayjagay", 1));
        testMessagesArray.add(new ModelMessage("Bu zada in birimji chat Model testi bolayjagay", 2));
        testMessagesArray.add(new ModelMessage(1, 3));
        testMessagesArray.add(new ModelMessage(1, 4));
        testMessagesArray.add(new ModelMessage(1, 5));
        testMessagesArray.add(new ModelMessage(1, 6));
        testMessagesArray.add(new ModelMessage(getResources().getDrawable(R.drawable.ic_logo_crm), 7));
        testMessagesArray.add(new ModelMessage(R.drawable.ic_logo_crm, 8));
        testMessagesArray.add(new ModelMessage("Bu zada in birimji chat Model testi bolayjagay", 1));
        testMessagesArray.add(new ModelMessage("Bu zada in birimji chat Model testi bolayjagay", 2));
        testMessagesArray.add(new ModelMessage(1, 3));
        testMessagesArray.add(new ModelMessage(1, 4));
        testMessagesArray.add(new ModelMessage(1, 5));
        testMessagesArray.add(new ModelMessage(1, 6));
        testMessagesArray.add(new ModelMessage(getResources().getDrawable(R.drawable.ic_logo_crm), 7));
        testMessagesArray.add(new ModelMessage(R.drawable.ic_logo_crm, 8));
        testMessagesArray.add(new ModelMessage("Bu zada in birimji chat Model testi bolayjagay", 1));
        testMessagesArray.add(new ModelMessage("Bu zada in birimji chat Model testi bolayjagay", 2));
        testMessagesArray.add(new ModelMessage(1, 3));
        testMessagesArray.add(new ModelMessage(1, 4));
        testMessagesArray.add(new ModelMessage(1, 5));
        testMessagesArray.add(new ModelMessage(1, 6));
        testMessagesArray.add(new ModelMessage(getResources().getDrawable(R.drawable.ic_logo_crm), 7));
        testMessagesArray.add(new ModelMessage(R.drawable.ic_logo_crm, 8));
        testMessagesArray.add(new ModelMessage("Bu zada in birimji chat Model testi bolayjagay", 9));
        testMessagesArray.add(new ModelMessage("Bu zada in birimji chat Model testi bolayjagay", 10));
        testMessagesArray.add(new ModelMessage("Bu zada in birimji chat Model testi bolayjagay", 9));
        testMessagesArray.add(new ModelMessage("Bu zada in birimji chat Model testi bolayjagay", 10));
        testMessagesArray.add(new ModelMessage("Bu zada in birimji chat Model testi bolayjagay", 9));
        testMessagesArray.add(new ModelMessage("Bu zada in birimji chat Model testi bolayjagay", 10));
        testMessagesArray.add(new ModelMessage("Bu zada in birimji chat Model testi bolayjagay", 9));
        testMessagesArray.add(new ModelMessage("Bu zada in birimji chat Model testi bolayjagay", 10));
        testMessagesArray.add(new ModelMessage("Bu zada in birimji chat Model testi bolayjagay", 9));
        testMessagesArray.add(new ModelMessage("Bu zada in birimji chat Model testi bolayjagay", 10));
        testMessagesArray.add(new ModelMessage("Bu zada in birimji chat Model testi bolayjagay", 9));
        testMessagesArray.add(new ModelMessage("Bu zada in birimji chat Model testi bolayjagay", 10));

        testMessagesArray.add(new ModelMessage(1, 3));
        testMessagesArray.add(new ModelMessage(1, 4));
        testMessagesArray.add(new ModelMessage(1, 5));
        testMessagesArray.add(new ModelMessage(1, 6));
        testMessagesArray.add(new ModelMessage(getResources().getDrawable(R.drawable.ic_logo_crm), 7));
        testMessagesArray.add(new ModelMessage(R.drawable.ic_logo_crm, 8));
        adapterSingleChat.setMessages(testMessagesArray);
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
        menu.setHeaderTitle("kdfdjakjdkjad");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        Toast.makeText(getContext(), "Selected", Toast.LENGTH_SHORT).show();
        return super.onContextItemSelected(item);

    }


    private void sendMessage() {
        RequestNewMessage newMessage = new RequestNewMessage();
    }
}