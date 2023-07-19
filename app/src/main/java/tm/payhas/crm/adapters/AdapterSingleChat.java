package tm.payhas.crm.adapters;

import static android.content.Context.CLIPBOARD_SERVICE;
import static tm.payhas.crm.activity.ActivityMain.mainFragmentManager;
import static tm.payhas.crm.activity.ActivityMain.webSocket;
import static tm.payhas.crm.adapters.AdapterChatContact.GROUP;
import static tm.payhas.crm.api.network.Network.BASE_PHOTO;
import static tm.payhas.crm.api.network.Network.BASE_URL;
import static tm.payhas.crm.helpers.Common.normalTime;
import static tm.payhas.crm.statics.StaticConstants.DATE;
import static tm.payhas.crm.statics.StaticConstants.FILE;
import static tm.payhas.crm.statics.StaticConstants.MEDIA_PLAYER;
import static tm.payhas.crm.statics.StaticConstants.MESSAGE_DELIVERED;
import static tm.payhas.crm.statics.StaticConstants.MESSAGE_READ;
import static tm.payhas.crm.statics.StaticConstants.MESSAGE_RECEIVE;
import static tm.payhas.crm.statics.StaticConstants.MESSAGE_SENT;
import static tm.payhas.crm.statics.StaticConstants.MESSAGE_UN_SEND;
import static tm.payhas.crm.statics.StaticConstants.PHOTO;
import static tm.payhas.crm.statics.StaticConstants.RECEIVED_MESSAGE;
import static tm.payhas.crm.statics.StaticConstants.STRING;
import static tm.payhas.crm.statics.StaticConstants.VOICE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.payhas.crm.R;
import tm.payhas.crm.api.request.RequestReceivedMessage;
import tm.payhas.crm.api.response.ResponseOneMessage;
import tm.payhas.crm.dataModels.DataMessageReceived;
import tm.payhas.crm.dataModels.DataMessageTarget;
import tm.payhas.crm.fragment.FragmentChatRoom;
import tm.payhas.crm.helpers.ChatMenu;
import tm.payhas.crm.helpers.Common;
import tm.payhas.crm.helpers.MusicProgressBar;
import tm.payhas.crm.interfaces.ChatRoomInterface;
import tm.payhas.crm.interfaces.NewMessage;
import tm.payhas.crm.preference.AccountPreferences;

public class AdapterSingleChat extends RecyclerView.Adapter implements NewMessage {
    private ArrayList<DataMessageTarget> messages = new ArrayList<>();
    private Context context;
    private Activity activity;
    private Integer authorId = 0;
    private Integer roomId = 0;
    private Integer currentMessageId = 0;
    private int selectedMenu;
    private String TAG = "AdapterSingleChat";
    private AccountPreferences accountPreferences;
    private int type;


    public AdapterSingleChat(Context context, Integer roomId, int type) {
        this.roomId = roomId;
        this.context = context;
        this.type = type;
        accountPreferences = new AccountPreferences(context);
    }


