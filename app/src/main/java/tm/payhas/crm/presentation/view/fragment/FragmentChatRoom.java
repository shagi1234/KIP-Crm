package tm.payhas.crm.presentation.view.fragment;

import static android.view.Gravity.BOTTOM;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static androidx.core.content.ContextCompat.checkSelfPermission;
import static tm.payhas.crm.data.remote.api.network.Network.BASE_PHOTO;
import static tm.payhas.crm.data.remote.api.network.Network.BASE_URL;
import static tm.payhas.crm.domain.helpers.Common.addFragment;
import static tm.payhas.crm.domain.helpers.Common.getImageUri;
import static tm.payhas.crm.domain.helpers.Common.getRealPathFromURI;
import static tm.payhas.crm.domain.helpers.Common.normalTime;
import static tm.payhas.crm.domain.helpers.StaticMethods.hideSoftKeyboard;
import static tm.payhas.crm.domain.helpers.StaticMethods.setDataAnswering;
import static tm.payhas.crm.domain.helpers.StaticMethods.setPadding;
import static tm.payhas.crm.domain.helpers.StaticMethods.statusBarHeight;
import static tm.payhas.crm.domain.statics.StaticConstants.CAMERA_REQUEST;
import static tm.payhas.crm.domain.statics.StaticConstants.FILE;
import static tm.payhas.crm.domain.statics.StaticConstants.PHOTO;
import static tm.payhas.crm.domain.statics.StaticConstants.REQUEST_CODE;
import static tm.payhas.crm.domain.statics.StaticConstants.REQUEST_RECORD_AUDIO;
import static tm.payhas.crm.domain.statics.StaticConstants.STRING;
import static tm.payhas.crm.domain.statics.StaticConstants.VOICE;
import static tm.payhas.crm.presentation.view.activity.ActivityMain.webSocket;
import static tm.payhas.crm.presentation.view.adapters.AdapterChatContact.GROUP;
import static tm.payhas.crm.presentation.view.adapters.AdapterChatContact.PRIVATE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.List;

import tm.payhas.crm.R;
import tm.payhas.crm.data.localdb.entity.EntityGroup;
import tm.payhas.crm.data.localdb.entity.EntityUserInfo;
import tm.payhas.crm.databinding.FragmentChatRoomBinding;
import tm.payhas.crm.domain.helpers.CustomItemAnimator;
import tm.payhas.crm.presentation.view.activity.ActivityMain;
import tm.payhas.crm.presentation.view.adapters.AdapterChatContact;
import tm.payhas.crm.presentation.view.adapters.AdapterSingleChat;
import tm.payhas.crm.domain.model.DataAnswer;
import tm.payhas.crm.domain.model.DataFile;
import tm.payhas.crm.data.localdb.entity.EntityMessage;
import tm.payhas.crm.domain.helpers.SelectedMedia;
import tm.payhas.crm.domain.helpers.SoftInputAssist;
import tm.payhas.crm.domain.helpers.WaveformRecorder;
import tm.payhas.crm.domain.interfaces.ChatRoomInterface;
import tm.payhas.crm.data.localdb.preference.AccountPreferences;
import tm.payhas.crm.presentation.viewModel.ViewModelChatRoom;
import tm.payhas.crm.presentation.viewModel.ViewModelChatRoomFactory;

public class FragmentChatRoom extends Fragment implements ChatRoomInterface {
    private FragmentChatRoomBinding b;
    private final String TAG = "chatRoom";
    //Values needed to create
    private int roomId;
    private int userId;
    private int type;
    //Variables needed
    private WaveformRecorder waveformRecorder;
    private ViewModelChatRoom viewModelChatRoom;
    private AdapterSingleChat adapterSingleChat;
    private boolean isRecording;
    private boolean isSet = false;
    private boolean isScrolling = false;


    public static FragmentChatRoom newInstance(int roomId, int userId, int type) {
        FragmentChatRoom fragment = new FragmentChatRoom();
        Bundle args = new Bundle();
        args.putInt("roomId", roomId);
        args.putInt("userId", userId);
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            viewModelChatRoom.closeUseCase(getContext());
        }
        hideSoftKeyboard(getActivity());

    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(() -> setPadding(b.chatContent, 0, statusBarHeight, 0, 0), 100);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        webSocket.setCurrentRoomId(0);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_chat_room, container, false);
        if (getArguments() != null) {
            roomId = getArguments().getInt("roomId");
            userId = getArguments().getInt("userId");
            type = getArguments().getInt("type");
        }
        setupViewModel(roomId);
        setUiHelpers();
        setRoom();
        setRecycler();
        setDateObserver();
        observerRoomId();
