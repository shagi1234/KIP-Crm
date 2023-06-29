package tm.payhas.crm.fragment;

import static android.view.Gravity.BOTTOM;
import static tm.payhas.crm.activity.ActivityMain.mainFragmentManager;
import static tm.payhas.crm.activity.ActivityMain.webSocket;
import static tm.payhas.crm.adapters.AdapterChatContact.GROUP;
import static tm.payhas.crm.adapters.AdapterChatContact.PRIVATE;
import static tm.payhas.crm.api.network.Network.BASE_URL;
import static tm.payhas.crm.helpers.Common.addFragment;
import static tm.payhas.crm.helpers.Common.getImageUri;
import static tm.payhas.crm.helpers.Common.getRealPathFromURI;
import static tm.payhas.crm.helpers.Common.normalTime;
import static tm.payhas.crm.helpers.StaticMethods.getPath;
import static tm.payhas.crm.helpers.StaticMethods.hideSoftKeyboard;
import static tm.payhas.crm.helpers.StaticMethods.setBackgroundDrawable;
import static tm.payhas.crm.helpers.StaticMethods.setPadding;
import static tm.payhas.crm.helpers.StaticMethods.statusBarHeight;
import static tm.payhas.crm.statics.StaticConstants.MESSAGE_SENT;
import static tm.payhas.crm.statics.StaticConstants.PHOTO;
import static tm.payhas.crm.statics.StaticConstants.RECEIVED_MESSAGE;
import static tm.payhas.crm.statics.StaticConstants.SENT_MESSAGE;
import static tm.payhas.crm.statics.StaticConstants.STRING;
import static tm.payhas.crm.statics.StaticConstants.USER_STATUS;
import static tm.payhas.crm.statics.StaticConstants.USER_STATUS_CHANNEL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.payhas.crm.R;
import tm.payhas.crm.activity.ActivityMain;
import tm.payhas.crm.adapters.AdapterSingleChat;
import tm.payhas.crm.api.data.dto.DtoUserInfo;
import tm.payhas.crm.api.data.dto.subClassesUserInfo.DtoPersonalData;
import tm.payhas.crm.api.request.RequestNewMessage;
import tm.payhas.crm.api.response.ResponseRoomMessages;
import tm.payhas.crm.api.response.ResponseSingleFile;
import tm.payhas.crm.dataModels.DataAttachment;
import tm.payhas.crm.dataModels.DataFile;
import tm.payhas.crm.dataModels.DataMessageTarget;
import tm.payhas.crm.dataModels.DataUserStatus;
import tm.payhas.crm.databinding.FragmentChatRoomBinding;
import tm.payhas.crm.helpers.Common;
import tm.payhas.crm.helpers.SelectedMedia;
import tm.payhas.crm.helpers.SoftInputAssist;
import tm.payhas.crm.interfaces.ChatRoomInterface;
import tm.payhas.crm.interfaces.NewMessage;
import tm.payhas.crm.preference.AccountPreferences;
import tm.payhas.crm.webSocket.EmmitUserStatus;

public class FragmentChatRoom extends Fragment implements ChatRoomInterface {
    private FragmentChatRoomBinding b;
    private static final int REQUEST_CODE = 132;
    public static final int CAMERA_REQUEST = 1122;
    private SoftInputAssist softInputAssist;
    private boolean isMessage = false;
    private final String TAG = "chatRoom";
    private boolean isSet = false;
    private int roomId;
    private int userId;
    private String userName;
    private String avatarUrl;
    private String lastActivity;
    private boolean isActive;
    private AccountPreferences accountPreferences;
    private AdapterSingleChat adapterSingleChat;
    private String event = "createMessage";
    private boolean toReply = false;
    private int replyMessageId = 0;
    private DataMessageTarget messageToReply;
    private int type;
    private int memberCount;