    @SuppressLint("NotifyDataSetChanged")
    public void setAuthorId(int authorId) {
        this.authorId = authorId;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setMessages(ArrayList<DataMessageTarget> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    public void setMessageStatus(Integer messageId) {
        this.currentMessageId = messageId;
    }

    public void addMessages(DataMessageTarget message) {
        messages.add(message);
        notifyDataSetChanged();
    }

    public static final int LAYOUT_SEND_MESSAGE = 1;
    public static final int LAYOUT_RECEIVED_MESSAGE = 2;
    public static final int LAYOUT_SEND_VOICE_MESSAGE = 3;
    public static final int LAYOUT_RECEIVED_VOICE_MESSAGE = 4;
    public static final int LAYOUT_SEND_FILE = 5;
    public static final int LAYOUT_RECEIVED_FILE = 6;
    public static final int LAYOUT_SEND_IMAGE = 7;
    public static final int LAYOUT_RECEIVED_IMAGE = 8;
    public static final int LAYOUT_SEND_REPLY = 9;
    public static final int LAYOUT_RECEIVED_REPLY = 10;
    public static final int LAYOUT_DATE = 11;

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public int getItemViewType(int position) {
        DataMessageTarget oneMessage = messages.get(position);
        switch (oneMessage.getType()) {
            case STRING:
                if (oneMessage.getAuthorId() == authorId)
                    if (oneMessage.getAnswerId() == null)
                        return LAYOUT_SEND_MESSAGE;
                    else
                        return LAYOUT_SEND_REPLY;
                else if (oneMessage.getAnswerId() == null)
                    return LAYOUT_RECEIVED_MESSAGE;
                else
                    return LAYOUT_RECEIVED_REPLY;
            case VOICE:
                if (oneMessage.getAuthorId() == authorId)
                    return LAYOUT_SEND_VOICE_MESSAGE;
                else
                    return LAYOUT_RECEIVED_VOICE_MESSAGE;
            case PHOTO:
                if (oneMessage.getAuthorId() == authorId)
                    return LAYOUT_SEND_IMAGE;
                else
                    return LAYOUT_RECEIVED_IMAGE;
            case FILE:
                if (oneMessage.getAuthorId() == authorId)
                    return LAYOUT_SEND_FILE;
                else
                    return LAYOUT_RECEIVED_FILE;
            case DATE:
                return LAYOUT_DATE;
            default:
                return LAYOUT_SEND_MESSAGE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case LAYOUT_SEND_MESSAGE:
                View layout1 = LayoutInflater.from(context).inflate(R.layout.item_sent_message, parent, false);
                return new SenderMessageViewHolder(layout1);
            case LAYOUT_RECEIVED_MESSAGE:
                View layout2 = LayoutInflater.from(context).inflate(R.layout.item_received_message, parent, false);
                return new ReceivedMessageViewHolder(layout2);
            case LAYOUT_SEND_VOICE_MESSAGE:
                View layout3 = LayoutInflater.from(context).inflate(R.layout.item_voice_message_sent, parent, false);
                return new SenderVoiceViewHolder(layout3);
            case LAYOUT_RECEIVED_VOICE_MESSAGE:
                View layout4 = LayoutInflater.from(context).inflate(R.layout.item_voice_message_received, parent, false);
                return new ReceivedVoiceViewHolder(layout4);
            case LAYOUT_SEND_FILE:
                View layout5 = LayoutInflater.from(context).inflate(R.layout.item_file_sent, parent, false);
                return new SenderFileViewHolder(layout5);
            case LAYOUT_RECEIVED_FILE:
                View layout6 = LayoutInflater.from(context).inflate(R.layout.item_file_received, parent, false);
                return new ReceivedFileViewHolder(layout6);
            case LAYOUT_SEND_IMAGE:
                View layout7 = LayoutInflater.from(context).inflate(R.layout.item_sent_image, parent, false);
                return new SendImageViewHolder(layout7);
            case LAYOUT_RECEIVED_IMAGE:
                View layout8 = LayoutInflater.from(context).inflate(R.layout.item_received_image, parent, false);
                return new ReceivedImageViewHolder(layout8);
            case LAYOUT_SEND_REPLY:
                View layout9 = LayoutInflater.from(context).inflate(R.layout.item_reply_sent, parent, false);
                return new SenderReplyViewHolder(layout9);
            case LAYOUT_RECEIVED_REPLY:
                View layout10 = LayoutInflater.from(context).inflate(R.layout.item_reply_recieved, parent, false);
                return new ReceivedReplyViewHolder(layout10);
            case LAYOUT_DATE:
                View layout11 = LayoutInflater.from(context).inflate(R.layout.item_date, parent, false);
                return new DateViewHolder(layout11);
            default:
                return null;

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DataMessageTarget oneMessage = messages.get(position);
        switch (oneMessage.getType()) {
            case STRING:
                if (oneMessage.getAuthorId() == authorId) {
                    if (oneMessage.getAnswerId() == null) {
                        ((SenderMessageViewHolder) holder).bind(oneMessage);
                    } else {
                        ((SenderReplyViewHolder) holder).bind(oneMessage);
                    }
                } else {
                    if (oneMessage.getAnswerId() == null) {
                        ((ReceivedMessageViewHolder) holder).bind(oneMessage, type);
                    } else {
                        ((ReceivedReplyViewHolder) holder).bind(oneMessage, type);
                    }
                }
                break;
            case VOICE:
                if (oneMessage.getAuthorId() == authorId) {
                    ((SenderVoiceViewHolder) holder).bind(oneMessage);
                } else
                    ((ReceivedVoiceViewHolder) holder).bind(oneMessage, type);
                break;
            case PHOTO:
                if (oneMessage.getAuthorId() == authorId)
                    ((SendImageViewHolder) holder).bind(oneMessage);
                else
                    ((ReceivedImageViewHolder) holder).bind(oneMessage, type);
                break;
            case FILE:
                if (oneMessage.getAuthorId() == authorId)
                    ((SenderFileViewHolder) holder).bind(oneMessage);
                else
                    ((ReceivedFileViewHolder) holder).bind(oneMessage, type);
                break;
            case DATE:
                ((DateViewHolder) holder).bind(oneMessage);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public void onMessageStatus(DataMessageTarget messageTarget) {
        for (int i = 0; i < messages.size(); i++) {
            if (messageTarget.getLocalId().equals(messages.get(i).getLocalId())) {
                messages.get(i).setStatus(messageTarget.getStatus());
                messages.get(i).setId(messageTarget.getId());
                notifyDataSetChanged();
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onNewMessage(int type, DataMessageTarget dataMessageTarget) {
        if (type == RECEIVED_MESSAGE) {
            int receivedMessageId = 0;
            receivedMessageId = dataMessageTarget.getId();
            DataMessageReceived received = new DataMessageReceived();
            received.setMessageId(receivedMessageId);
            RequestReceivedMessage receivedMessage = new RequestReceivedMessage();
            receivedMessage.setData(received);
            receivedMessage.setEvent(MESSAGE_RECEIVE);
            String s = new Gson().toJson(receivedMessage);
            webSocket.sendMessage(s);
        }
        if (Objects.equals(dataMessageTarget.getRoomId(), roomId)) {
            messages.add(0, dataMessageTarget);
            notifyDataSetChanged();
        }

    }

    @Override
    public void deleteMessage(DataMessageTarget dataMessageTarget) {
        int toDelete = 0;
        for (int i = 0; i < messages.size(); i++) {
            if (Objects.equals(dataMessageTarget.getLocalId(), messages.get(i).getLocalId())) {
                toDelete = i;
                messages.remove(toDelete);

            }
        }

        notifyItemRemoved(toDelete);
    }

    @Override
    public void onMenuSelected(View view, Integer i, Integer messageId, DataMessageTarget messageTarget) {
        switch (i) {
            case 1:
                ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", messageTarget.getText());
                clipboard.setPrimaryClip(clip);
                break;
            case 2:
                Call<ResponseOneMessage> call = Common.getApi().removeMessage(accountPreferences.getToken(), messageTarget.getId());
                call.enqueue(new Callback<ResponseOneMessage>() {
                    @Override
                    public void onResponse(Call<ResponseOneMessage> call, Response<ResponseOneMessage> response) {

                    }

                    @Override
                    public void onFailure(Call<ResponseOneMessage> call, Throwable t) {
                        Log.e(TAG, "onFailure: " + t.getMessage());
                    }
                });
                break;
            case 3:
                Fragment chatRoom = mainFragmentManager.findFragmentByTag(FragmentChatRoom.class.getSimpleName());
                if (chatRoom instanceof ChatRoomInterface) {
                    ((ChatRoomInterface) chatRoom).newReplyMessage(messageTarget);
                }
                break;
            case 0:
                break;
        }
    }


    @Override
    public void onReceiveYourMessage(DataMessageTarget messageTarget) {
        for (int i = 0; i < messages.size(); i++) {
            if (messageTarget.getLocalId().equals(messages.get(i).getLocalId())) {
                messages.get(i).setStatus(messageTarget.getStatus());
                notifyDataSetChanged();
            }
        }
    }


    private static class SenderMessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView msgSent;
        private final TextView time;
        private final ImageView status;
        private ChatMenu menu;

        public SenderMessageViewHolder(View itemView) {
            super(itemView);
            msgSent = itemView.findViewById(R.id.message_sent);
            time = itemView.findViewById(R.id.message_time);
            status = itemView.findViewById(R.id.msg_indicator);
            menu = new ChatMenu(itemView.getContext());
        }

        @RequiresApi(api = Build.VERSION_CODES.Q)
        public void bind(DataMessageTarget messageTarget) {
            msgSent.setText(messageTarget.getText());
            msgSent.setOnLongClickListener(view -> {
                menu.createPopUpMenu(msgSent, messageTarget.getId(), messageTarget);
                int i = menu.getSelectedMenu();
                return true;
            });
            if (messageTarget.getCreatedAt() != null) {
                time.setText(normalTime(messageTarget.getCreatedAt()));
            }
            if (messageTarget.getStatus() != null && !messageTarget.getStatus().isEmpty()) {
                switch (messageTarget.getStatus()) {
                    case MESSAGE_UN_SEND:
                    case MESSAGE_SENT:
                        status.setImageResource(R.drawable.ic_msg_indicator_sent);
                        break;
                    case MESSAGE_DELIVERED:
                        status.setImageResource(R.drawable.ic_message_received);
                        break;
                    case MESSAGE_READ:
                        status.setImageResource(R.drawable.ic_msg_indicator_read);
                        break;
                }
            }
        }
    }

    private static class SenderReplyViewHolder extends RecyclerView.ViewHolder {
        private final TextView msgReply;
        private final TextView time;
        private final ImageView status;
        private ChatMenu menu;
        private final TextView replyUserName;
        private final TextView userReplyText;
        private final RoundedImageView replyImage;
        private final View line;

        public SenderReplyViewHolder(View itemView) {
            super(itemView);
            msgReply = itemView.findViewById(R.id.reply_text);
            time = itemView.findViewById(R.id.message_time);
            replyUserName = itemView.findViewById(R.id.reply_contact_name);
            userReplyText = itemView.findViewById(R.id.user_reply_text);
            status = itemView.findViewById(R.id.msg_indicator);
            menu = new ChatMenu(itemView.getContext());
            replyImage = itemView.findViewById(R.id.reply_file_img);
            line = itemView.findViewById(R.id.line);
        }

        @RequiresApi(api = Build.VERSION_CODES.Q)
        public void bind(DataMessageTarget messageTarget) {
            msgReply.setText(messageTarget.getText());
            time.setText(normalTime(messageTarget.getCreatedAt()));
            itemView.setOnLongClickListener(view -> {
                menu.createPopUpMenu(itemView, messageTarget.getId(), messageTarget);
                return true;
            });
            if (messageTarget.getAnswering().getType().equals(STRING)) {
                replyUserName.setText(messageTarget.getAnswering().getAuthor().getPersonalData().getName());
                userReplyText.setText(messageTarget.getAnswering().getText());
            } else if (messageTarget.getAnswering().getType().equals(PHOTO)) {
                replyImage.setVisibility(View.VISIBLE);
                line.setVisibility(View.GONE);
                userReplyText.setText("Photo");
                Picasso.get().load(BASE_PHOTO + messageTarget.getAnswering().getAttachment().getFileUrl()).placeholder(R.color.primary).into(replyImage);
            } else if (messageTarget.getAnswering().getType().equals(FILE)) {
                replyImage.setVisibility(View.VISIBLE);
                line.setVisibility(View.GONE);
                userReplyText.setText("File");
                replyImage.setImageResource(R.drawable.ic_documenyt_file);
            }


            if (messageTarget.getStatus() != null && !messageTarget.getStatus().isEmpty()) {
                switch (messageTarget.getStatus()) {
                    case MESSAGE_UN_SEND:
                    case MESSAGE_SENT:
                        status.setImageResource(R.drawable.ic_msg_indicator_sent);
                        break;
                    case MESSAGE_DELIVERED:
                        status.setImageResource(R.drawable.ic_message_received);
                        break;
                    case MESSAGE_READ:
                        status.setImageResource(R.drawable.ic_msg_indicator_read);
                        break;
                }
            }
        }
    }


    private class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView msgReceived;
        private final TextView time;
        private ChatMenu menu;
        private final LinearLayout senderGroupLayout;
        private final RoundedImageView senderGroupAvatar;
        private final TextView senderGroupName;


        public ReceivedMessageViewHolder(View itemView) {
            super(itemView);
            msgReceived = itemView.findViewById(R.id.message_received);
            time = itemView.findViewById(R.id.message_time);
            menu = new ChatMenu(itemView.getContext());
            senderGroupLayout = itemView.findViewById(R.id.layout_sender_group);
            senderGroupAvatar = itemView.findViewById(R.id.group_sender_avatar);
            senderGroupName = itemView.findViewById(R.id.name_sender_group);
        }


        @RequiresApi(api = Build.VERSION_CODES.Q)
        public void bind(DataMessageTarget messageTarget, int type) {
            if (type == GROUP) {
                senderGroupLayout.setVisibility(View.VISIBLE);
                Picasso.get().load(BASE_PHOTO + messageTarget.getAuthor().getAvatar()).placeholder(R.color.primary).into(senderGroupAvatar);
                senderGroupName.setText(messageTarget.getAuthor().getPersonalData().getName() + "  " + messageTarget.getAuthor().getPersonalData().getSurname());
            } else {
                senderGroupLayout.setVisibility(View.GONE);
            }
            msgReceived.setText(messageTarget.getText());
            msgReceived.setOnLongClickListener(view -> {
                menu.createPopUpMenu(msgReceived, messageTarget.getId(), messageTarget);
                return true;
            });
            time.setText(normalTime(messageTarget.getCreatedAt()));

        }
    }

    private class ReceivedReplyViewHolder extends RecyclerView.ViewHolder {
        private final TextView time;
        private final TextView userName;
        private final TextView replyUserText;
        private final TextView text;
        private final ChatMenu menu;
        private final LinearLayout senderGroupLayout;
        private final RoundedImageView senderGroupAvatar;
        private final TextView senderGroupName;

        public ReceivedReplyViewHolder(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.message_time);
            userName = itemView.findViewById(R.id.reply_contact_name);
            replyUserText = itemView.findViewById(R.id.user_reply_text);
            text = itemView.findViewById(R.id.reply_text);
            menu = new ChatMenu(itemView.getContext());
            senderGroupLayout = itemView.findViewById(R.id.layout_sender_group);
            senderGroupAvatar = itemView.findViewById(R.id.group_sender_avatar);
            senderGroupName = itemView.findViewById(R.id.name_sender_group);
        }


        @RequiresApi(api = Build.VERSION_CODES.Q)
        public void bind(DataMessageTarget messageTarget, int type) {
            if (type == GROUP) {
                senderGroupLayout.setVisibility(View.VISIBLE);
                Picasso.get().load(BASE_PHOTO + messageTarget.getAuthor().getAvatar()).placeholder(R.color.primary).into(senderGroupAvatar);
                senderGroupName.setText(messageTarget.getAuthor().getPersonalData().getName() + "  " + messageTarget.getAuthor().getPersonalData().getSurname());
            } else {
                senderGroupLayout.setVisibility(View.GONE);
            }
            text.setText(messageTarget.getText());
            time.setText(normalTime(messageTarget.getCreatedAt()));
            userName.setText(messageTarget.getAnswering().getAuthor().getPersonalData().getName());
            replyUserText.setText(messageTarget.getAnswering().getText());
            itemView.setOnLongClickListener(view -> {
                menu.createPopUpMenu(itemView, messageTarget.getId(), messageTarget);
                return true;
            });
        }
    }

    private class SenderVoiceViewHolder extends RecyclerView.ViewHolder {
        private final TextView voiceInformation;
        private final ImageView status;
        private final TextView time;
        private final MusicProgressBar voiceProgress;
        private ChatMenu menu;
        private ImageView playPause;
        private boolean isPause = true;

        public SenderVoiceViewHolder(View itemView) {
            super(itemView);
            voiceInformation = itemView.findViewById(R.id.voice_message_info);
            status = itemView.findViewById(R.id.msg_indicator);
            time = itemView.findViewById(R.id.message_time);
            voiceProgress = itemView.findViewById(R.id.sent_voice_progress);
            playPause = itemView.findViewById(R.id.play_pause_voice_message);
            menu = new ChatMenu(itemView.getContext());
        }

        @RequiresApi(api = Build.VERSION_CODES.Q)
        public void bind(DataMessageTarget oneMessage) {
            setInformation(oneMessage);
            setListeners(oneMessage);


        }

        @RequiresApi(api = Build.VERSION_CODES.Q)
        private void setListeners(DataMessageTarget oneMessage) {
            itemView.setOnLongClickListener(view -> {
                menu.createPopUpMenu(itemView, oneMessage.getId(), oneMessage);
                return true;
            });
            playPause.setOnClickListener(view -> {
                DataMessageTarget oneVoiceMessage = messages.get(getAdapterPosition());
                Log.e("Play_pause", "play_clicked");
                isPause = !isPause;
                if (!isPause) {
                    playPause.setImageResource(R.drawable.ic_pause_circle);
                } else {
                    playPause.setImageResource(R.drawable.ic_play_circle);
                }
                if (!isPause) {
                    setVoiceProgress(oneVoiceMessage);
                } else {
                    MEDIA_PLAYER.stop();
                }
            });
        }

        private void setInformation(DataMessageTarget oneMessage) {
            if (oneMessage.getStatus() != null && !oneMessage.getStatus().isEmpty()) {
                switch (oneMessage.getStatus()) {
                    case MESSAGE_UN_SEND:
                    case MESSAGE_SENT:
                        status.setImageResource(R.drawable.ic_msg_indicator_sent);
                        break;
                    case MESSAGE_DELIVERED:
                        status.setImageResource(R.drawable.ic_message_received);
                        break;
                    case MESSAGE_READ:
                        status.setImageResource(R.drawable.ic_msg_indicator_read);
                        break;
                }
            }
            voiceInformation.setText(oneMessage.getAttachment().getSize() / 1000 + "KB");
            time.setText(normalTime(oneMessage.getCreatedAt()));
        }

        private void setVoiceProgress(DataMessageTarget oneVoiceMessage) {
            String audioUrl = oneVoiceMessage.getAttachment().getFileUrl();
            if (MEDIA_PLAYER != null) {
                MEDIA_PLAYER.stop();
                MEDIA_PLAYER.release();
            }
            MEDIA_PLAYER = new MediaPlayer();
            MEDIA_PLAYER.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                MEDIA_PLAYER.setDataSource(BASE_URL + "/" + audioUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            MEDIA_PLAYER.prepareAsync();
            MEDIA_PLAYER.setOnCompletionListener(mp -> {
                // Playback has completed, update progress bar to full duration
                MEDIA_PLAYER.reset();
                playPause.setImageResource(R.drawable.ic_play_circle);
                voiceProgress.setCurrentProgress(0);
                isPause = true;
            });
            MEDIA_PLAYER.setOnPreparedListener(mp -> {
                // The MediaPlayer is prepared, you can start playing the audio
                MEDIA_PLAYER.start();
                Handler handler = new Handler();
                Runnable progressRunnable = new Runnable() {
                    @Override
                    public void run() {
                        if (MEDIA_PLAYER != null && MEDIA_PLAYER.isPlaying()) {
                            int currentPosition = MEDIA_PLAYER.getCurrentPosition();
                            int totalDuration = MEDIA_PLAYER.getDuration();

                            voiceProgress.setMaxProgress(totalDuration);
                            voiceProgress.setCurrentProgress(currentPosition);

                            handler.postDelayed(this, 1000); // Update every 1 second
                        }
                    }
                };

                handler.post(progressRunnable);
            });
            MEDIA_PLAYER.setOnErrorListener((mp, what, extra) -> {
                // Handle the error
                Toast.makeText(itemView.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                return false; // Return false to indicate that the error is not handled and should be propagated
            });

        }
    }

    private class ReceivedVoiceViewHolder extends RecyclerView.ViewHolder {
        private final TextView voiceInformation;
        private final TextView time;
        private final ChatMenu menu;
        private final LinearLayout senderGroupLayout;
        private final RoundedImageView senderGroupAvatar;
        private final TextView senderGroupName;
        private final ImageView playPause;
        private final MusicProgressBar voiceProgress;
        private boolean isPause = false;

        public ReceivedVoiceViewHolder(View itemView) {
            super(itemView);
            voiceInformation = itemView.findViewById(R.id.voice_message_info);
            time = itemView.findViewById(R.id.message_time);
            menu = new ChatMenu(itemView.getContext());
            senderGroupLayout = itemView.findViewById(R.id.layout_sender_group);
            senderGroupAvatar = itemView.findViewById(R.id.group_sender_avatar);
            senderGroupName = itemView.findViewById(R.id.name_sender_group);
            playPause = itemView.findViewById(R.id.play_pause_voice_message);
            voiceProgress = itemView.findViewById(R.id.received_voice_progress);
        }

        @RequiresApi(api = Build.VERSION_CODES.Q)
        public void bind(DataMessageTarget oneMessage, int type) {
            if (type == GROUP) {
                senderGroupLayout.setVisibility(View.VISIBLE);
                Picasso.get().load(BASE_PHOTO + oneMessage.getAuthor().getAvatar()).placeholder(R.color.primary).into(senderGroupAvatar);
                senderGroupName.setText(oneMessage.getAuthor().getPersonalData().getName() + "  " + oneMessage.getAuthor().getPersonalData().getSurname());
            } else {
                senderGroupLayout.setVisibility(View.GONE);
            }
            itemView.setOnLongClickListener(view -> {
                menu.createPopUpMenu(itemView, oneMessage.getId(), oneMessage);
                return true;
            });
            voiceInformation.setText(oneMessage.getAttachment().getSize() / 1000 + "KB");
            time.setText(normalTime(oneMessage.getCreatedAt()));
            playPause.setOnClickListener(view -> {
                DataMessageTarget oneVoiceMessage = messages.get(getAdapterPosition());
                Log.e("Play_pause", "play_clicked");
                isPause = !isPause;
                if (!isPause) {
                    playPause.setImageResource(R.drawable.ic_pause_circle);
                } else {
                    playPause.setImageResource(R.drawable.ic_play_circle);
                }
                if (!isPause) {
                    setVoiceProgress(oneVoiceMessage);
                } else {
                    MEDIA_PLAYER.stop();
                }
            });

        }

        private void setVoiceProgress(DataMessageTarget oneVoiceMessage) {
            String audioUrl = oneVoiceMessage.getAttachment().getFileUrl();
            if (MEDIA_PLAYER != null) {
                MEDIA_PLAYER.stop();
                MEDIA_PLAYER.release();
            }
            MEDIA_PLAYER = new MediaPlayer();
            MEDIA_PLAYER.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                MEDIA_PLAYER.setDataSource(BASE_URL + "/" + audioUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            MEDIA_PLAYER.prepareAsync();
            MEDIA_PLAYER.setOnCompletionListener(mp -> {
                // Playback has completed, update progress bar to full duration
                MEDIA_PLAYER.reset();
                playPause.setImageResource(R.drawable.ic_play_circle);
                voiceProgress.setCurrentProgress(0);
                isPause = true;
            });
            MEDIA_PLAYER.setOnPreparedListener(mp -> {
                // The MediaPlayer is prepared, you can start playing the audio
                MEDIA_PLAYER.start();
                Handler handler = new Handler();
                Runnable progressRunnable = new Runnable() {
                    @Override
                    public void run() {
                        if (MEDIA_PLAYER != null && MEDIA_PLAYER.isPlaying()) {
                            int currentPosition = MEDIA_PLAYER.getCurrentPosition();
                            int totalDuration = MEDIA_PLAYER.getDuration();

                            voiceProgress.setMaxProgress(totalDuration);
                            voiceProgress.setCurrentProgress(currentPosition);

                            handler.postDelayed(this, 1000); // Update every 1 second
                        }
                    }
                };

                handler.post(progressRunnable);
            });
            MEDIA_PLAYER.setOnErrorListener((mp, what, extra) -> {
                // Handle the error
                Toast.makeText(itemView.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                return false; // Return false to indicate that the error is not handled and should be propagated
            });

        }

    }

    private static class SenderFileViewHolder extends RecyclerView.ViewHolder {
        private final TextView fileName;
        private final TextView time;
        private final ImageView status;
        private final TextView fileInformation;
        private final ChatMenu menu;

        public SenderFileViewHolder(View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.msg_file_name);
            time = itemView.findViewById(R.id.message_time);
            status = itemView.findViewById(R.id.msg_indicator);
            fileInformation = itemView.findViewById(R.id.msg_file_info);
            menu = new ChatMenu(itemView.getContext());
        }

        @RequiresApi(api = Build.VERSION_CODES.Q)
        public void bind(DataMessageTarget oneMessage) {
            itemView.setOnLongClickListener(view -> {
                menu.createPopUpMenu(itemView, oneMessage.getId(), oneMessage);
                return true;
            });
            fileName.setText(oneMessage.getAttachment().getFileName());
            fileInformation.setText(oneMessage.getAttachment().getDuration() + "," + oneMessage.getAttachment().getSize());
            time.setText(normalTime(oneMessage.getCreatedAt()));
            if (oneMessage.getStatus() != null && !oneMessage.getStatus().isEmpty()) {
                switch (oneMessage.getStatus()) {
                    case MESSAGE_UN_SEND:
                    case MESSAGE_SENT:
                        status.setImageResource(R.drawable.ic_msg_indicator_sent);
                        break;
                    case MESSAGE_DELIVERED:
                        status.setImageResource(R.drawable.ic_message_received);
                        break;
                    case MESSAGE_READ:
                        status.setImageResource(R.drawable.ic_msg_indicator_read);
                        break;
                }
            }
        }

    }

    private static class ReceivedFileViewHolder extends RecyclerView.ViewHolder {
        private final TextView fileName;
        private final TextView time;
        private final TextView fileInformation;
        private final ChatMenu menu;
        private final LinearLayout senderGroupLayout;
        private final RoundedImageView senderGroupAvatar;
        private final TextView senderGroupName;

        public ReceivedFileViewHolder(View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.msg_file_name);
            time = itemView.findViewById(R.id.message_time);
            fileInformation = itemView.findViewById(R.id.file_msg_rec_info);
            menu = new ChatMenu(itemView.getContext());
            senderGroupLayout = itemView.findViewById(R.id.layout_sender_group);
            senderGroupAvatar = itemView.findViewById(R.id.group_sender_avatar);
            senderGroupName = itemView.findViewById(R.id.name_sender_group);
        }

        @RequiresApi(api = Build.VERSION_CODES.Q)
        public void bind(DataMessageTarget oneMessage, int type) {
            if (type == GROUP) {
                senderGroupLayout.setVisibility(View.VISIBLE);
                Picasso.get().load(BASE_PHOTO + oneMessage.getAuthor().getAvatar()).placeholder(R.color.primary).into(senderGroupAvatar);
                senderGroupName.setText(oneMessage.getAuthor().getPersonalData().getName() + "  " + oneMessage.getAuthor().getPersonalData().getSurname());
            } else {
                senderGroupLayout.setVisibility(View.GONE);
            }
            itemView.setOnLongClickListener(view -> {
                menu.createPopUpMenu(itemView, oneMessage.getId(), oneMessage);
                return true;
            });
            if (oneMessage.getAttachment() != null) {
                if (oneMessage.getAttachment().getFileName() != null) {
                    fileName.setText(oneMessage.getAttachment().getFileName());
                }
                if (oneMessage.getAttachment().getSize() == 0) {
                    fileInformation.setText(oneMessage.getAttachment().getSize());
                }
                if (oneMessage.getCreatedAt() != null)
                    time.setText(normalTime(oneMessage.getCreatedAt()));
            }

        }
    }

    private class SendImageViewHolder extends RecyclerView.ViewHolder {
        private final RoundedImageView image;
        private final ImageView status;
        private final TextView imageSentTime;
        private final TextView imageSentSize;
        private final ChatMenu menu;

        public SendImageViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            status = itemView.findViewById(R.id.msg_indicator);
            imageSentTime = itemView.findViewById(R.id.img_sent_time);
            imageSentSize = itemView.findViewById(R.id.img_size);
            menu = new ChatMenu(itemView.getContext());
        }

        @RequiresApi(api = Build.VERSION_CODES.Q)
        public void bind(DataMessageTarget oneMessage) {
            itemView.setOnLongClickListener(view -> {
                menu.createPopUpMenu(itemView, oneMessage.getId(), oneMessage);
                return true;
            });
            Picasso.get().load(BASE_URL + "/" + oneMessage.getAttachment().getFileUrl()).placeholder(R.color.primary).into(image);
            imageSentTime.setText(normalTime(oneMessage.getCreatedAt()));
            if (oneMessage.getAttachment().getSize() != 0) {
                imageSentSize.setText(String.valueOf(oneMessage.getAttachment().getSize() / 1000) + "KB");
            }
            if (oneMessage.getStatus() != null && !oneMessage.getStatus().isEmpty()) {
                switch (oneMessage.getStatus()) {
                    case MESSAGE_UN_SEND:
                    case MESSAGE_SENT:
                        status.setImageResource(R.drawable.ic_msg_indicator_sent);
                        break;
                    case MESSAGE_DELIVERED:
                        status.setImageResource(R.drawable.ic_message_received);
                        break;
                    case MESSAGE_READ:
                        status.setImageResource(R.drawable.ic_msg_indicator_read);
                        break;
                }
            }

        }
    }

    private class ReceivedImageViewHolder extends RecyclerView.ViewHolder {
        private final RoundedImageView image;
        private final TextView imageSentTime;
        private final TextView imageSentSize;
        private final ChatMenu menu;
        private final LinearLayout senderGroupLayout;
        private final RoundedImageView senderGroupAvatar;
        private final TextView senderGroupName;

        public ReceivedImageViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            imageSentTime = itemView.findViewById(R.id.img_sent_time);
            imageSentSize = itemView.findViewById(R.id.img_size);
            menu = new ChatMenu(itemView.getContext());
            senderGroupLayout = itemView.findViewById(R.id.layout_sender_group);
            senderGroupAvatar = itemView.findViewById(R.id.group_sender_avatar);
            senderGroupName = itemView.findViewById(R.id.name_sender_group);
        }

        @RequiresApi(api = Build.VERSION_CODES.Q)
        @SuppressLint("SetTextI18n")
        public void bind(DataMessageTarget oneMessage, int type) {
            if (type == GROUP) {
                senderGroupLayout.setVisibility(View.VISIBLE);
                Picasso.get().load(BASE_PHOTO + oneMessage.getAuthor().getAvatar()).placeholder(R.color.primary).into(senderGroupAvatar);
                senderGroupName.setText(oneMessage.getAuthor().getPersonalData().getName() + "  " + oneMessage.getAuthor().getPersonalData().getSurname());
            } else {
                senderGroupLayout.setVisibility(View.GONE);
            }
            itemView.setOnLongClickListener(view -> {
                menu.createPopUpMenu(itemView, oneMessage.getId(), oneMessage);
                return true;
            });
            Picasso.get().load(BASE_URL + "/" + oneMessage.getAttachment().getFileUrl()).placeholder(R.color.primary).into(image);
            imageSentTime.setText(normalTime(oneMessage.getCreatedAt()));
            Integer size = oneMessage.getAttachment().getSize();
            if (size != 0) {
                // The size is available and not equal to zero
                imageSentSize.setText(String.valueOf(size / 1000) + "KB");
            }
        }
    }

    private static class DateViewHolder extends RecyclerView.ViewHolder {
        private final TextView date;

        public DateViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
        }

        public void bind(DataMessageTarget oneMessage) {
            date.setText(oneMessage.getText());
        }
    }


}