//        setToLastPositionObserver();
        initListeners();
        getMessages(roomId);
        setReplyObserver();
        return b.getRoot();
    }


//    private void setToLastPositionObserver() {
//        viewModelChatRoom.getIsAtLastPosition().observe(getViewLifecycleOwner(), isLastPosition -> {
//            if (isLastPosition) {
//                fadeOutButton();
//            } else {
//                fadeInButton();
//            }
//        });
//    }

    private void setDateObserver() {
        viewModelChatRoom.getCurrentMessagesDate().observe(getViewLifecycleOwner(), newDate -> b.chatDate.setText(newDate));
    }

    private void setReplyObserver() {
        viewModelChatRoom.getIsReplyLiveData().observe(getViewLifecycleOwner(), isReply -> {
            if (isReply) {
                b.replyLayout.setVisibility(View.VISIBLE);
                if (viewModelChatRoom.getMessageToReply() != null) {
                    b.replyUserName.setText(viewModelChatRoom.getMessageToReply().getAuthorAnswer().getPersonalDataAuthor().getName());
                    b.replyText.setText(viewModelChatRoom.getMessageToReply().getTextAnswer());
                } else {
                    Log.e(TAG, "setReplyObserver: data Answer is null");
                }

            } else {
                b.replyLayout.setVisibility(GONE);
            }
        });
    }

    private void setupViewModel(int roomId) {
        ViewModelChatRoomFactory factory = new ViewModelChatRoomFactory(getActivity().getApplication(), roomId);
        viewModelChatRoom = new ViewModelProvider(this, factory).get(ViewModelChatRoom.class);
        b.setViewModel(viewModelChatRoom);
        b.setLifecycleOwner(this);
        webSocket.setCurrentRoomId(roomId);
    }

    private void setUiHelpers() {
        setWaveForm();
        hideSoftKeyboard(getActivity());
        if (getActivity() != null) {
            SoftInputAssist softInputAssist = new SoftInputAssist(getActivity());
        }
    }

    private void setWaveForm() {
        waveformRecorder = new WaveformRecorder(getContext());
        waveformRecorder.setOnWaveformUpdateListener(this::onWaveformUpdate);
    }

    private void observerRoomId() {
        viewModelChatRoom.getRoomIdForUser(userId).observe(getViewLifecycleOwner(), roomId -> {
            if (this.roomId == 0 && roomId != 0) {
                getMessages(roomId);
                setRoom();
            }

        });
    }

    private void setUserStatus() {
        if (type == PRIVATE) {
            EntityUserInfo userInfo = viewModelChatRoom.getUserInfo(userId);
            b.username.setText(userInfo.getPersonalData().getName());
            Picasso.get().load(BASE_PHOTO + userInfo.getAvatar()).placeholder(R.color.primary).into(b.contactImage);
            if (userInfo.isActive()) {
                b.userStatus.setText("Online");
            } else {
                b.userStatus.setText(normalTime(userInfo.getLastActivity()));
            }
        } else if (type == GROUP) {
            EntityGroup group = viewModelChatRoom.getGroupInfo(userId);
            b.username.setText(group.getName());
            Picasso.get().load(BASE_PHOTO + group.getAvatar()).placeholder(R.color.primary).into(b.contactImage);
            b.userStatus.setText(String.valueOf(group.getMessageRoom().getRoom().getCount().getParticipants() + " participants"));
        }


    }

    @SuppressLint("SetTextI18n")
    private void setRoom() {
        setUserStatus();
        if (roomId == 0) {
            b.noMessages.setVisibility(VISIBLE);
        } else {
            fadeOutButton(b.noMessages);
        }
        b.recChatScreen.smoothScrollToPosition(1);
    }

    private void getMessages(int roomId) {
        viewModelChatRoom.resetMessageCountForUser(userId);
        if (roomId != 0)
            viewModelChatRoom.getAllMessages(roomId).observe(getViewLifecycleOwner(), entityMessages -> {
                adapterSingleChat.setMessages(entityMessages);
            });
    }

    private void fadeInButton(View view) {
        Animation fadeIn = new AlphaAnimation(0f, 1f);
        fadeIn.setDuration(500); // Set your desired animation duration
        b.chatDate.startAnimation(fadeIn);
        view.setVisibility(VISIBLE);
    }

    private void fadeOutButton(View view) {
        Animation fadeOut = new AlphaAnimation(1f, 0f);
        fadeOut.setDuration(500); // Set your desired animation duration
        b.chatDate.startAnimation(fadeOut);
        view.setVisibility(GONE);
    }


    @SuppressLint("ClickableViewAccessibility")
    private void initListeners() {
        b.recChatScreen.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                isScrolling = newState != RecyclerView.SCROLL_STATE_IDLE;

                if (isScrolling) {
                    fadeOutButton(b.chatDate);
                } else {
                    fadeInButton(b.chatDate);
                }

                if (totalItemCount <= lastVisibleItem + 1) {
                    viewModelChatRoom.loadNextPage();
                }
            }
        });
        b.roomInfo.setOnClickListener(view -> {
            b.roomInfo.setEnabled(false);
            if (type == AdapterChatContact.GROUP) {
                addFragment(ActivityMain.mainFragmentManager, R.id.main_content, FragmentGroupInfo.newInstance(userId));
            } else if (type == PRIVATE) {
                addFragment(ActivityMain.mainFragmentManager, R.id.main_content, FragmentUserInfo.newInstance(userId));
            }
            new Handler().postDelayed(() -> b.roomInfo.setEnabled(true), 200);
        });
        b.cancelReply.setOnClickListener(view -> {
            b.replyLayout.setVisibility(View.GONE);
            viewModelChatRoom.setIsReply(false, null, 0);
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
            webSocket.connectToWebSocket();
            sendMessage();
            AnimationSet animationSet = new AnimationSet(true);

            // TranslateAnimation to move the button from left to right
            Animation translateAnimation = new TranslateAnimation(0, 100, 0, -100);
            translateAnimation.setDuration(250);

            // AlphaAnimation to gradually make the button invisible
            Animation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
            alphaAnimation.setDuration(150);

            animationSet.addAnimation(translateAnimation);
            animationSet.addAnimation(alphaAnimation);

            // Set an AnimationListener to handle actions after animation ends
            animationSet.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    // Animation started
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    // Animation ended
                    b.input.clearAnimation(); // Clear the animation
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    // Animation repeated
                }
            });

            // Start the animation
            b.input.startAnimation(animationSet);
            b.input.setText("");
        });
        b.input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputHandler();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        if (roomId == 0) return;
        b.input.setOnFocusChangeListener((view, bi) ->

        {
            if (!bi) {
                hideSoftKeyboard(getActivity());
            }
        });

        b.recordVoice.setOnTouchListener(new View.OnTouchListener() {
            private boolean isRecording = false;
            private boolean isLongPress = false;
            private Handler handler = new Handler();

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isLongPress = false;
                        handler.postDelayed(longPressRunnable, 200); // Adjust the delay as needed
                        return true;

                    case MotionEvent.ACTION_UP:
                        handler.removeCallbacks(longPressRunnable);
                        if (isRecording) {
                            // Stop recording
                            try {
                                stopRecording();
                                isRecording = false;
                            } catch (IOException ex) {
                                ex.printStackTrace();
                                // Handle the exception (e.g., show an error message)
                            }
                        } else if (!isLongPress) {
                            // Display toast for single click (short press)
                            Toast.makeText(getContext(), "Hold to record", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                }
                return false;
            }

            private Runnable longPressRunnable = new Runnable() {
                @Override
                public void run() {
                    if (checkPermissionsAudio()) {
                        isLongPress = true;
                        // Start recording
                        if (!isRecording) startRecording();
                        isRecording = true;
                    }

                }
            };
        });
    }

    private void inputHandler() {
        if (b.input.getText().length() != 0) {
            b.recordVoice.setVisibility(View.GONE);
            b.sendMessage.setVisibility(View.VISIBLE);
        } else {
            new Handler().postDelayed(() -> {
                b.recordVoice.setVisibility(View.VISIBLE);
                b.sendMessage.setVisibility(View.GONE);
            }, 250);

        }
    }

    private void startRecording() {
        // Check if the recording is already in progress
        if (isRecording) {
            return;
        }
        try {
            isRecording = true;
            setVoiceRecorder(true);
            viewModelChatRoom.startRecording();
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error message)
        }

    }

    private void setVoiceRecorder(boolean isRecording) {
        if (isRecording) {
            b.voiceLay.setVisibility(VISIBLE);
            b.messageLay.setVisibility(GONE);
            waveformRecorder.clearWaveform();
            waveformRecorder.startRecording();
            b.voiceTimer.setBase(SystemClock.elapsedRealtime());
            b.voiceTimer.start();
        } else {
            b.voiceLay.setVisibility(GONE);
            b.messageLay.setVisibility(VISIBLE);
            waveformRecorder.stopRecording();
            b.voiceTimer.stop();
        }

    }

    private void stopRecording() throws IOException {
        if (isRecording) {
            viewModelChatRoom.sendFileImageVoice(VOICE, viewModelChatRoom.stopRecording(), 0, "", null, roomId, userId);
            setVoiceRecorder(false);
            isRecording = false;
        }
    }

    private void setRecycler() {
        if (type == PRIVATE)
            adapterSingleChat = new AdapterSingleChat(getContext(), roomId, PRIVATE, AccountPreferences.newInstance(getContext()).getAuthorId(), viewModelChatRoom.useCaseChatRoom, viewModelChatRoom, b.recChatScreen);
        else
            adapterSingleChat = new AdapterSingleChat(getContext(), roomId, AdapterChatContact.GROUP, AccountPreferences.newInstance(getContext()).getAuthorId(), viewModelChatRoom.useCaseChatRoom, viewModelChatRoom, b.recChatScreen);
        b.recChatScreen.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));
        b.recChatScreen.setAdapter(adapterSingleChat);
    }

    private void sendMessage() {
        viewModelChatRoom.sendMessage(b.input.getText().toString(), STRING, roomId, userId);
    }

    private boolean checkPermissionsAudio() {
        if (getActivity() == null || getContext() == null) return false;
        if (checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);
            return false;
        }
    }

    public void showDialog() {
        final BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_behavoior);

        FrameLayout cameraClicker = dialog.findViewById(R.id.clicker_camera);
        FrameLayout galleryClicker = dialog.findViewById(R.id.clicker_gallery);
        FrameLayout clickerFile = dialog.findViewById(R.id.clicker_file);
        cameraClicker.setOnClickListener(view1 -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
            dialog.dismiss();
        });
        galleryClicker.setOnClickListener(view -> {
            SelectedMedia.getArrayList().clear();
            addFragment(ActivityMain.mainFragmentManager, R.id.main_content, FragmentOpenGallery.newInstance(1, roomId, FragmentOpenGallery.SINGLE, PRIVATE));
            dialog.dismiss();
        });
        clickerFile.setOnClickListener(view -> {
            pickFileFromInternalStorage();
            dialog.dismiss();
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(BOTTOM);
    }

    private void pickFileFromInternalStorage() {
        String[] mimeTypes = {"application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation",
                "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "text/plain", "application/pdf", "application/zip", "video/mp4",
                "video/x-msvideo", "video/x-matroska", "audio/mpeg",
                "audio/wav", "audio/x-wav", "audio/midi", "audio/x-midi"};

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(Intent.createChooser(intent, "Select File"), REQUEST_CODE);
    }

    @Override
    public void newImageImageUrl(DataFile dataFile) throws IOException {
        viewModelChatRoom.sendFileImageVoice(PHOTO, dataFile, 0, "", null, roomId, userId);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: " + "Image Taken");
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                try {
                    viewModelChatRoom.sendFileImageVoice(FILE, viewModelChatRoom.processFile(data.getData()), 0, "", null, roomId, userId);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
            if (data != null) {

                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Uri tempUri = getImageUri(getContext(), photo);

                File finalFile = new File(getRealPathFromURI(tempUri, getActivity()));
                try {
                    viewModelChatRoom.sendFileImageVoice(PHOTO, finalFile, 0, "", null, roomId, userId);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
    }

    @Override
    public void onWaveformUpdate(short[] waveformData) {
        b.voiceIndicator.setWaveformData(waveformData);
        b.voiceIndicator.invalidate();
    }

}