    public static FragmentChatRoom newInstance(int roomId, int userId, String username, String avatarUrl, String lastActivity, boolean isActive, int type, int memberCount) {
        FragmentChatRoom fragment = new FragmentChatRoom();
        Bundle args = new Bundle();
        args.putInt("roomId", roomId);
        args.putInt("userId", userId);
        args.putInt("type", type);
        args.putInt("memberCount", memberCount);
        args.putString("username", username);
        args.putString("avatarUrl", avatarUrl);
        args.putString("lastActivity", lastActivity);
        args.putBoolean("isActive", isActive);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountPreferences = new AccountPreferences(getContext());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        hideSoftKeyboard(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentChatRoomBinding.inflate(inflater);
        hideSoftKeyboard(getActivity());
        if (getArguments() != null) {
            roomId = getArguments().getInt("roomId");
            userId = getArguments().getInt("userId");
            type = getArguments().getInt("type");
            userName = getArguments().getString("username");
            avatarUrl = getArguments().getString("avatarUrl");
            lastActivity = getArguments().getString("lastActivity");
            isActive = getArguments().getBoolean("isActive");
            memberCount = getArguments().getInt("memberCount");
        }
        Log.e(TAG, "onCreateView: " + type);
        if (getActivity() != null) {
            softInputAssist = new SoftInputAssist(getActivity());
        }
        setRoom();
        setRecycler();
        setBackground();
        initListeners();
        setAuthorId();
        setUserStatus();
        setUserOnline();
        if (roomId != 0) {
            getMessages();
        }
        Log.e(TAG, "onCreateView: roomId" + roomId);
        Log.e(TAG, "onCreateView: userId" + userId);
        return b.getRoot();
    }

    private void setUserStatus() {
        if (type == PRIVATE) {
            if (isActive)
                b.userStatus.setText("Online");
            else
                b.userStatus.setText(normalTime(lastActivity));
        }

    }

    @SuppressLint("SetTextI18n")
    private void setRoom() {
        if (type == PRIVATE) {
            if (isSet) {
                b.recChatScreen.smoothScrollToPosition(1);
            } else {
                b.recChatScreen.scrollToPosition(1);
                isSet = true;
            }

            b.username.setText(userName);
            Picasso.get().load(BASE_URL + "/" + avatarUrl).placeholder(R.color.primary).into(b.contactImage);
        } else {
            if (isSet) {
                b.recChatScreen.smoothScrollToPosition(1);
            } else {
                b.recChatScreen.scrollToPosition(1);
                isSet = true;
            }
            b.username.setText(userName);
            Picasso.get().load(BASE_URL + "/" + avatarUrl).placeholder(R.color.primary).into(b.contactImage);
            b.userStatus.setText(String.valueOf(memberCount) + "participants");
        }

    }

    private void setAuthorId() {
        adapterSingleChat.setAuthorId(accountPreferences.getAuthorId());
    }

    private void getMessages() {
        Call<ResponseRoomMessages> messagesRooms = Common.getApi().getMessageRoom(accountPreferences.getToken(), roomId, 1, 50);
        messagesRooms.enqueue(new Callback<ResponseRoomMessages>() {
            @Override
            public void onResponse(Call<ResponseRoomMessages> call, Response<ResponseRoomMessages> response) {
                if (response.isSuccessful()) {
                    adapterSingleChat.setMessages(response.body().getData());
                    Log.e(TAG, "onResponse: " + response.body().getData().size());
                }
            }

            @Override
            public void onFailure(Call<ResponseRoomMessages> call, Throwable t) {

            }
        });
    }

    private void initListeners() {
        b.roomInfo.setOnClickListener(view -> {
            b.roomInfo.setEnabled(false);
            if (type == GROUP) {
                addFragment(mainFragmentManager, R.id.main_content, FragmentGroupInfo.newInstance(userId, memberCount, userName, avatarUrl));
            } else if (type == PRIVATE) {
                addFragment(mainFragmentManager, R.id.main_content, FragmentUserInfo.newInstance(userId));
            }
            new Handler().postDelayed(() -> b.roomInfo.setEnabled(true), 200);
        });
        b.cancelReply.setOnClickListener(view -> {
            b.replyLayout.setVisibility(View.GONE);
            toReply = false;
            replyMessageId = 0;
        });
        b.attach.setOnClickListener(view -> {
            b.attach.setEnabled(false);
            showDialog();
            new Handler().postDelayed(() -> b.attach.setEnabled(true), 200);
        });
        b.back.setOnClickListener(view -> {
            b.back.setEnabled(false);
            hideSoftKeyboard(getActivity());
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });
        b.sendMessage.setOnClickListener(view -> {
            if (toReply) {
                sendMessage(replyMessageId);
                b.replyLayout.setVisibility(View.GONE);
                replyMessageId = 0;
                toReply = false;
            } else
                sendMessage(0);
            b.input.setText("");
        });
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
        if (roomId == 0) return;
        b.input.setOnFocusChangeListener((view, bi) -> {
            if (!bi) {
                hideSoftKeyboard(getActivity());
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(() -> setPadding(b.chatContent,
                0,
                statusBarHeight,
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
        if (type == PRIVATE)
            adapterSingleChat = new AdapterSingleChat(getContext(), roomId, PRIVATE);
        else
            adapterSingleChat = new AdapterSingleChat(getContext(), roomId, GROUP);
        b.recChatScreen.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));
        b.recChatScreen.setAdapter(adapterSingleChat);
        registerForContextMenu(b.recChatScreen);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.message_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        return super.onContextItemSelected(item);

    }


    private void sendMessage(int replyId) {
        if (roomId == 0) {
            DtoPersonalData personalData = new DtoPersonalData();
            personalData.setName(accountPreferences.getUserName());
            personalData.setSurname(accountPreferences.getPrefSurname());
            personalData.setBirthday(accountPreferences.getPrefBirthday());
            personalData.setLastName(accountPreferences.getPrefLastname());
            DtoUserInfo userInfo = new DtoUserInfo();
            userInfo.setPersonalData(personalData);
            DataMessageTarget newMessageData = new DataMessageTarget();
            newMessageData.setRoomId(roomId);
            newMessageData.setStatus(MESSAGE_SENT);
            newMessageData.setAuthor(userInfo);
            newMessageData.setType(STRING);
            newMessageData.setAuthorId(accountPreferences.getAuthorId());
            newMessageData.setText(b.input.getText().toString());
            newMessageData.setFriendId(userId);
            newMessageData.setStatus(MESSAGE_SENT);
            newMessageData.setAnswerId(replyId);
            newMessageData.setLocalId(UUID.randomUUID().toString());
            RequestNewMessage newMessage = new RequestNewMessage();
            newMessage.setEvent(event);
            newMessage.setData(newMessageData);
            String s = new Gson().toJson(newMessage);
            webSocket.sendMessage(s);
        } else {
            if (replyId == 0) {
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                String time = df.format(c);
                DtoPersonalData personalData = new DtoPersonalData();
                personalData.setName(accountPreferences.getUserName());
                personalData.setSurname(accountPreferences.getPrefSurname());
                personalData.setBirthday(accountPreferences.getPrefBirthday());
                personalData.setLastName(accountPreferences.getPrefLastname());
                DtoUserInfo userInfo = new DtoUserInfo();
                userInfo.setPersonalData(personalData);
                DataMessageTarget newMessageData = new DataMessageTarget();
                newMessageData.setRoomId(roomId);
                newMessageData.setAuthor(userInfo);
                newMessageData.setType(STRING);
                newMessageData.setCreatedAt(time);
                newMessageData.setAuthorId(accountPreferences.getAuthorId());
                newMessageData.setText(b.input.getText().toString());
                newMessageData.setFriendId(userId);
                newMessageData.setStatus(MESSAGE_SENT);
                newMessageData.setLocalId(UUID.randomUUID().toString());
                RequestNewMessage newMessage = new RequestNewMessage();
                newMessage.setEvent(event);
                newMessage.setData(newMessageData);
                String s = new Gson().toJson(newMessage);
                webSocket.sendMessage(s);
                onNewMessage(SENT_MESSAGE, newMessageData);
            } else {
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                String time = df.format(c);
                DtoPersonalData personalData = new DtoPersonalData();
                personalData.setName(accountPreferences.getUserName());
                personalData.setSurname(accountPreferences.getPrefSurname());
                personalData.setBirthday(accountPreferences.getPrefBirthday());
                personalData.setLastName(accountPreferences.getPrefLastname());
                DtoUserInfo userInfo = new DtoUserInfo();
                userInfo.setPersonalData(personalData);
                DataMessageTarget newMessageData = new DataMessageTarget();
                newMessageData.setAuthor(userInfo);
                newMessageData.setCreatedAt(time);
                newMessageData.setRoomId(roomId);
                newMessageData.setType(STRING);
                newMessageData.setAuthorId(accountPreferences.getAuthorId());
                newMessageData.setText(b.input.getText().toString());
                newMessageData.setFriendId(userId);
                newMessageData.setStatus(MESSAGE_SENT);
                newMessageData.setAnswerId(replyId);
                newMessageData.setAnswering(messageToReply);
                String uuid = UUID.randomUUID().toString();
                Log.e(TAG, "sendMessage: " + uuid);
                newMessageData.setLocalId(uuid);
                RequestNewMessage newMessage = new RequestNewMessage();
                newMessage.setEvent(event);
                newMessage.setData(newMessageData);
                String s = new Gson().toJson(newMessage);
                webSocket.sendMessage(s);
                onNewMessage(SENT_MESSAGE, newMessageData);
            }

        }
    }

    private void onNewMessage(int type, DataMessageTarget newMessage) {
        if (adapterSingleChat != null) {
            ((NewMessage) adapterSingleChat).onNewMessage(type, newMessage);
            adapterSingleChat.notifyDataSetChanged();
        }
    }

    @Override
    public void onMessageStatus(DataMessageTarget messageTarget) {
        if (adapterSingleChat != null) {
            ((NewMessage) adapterSingleChat).onMessageStatus(messageTarget);
            adapterSingleChat.notifyDataSetChanged();
        }
    }

    private void sendImage(DataFile dataFile) {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        String time = df.format(c);
        DtoPersonalData personalData = new DtoPersonalData();
        personalData.setName(accountPreferences.getUserName());
        personalData.setSurname(accountPreferences.getPrefSurname());
        personalData.setBirthday(accountPreferences.getPrefBirthday());
        personalData.setLastName(accountPreferences.getPrefLastname());
        DtoUserInfo userInfo = new DtoUserInfo();
        userInfo.setPersonalData(personalData);
        DataMessageTarget newMessageData = new DataMessageTarget();
        newMessageData.setRoomId(roomId);
        newMessageData.setAuthor(userInfo);
        newMessageData.setType(PHOTO);
        newMessageData.setCreatedAt(time);
        newMessageData.setAuthorId(accountPreferences.getAuthorId());
        newMessageData.setFriendId(userId);
        newMessageData.setStatus(MESSAGE_SENT);
        newMessageData.setLocalId(UUID.randomUUID().toString());
        RequestNewMessage newMessage = new RequestNewMessage();
        DataAttachment dataAttachment = new DataAttachment();
        dataAttachment.setFileUrl(dataFile.getUrl());
        dataAttachment.setSize(dataFile.getSize());
        newMessageData.setAttachment(dataAttachment);
        newMessage.setEvent(event);
        newMessage.setData(newMessageData);
        String s = new Gson().toJson(newMessage);
        webSocket.sendMessage(s);
        onNewMessage(SENT_MESSAGE, newMessageData);
    }


    @Override
    public void userStatus(boolean isActive) {
        if (isActive)
            b.userStatus.setText("Online");
        else
            b.userStatus.setText("Offline");
    }

    @Override
    public void newMessage(DataMessageTarget messageTarget) {
        b.recChatScreen.smoothScrollToPosition(1);
        Log.e(TAG, "newMessage: " + "status received");
        onNewMessage(RECEIVED_MESSAGE, messageTarget);
    }

    @Override
    public void newImageImageUrl(DataFile dataFile) {
        sendImage(dataFile);
    }

    @Override
    public void deleteMessage(DataMessageTarget messageTarget) {
        if (adapterSingleChat != null) {
            ((NewMessage) adapterSingleChat).deleteMessage(messageTarget);
            adapterSingleChat.notifyDataSetChanged();
        }
    }


    @Override
    public void newReplyMessage(DataMessageTarget messageTarget) {
        messageToReply = messageTarget;
        b.replyLayout.setVisibility(View.VISIBLE);
        toReply = true;
        replyMessageId = messageTarget.getId();
        b.replyUserName.setText(messageTarget.getAuthor().getPersonalData().getName());
        b.replyText.setText(messageTarget.getText());
    }

    @Override
    public void onMessageReceived(DataMessageTarget messageTarget) {
        if (adapterSingleChat != null) {
            ((NewMessage) adapterSingleChat).onReceiveYourMessage(messageTarget);
            adapterSingleChat.notifyDataSetChanged();
        }
    }


    private void setUserOnline() {
        EmmitUserStatus emitUserStatus = new EmmitUserStatus();
        emitUserStatus.setChannel(USER_STATUS_CHANNEL);
        emitUserStatus.setEvent(USER_STATUS);
        DataUserStatus userStatus = new DataUserStatus();
        userStatus.setActive(true);
        userStatus.setUserId(accountPreferences.getAuthorId());
        webSocket.setUserStatus(emitUserStatus);
    }


    private void uploadFile(String image) {
        MultipartBody.Part fileToUpload = null;
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"),
                        new File(image)
                );
        try {
            fileToUpload = MultipartBody.Part.createFormData("fileUrl", URLEncoder.encode(image, "utf-8"), requestFile);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        RequestBody fileUrl = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(fileToUpload));

        Call<ResponseSingleFile> upload = Common.getApi().uploadFile(fileToUpload);
        upload.enqueue(new Callback<ResponseSingleFile>() {
            @Override
            public void onResponse(Call<ResponseSingleFile> call, Response<ResponseSingleFile> response) {
                if (response.isSuccessful()) {
                    b.linearProgressBar.setVisibility(View.GONE);
                    Fragment chatRoom = ActivityMain.mainFragmentManager.findFragmentByTag(FragmentChatRoom.class.getSimpleName());

                    if (chatRoom instanceof ChatRoomInterface) {
                        ((ChatRoomInterface) chatRoom).newImageImageUrl(response.body().getData());
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseSingleFile> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: " + "Image Taken");
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri fileUri = data.getData();
                if (fileUri != null) {
                    String filePath = getPath(getContext(), fileUri);
                    if (filePath != null) {
                        File file = new File(filePath);
                        uploadFile(file);
                    }
                }
            }
        } else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
            if (data != null) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Uri tempUri = getImageUri(getContext(), photo);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                File finalFile = new File(getRealPathFromURI(tempUri, getActivity()));
                uploadFile(finalFile);
            }

    }

    private void uploadFile(File file) {
        MultipartBody.Part fileToUpload = null;
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"),
                        new File(file.getPath())
                );
        try {
            fileToUpload = MultipartBody.Part.createFormData("fileUrl", URLEncoder.encode(file.getPath(), "utf-8"), requestFile);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        RequestBody fileUrl = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(fileToUpload));

        Call<ResponseSingleFile> upload = Common.getApi().uploadFile(fileToUpload);
        upload.enqueue(new Callback<ResponseSingleFile>() {
            @Override
            public void onResponse(@NonNull Call<ResponseSingleFile> call, @NonNull Response<ResponseSingleFile> response) {
                if (response.isSuccessful()) {
                    b.linearProgressBar.setVisibility(View.GONE);
                    DataFile fileUrl = new DataFile();
                    fileUrl.setSize(response.body().getData().getSize());
                    fileUrl.setUrl(response.body().getData().getUrl());
                    sendImage(fileUrl);
                }
            }

            @Override
            public void onFailure(Call<ResponseSingleFile> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                b.linearProgressBar.setVisibility(View.GONE);
            }
        });
    }


    public void showDialog() {
        final BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_behavoior);

        FrameLayout cameraClicker = dialog.findViewById(R.id.clicker_camera);
        FrameLayout galleryClicker = dialog.findViewById(R.id.clicker_gallery);
        FrameLayout clickerFile = dialog.findViewById(R.id.clicker_file);
        cameraClicker.setOnClickListener(view1 -> {
            b.linearProgressBar.setVisibility(View.VISIBLE);
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
            dialog.dismiss();
        });
        galleryClicker.setOnClickListener(view -> {
            SelectedMedia.getArrayList().clear();
            addFragment(mainFragmentManager, R.id.main_content, FragmentOpenGallery.newInstance(1, roomId, FragmentOpenGallery.SINGLE, PRIVATE));
            dialog.dismiss();
        });
        clickerFile.setOnClickListener(view -> dialog.dismiss());
        pickFileFromInternalStorage();

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(BOTTOM);
    }

    private void pickFileFromInternalStorage() {
        String[] mimeTypes = {"application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                "text/plain", "application/pdf", "application/zip"};

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(Intent.createChooser(intent, "Select File"), REQUEST_CODE);
    }
}