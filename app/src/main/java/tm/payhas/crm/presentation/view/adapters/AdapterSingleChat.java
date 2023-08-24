package tm.payhas.crm.presentation.view.adapters;

import static tm.payhas.crm.data.remote.api.network.Network.BASE_PHOTO;
import static tm.payhas.crm.data.remote.api.network.Network.BASE_URL;
import static tm.payhas.crm.domain.helpers.Common.addFragment;
import static tm.payhas.crm.domain.helpers.Common.normalDate;
import static tm.payhas.crm.domain.helpers.Common.normalTime;
import static tm.payhas.crm.domain.helpers.StaticMethods.getPlaceholder;
import static tm.payhas.crm.domain.statics.StaticConstants.DATE;
import static tm.payhas.crm.domain.statics.StaticConstants.EXCEL;
import static tm.payhas.crm.domain.statics.StaticConstants.FILE;
import static tm.payhas.crm.domain.statics.StaticConstants.MESSAGE_DELIVERED;
import static tm.payhas.crm.domain.statics.StaticConstants.MESSAGE_READ;
import static tm.payhas.crm.domain.statics.StaticConstants.MESSAGE_SENDING;
import static tm.payhas.crm.domain.statics.StaticConstants.MESSAGE_SENT;
import static tm.payhas.crm.domain.statics.StaticConstants.MESSAGE_UN_SEND;
import static tm.payhas.crm.domain.statics.StaticConstants.PDF;
import static tm.payhas.crm.domain.statics.StaticConstants.PHOTO;
import static tm.payhas.crm.domain.statics.StaticConstants.POWER_POINT;
import static tm.payhas.crm.domain.statics.StaticConstants.STRING;
import static tm.payhas.crm.domain.statics.StaticConstants.VOICE;
import static tm.payhas.crm.domain.statics.StaticConstants.WORD;
import static tm.payhas.crm.presentation.view.activity.ActivityMain.mainFragmentManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tm.payhas.crm.R;
import tm.payhas.crm.data.localdb.entity.EntityMessage;
import tm.payhas.crm.domain.helpers.FileFormatUtil;
import tm.payhas.crm.domain.useCases.UseCaseChatRoom;
import tm.payhas.crm.domain.helpers.ChatMenu;
import tm.payhas.crm.data.localdb.preference.AccountPreferences;
import tm.payhas.crm.presentation.view.fragment.FragmentPhotoItem;
import tm.payhas.crm.presentation.viewModel.ViewModelChatRoom;

public class AdapterSingleChat extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<EntityMessage> messages = new ArrayList<>();
    private Context context;
    private Activity activity;
    private Integer authorId = 0;
    private Integer roomId = 0;
    private String TAG = "AdapterSingleChat";
    private AccountPreferences accountPreferences;
    private int type;
    public UseCaseChatRoom useCaseChatRoom;
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
    private ViewModelChatRoom viewModelChatRoom;
    private ChatMenu chatMenu;


    public AdapterSingleChat(Context context, Integer roomId, int type, int authorId, UseCaseChatRoom useCaseChatRoom, ViewModelChatRoom viewModelChatRoom, RecyclerView recyclerView) {
        this.roomId = roomId;
        this.context = context;
        this.authorId = authorId;
        this.type = type;
        this.useCaseChatRoom = useCaseChatRoom;
        accountPreferences = new AccountPreferences(context);
        this.viewModelChatRoom = viewModelChatRoom;
        chatMenu = new ChatMenu(context, viewModelChatRoom);
    }

    public void setMessages(List<EntityMessage> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public int getItemViewType(int position) {
        EntityMessage oneMessage = messages.get(position);
        switch (oneMessage.getType()) {
            case STRING:
                if (oneMessage.getAuthorId() == authorId)
                    if (oneMessage.getAnswering() == null) return LAYOUT_SEND_MESSAGE;
                    else return LAYOUT_SEND_REPLY;
                else if (oneMessage.getAnswering() == null) return LAYOUT_RECEIVED_MESSAGE;
                else return LAYOUT_RECEIVED_REPLY;
            case VOICE:
                if (oneMessage.getAuthorId() == authorId) return LAYOUT_SEND_VOICE_MESSAGE;
                else return LAYOUT_RECEIVED_VOICE_MESSAGE;
            case PHOTO:
                if (oneMessage.getAuthorId() == authorId) return LAYOUT_SEND_IMAGE;
                else return LAYOUT_RECEIVED_IMAGE;
            case FILE:
                if (oneMessage.getAuthorId() == authorId) return LAYOUT_SEND_FILE;
                else return LAYOUT_RECEIVED_FILE;
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
        EntityMessage oneMessage = messages.get(position);
        viewModelChatRoom.setCurrentMessagesDate(normalDate(oneMessage.getCreatedAt()));
        switch (oneMessage.getType()) {
            case STRING:
                if (oneMessage.getAuthorId() == authorId) {
                    if (oneMessage.getAnswering() == null) {
                        ((SenderMessageViewHolder) holder).bind(oneMessage);
                    } else {
                        ((SenderReplyViewHolder) holder).bind(oneMessage);
                    }
                } else {
                    if (oneMessage.getAnswering() == null) {
                        ((ReceivedMessageViewHolder) holder).bind(oneMessage, type);
                    } else {
                        ((ReceivedReplyViewHolder) holder).bind(oneMessage, type);
                    }
                }
                break;
            case VOICE:
                if (oneMessage.getAuthorId() == authorId) {
                    ((SenderVoiceViewHolder) holder).bind(oneMessage);
                } else ((ReceivedVoiceViewHolder) holder).bind(oneMessage, type);
                break;
            case PHOTO:
                if (oneMessage.getAuthorId() == authorId)
                    ((SendImageViewHolder) holder).bind(oneMessage);
                else ((ReceivedImageViewHolder) holder).bind(oneMessage, type);
                break;
            case FILE:
                if (oneMessage.getAuthorId() == authorId)
                    ((SenderFileViewHolder) holder).bind(oneMessage);
                else ((ReceivedFileViewHolder) holder).bind(oneMessage, type);
                break;
            case DATE:
                ((DateViewHolder) holder).bind(oneMessage);
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (messages != null && messages.size() != 0) {
            return messages.size();
        } else {
            return 0;
        }

    }


    private class SenderMessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView msgSent;
        private final TextView time;
        private final ImageView status;
        private FrameLayout clickerResend;

        public SenderMessageViewHolder(View itemView) {
            super(itemView);
            msgSent = itemView.findViewById(R.id.message_sent);
            time = itemView.findViewById(R.id.message_time);
            status = itemView.findViewById(R.id.msg_indicator);
            clickerResend = itemView.findViewById(R.id.clicker_resend);

        }

        @RequiresApi(api = Build.VERSION_CODES.Q)
        public void bind(EntityMessage messageTarget) {
            msgSent.setText(messageTarget.getText());
            msgSent.setOnLongClickListener(view -> {
                chatMenu.showMenu(messageTarget, itemView, true);
                return true;
            });
            if (messageTarget.getCreatedAt() != null) {
                time.setText(normalTime(messageTarget.getCreatedAt()));
            }
            clickerResend.setOnClickListener(view -> {
                useCaseChatRoom.showDialog(context, messageTarget);
            });
            if (messageTarget.getStatus() != null && !messageTarget.getStatus().isEmpty()) {
                switch (messageTarget.getStatus()) {
                    case MESSAGE_SENDING:
                        status.setVisibility(View.GONE);
                        Log.e(TAG, "bind: +receiving sending !");
                        break;
                    case MESSAGE_UN_SEND:
                        Log.e(TAG, "bind: +receiving unsent!");
                        status.setVisibility(View.VISIBLE);
                        status.setImageResource(R.drawable.ic_error_message);
                        break;
                    case MESSAGE_SENT:
                        status.setVisibility(View.VISIBLE);
                        status.setImageResource(R.drawable.ic_msg_indicator_sent);
                        break;
                    case MESSAGE_DELIVERED:
                        status.setVisibility(View.VISIBLE);
                        status.setImageResource(R.drawable.ic_message_received);
                        break;
                    case MESSAGE_READ:
                        status.setVisibility(View.VISIBLE);
                        status.setImageResource(R.drawable.ic_msg_indicator_read);
                        break;
                }
            }
        }
    }

    private class SenderReplyViewHolder extends RecyclerView.ViewHolder {
        private final TextView msgReply;
        private final TextView time;
        private final ImageView status;
        private final TextView replyUserName;
        private final TextView userReplyText;
        private final RoundedImageView replyImage;
        private final View line;
        private FrameLayout clickerResend;

        public SenderReplyViewHolder(View itemView) {
            super(itemView);
            msgReply = itemView.findViewById(R.id.reply_text);
            time = itemView.findViewById(R.id.message_time);
            replyUserName = itemView.findViewById(R.id.reply_contact_name);
            userReplyText = itemView.findViewById(R.id.user_reply_text);
            status = itemView.findViewById(R.id.msg_indicator);
            replyImage = itemView.findViewById(R.id.reply_file_img);
            line = itemView.findViewById(R.id.line);
            clickerResend = itemView.findViewById(R.id.clicker_resend);
        }

        @RequiresApi(api = Build.VERSION_CODES.Q)
        public void bind(EntityMessage messageTarget) {
            msgReply.setText(messageTarget.getText());
            time.setText(normalTime(messageTarget.getCreatedAt()));
            itemView.setOnLongClickListener(view -> {
                chatMenu.showMenu(messageTarget, itemView, true);
                return true;
            });
            if (messageTarget.getAnswering() != null) {
                switch (messageTarget.getAnswering().getTypeAnswer()) {
                    case STRING:
                        replyUserName.setText(messageTarget.getAnswering().getAuthorAnswer().getPersonalDataAuthor().getName());
                        userReplyText.setText(messageTarget.getAnswering().getTextAnswer());
                        break;
                    case PHOTO:
                        replyImage.setVisibility(View.VISIBLE);
                        line.setVisibility(View.GONE);
                        userReplyText.setText("Photo");
                        Picasso.get().load(BASE_PHOTO + messageTarget.getAnswering().getAttachmentAnswer().getFileUrl()).placeholder(R.color.primary).into(replyImage);
                        break;
                    case FILE:
                        replyImage.setVisibility(View.VISIBLE);
                        line.setVisibility(View.GONE);
                        userReplyText.setText("File");
                        replyImage.setImageResource(R.drawable.ic_documenyt_file);
                        break;
                }
            }
            clickerResend.setOnClickListener(view -> {
                useCaseChatRoom.showDialog(context, messageTarget);
            });
            if (messageTarget.getStatus() != null && !messageTarget.getStatus().isEmpty()) {
                switch (messageTarget.getStatus()) {
                    case MESSAGE_SENDING:
                        status.setVisibility(View.GONE);
                        break;
                    case MESSAGE_UN_SEND:
                        status.setVisibility(View.VISIBLE);
                        status.setImageResource(R.drawable.ic_error_message);
                        break;
                    case MESSAGE_SENT:
                        status.setVisibility(View.VISIBLE);
                        status.setImageResource(R.drawable.ic_msg_indicator_sent);
                        break;
                    case MESSAGE_DELIVERED:
                        status.setVisibility(View.VISIBLE);
                        status.setImageResource(R.drawable.ic_message_received);
                        break;
                    case MESSAGE_READ:
                        status.setVisibility(View.VISIBLE);
                        status.setImageResource(R.drawable.ic_msg_indicator_read);
                        break;
                }
            }
        }
    }


    private class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView msgReceived;
        private final TextView time;
        private final LinearLayout senderGroupLayout;
        private final RoundedImageView senderGroupAvatar;
        private final TextView senderGroupName;


        public ReceivedMessageViewHolder(View itemView) {
            super(itemView);
            msgReceived = itemView.findViewById(R.id.message_received);
            time = itemView.findViewById(R.id.message_time);
            senderGroupLayout = itemView.findViewById(R.id.layout_sender_group);
            senderGroupAvatar = itemView.findViewById(R.id.group_sender_avatar);
            senderGroupName = itemView.findViewById(R.id.name_sender_group);
        }


        @RequiresApi(api = Build.VERSION_CODES.Q)
        public void bind(EntityMessage messageTarget, int type) {
            if (type == AdapterChatContact.GROUP) {
                senderGroupLayout.setVisibility(View.VISIBLE);
                Picasso.get().load(BASE_PHOTO + messageTarget.getAuthor().getAvatarAuthor()).placeholder(R.color.primary).into(senderGroupAvatar);
                senderGroupName.setText(messageTarget.getAuthor().getPersonalDataAuthor().getName() + "  " + messageTarget.getAuthor().getPersonalDataAuthor().getSurname());
            } else {
                senderGroupLayout.setVisibility(View.GONE);
            }
            msgReceived.setText(messageTarget.getText());
            msgReceived.setOnLongClickListener(view -> {
                chatMenu.showMenu(messageTarget, itemView, false);
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
        private final LinearLayout senderGroupLayout;
        private final RoundedImageView senderGroupAvatar;
        private final TextView senderGroupName;

        public ReceivedReplyViewHolder(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.message_time);
            userName = itemView.findViewById(R.id.reply_contact_name);
            replyUserText = itemView.findViewById(R.id.user_reply_text);
            text = itemView.findViewById(R.id.reply_text);
            senderGroupLayout = itemView.findViewById(R.id.layout_sender_group);
            senderGroupAvatar = itemView.findViewById(R.id.group_sender_avatar);
            senderGroupName = itemView.findViewById(R.id.name_sender_group);
        }


        @RequiresApi(api = Build.VERSION_CODES.Q)
        public void bind(EntityMessage messageTarget, int type) {
            if (type == AdapterChatContact.GROUP) {
                senderGroupLayout.setVisibility(View.VISIBLE);
                Picasso.get().load(BASE_PHOTO + messageTarget.getAuthor().getAvatarAuthor()).placeholder(R.color.primary).into(senderGroupAvatar);
                senderGroupName.setText(messageTarget.getAuthor().getPersonalDataAuthor().getName() + "  " + messageTarget.getAuthor().getPersonalDataAuthor().getSurname());
            } else {
                senderGroupLayout.setVisibility(View.GONE);
            }
            text.setText(messageTarget.getText());
            time.setText(normalTime(messageTarget.getCreatedAt()));
            if (messageTarget.getAnswering() != null) {
                userName.setText(messageTarget.getAnswering().getAuthorAnswer().getPersonalDataAuthor().getName());
                replyUserText.setText(messageTarget.getAnswering().getTextAnswer());
            }
            itemView.setOnLongClickListener(view -> {
                chatMenu.showMenu(messageTarget, itemView, false);
                return true;
            });
        }
    }

    private class SenderVoiceViewHolder extends RecyclerView.ViewHolder {
        private final TextView voiceInformation;
        private final ImageView status;
        private final TextView time;
        private final SeekBar seekBar;
        private ImageView playPause;
        private boolean isPause = true;
        private FrameLayout clickerResend;
        private MediaPlayer mediaPlayer;
        private Runnable updateSeekBar;
        private Handler seekBarHandler = new Handler();

        public SenderVoiceViewHolder(View itemView) {
            super(itemView);
            voiceInformation = itemView.findViewById(R.id.voice_message_info);
            status = itemView.findViewById(R.id.msg_indicator);
            time = itemView.findViewById(R.id.message_time);
            seekBar = itemView.findViewById(R.id.sent_voice_progress);
            playPause = itemView.findViewById(R.id.play_pause_voice_message);
            clickerResend = itemView.findViewById(R.id.clicker_resend);
        }

        @RequiresApi(api = Build.VERSION_CODES.Q)
        public void bind(EntityMessage oneMessage) {
            setInformation(oneMessage);
            setListeners(oneMessage);

        }

        @RequiresApi(api = Build.VERSION_CODES.Q)
        private void setListeners(EntityMessage oneMessage) {
            itemView.setOnLongClickListener(view -> {
                chatMenu.showMenu(oneMessage, itemView, true);
                return true;
            });
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser && mediaPlayer != null) {
                        mediaPlayer.seekTo(progress);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // This method is not needed for your use case
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // This method is not needed for your use case
                }
            });
            playPause.setOnClickListener(view -> {
                EntityMessage oneVoiceMessage = messages.get(getAdapterPosition());
                Log.e("Play_pause", "play_clicked");
                isPause = !isPause;

                if (mediaPlayer == null) {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    prepareMediaPlayer(oneVoiceMessage);
                }

                if (isPause) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }
                    playPause.setImageResource(R.drawable.ic_play_circle);
                } else {
                    if (mediaPlayer.getDuration() > 0) {
                        if (!mediaPlayer.isPlaying()) {
                            mediaPlayer.start();
                            updateSeekBar.run(); // Start updating seek bar progress
                        }
                        playPause.setImageResource(R.drawable.ic_pause_circle);
                    }
                }
            });

        }

        private void prepareMediaPlayer(EntityMessage oneVoiceMessage) {
            String audioUrl = BASE_URL + "/" + oneVoiceMessage.getAttachment().getFileUrl();

            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(audioUrl);
                mediaPlayer.prepareAsync();

                mediaPlayer.setOnPreparedListener(mp -> {
                    int totalDuration = mediaPlayer.getDuration();
                    seekBar.setMax(totalDuration);

                    updateSeekBar = new Runnable() {
                        @Override
                        public void run() {
                            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                                int currentPosition = mediaPlayer.getCurrentPosition();
                                seekBar.setProgress(currentPosition);
                                seekBarHandler.postDelayed(this, 500); // Update every 500ms
                            }
                        }
                    };

                    mediaPlayer.start();
                    playPause.setImageResource(R.drawable.ic_pause_circle);
                    updateSeekBar.run();
                });

                mediaPlayer.setOnCompletionListener(mp1 -> {
                    playPause.setImageResource(R.drawable.ic_play_circle);
                    isPause = true;
                    mediaPlayer.seekTo(0);
                    seekBar.setProgress(0);
                    seekBarHandler.removeCallbacks(updateSeekBar); // Stop updating seek bar progress
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void setInformation(EntityMessage oneMessage) {
            clickerResend.setOnClickListener(view -> {
                useCaseChatRoom.showDialog(context, oneMessage);
            });
            if (oneMessage.getStatus() != null && !oneMessage.getStatus().isEmpty()) {
                switch (oneMessage.getStatus()) {
                    case MESSAGE_SENDING:
                        status.setVisibility(View.GONE);
                        break;
                    case MESSAGE_UN_SEND:
                        status.setVisibility(View.VISIBLE);
                        status.setImageResource(R.drawable.ic_error_message);
                        break;
                    case MESSAGE_SENT:
                        status.setVisibility(View.VISIBLE);
                        status.setImageResource(R.drawable.ic_msg_indicator_sent);
                        break;
                    case MESSAGE_DELIVERED:
                        status.setVisibility(View.VISIBLE);
                        status.setImageResource(R.drawable.ic_message_received);
                        break;
                    case MESSAGE_READ:
                        status.setVisibility(View.VISIBLE);
                        status.setImageResource(R.drawable.ic_msg_indicator_read);
                        break;
                }
            }
            voiceInformation.setText(oneMessage.getAttachment().getSize() / 1000 + "KB");
            time.setText(normalTime(oneMessage.getCreatedAt()));
        }
    }

    private class ReceivedVoiceViewHolder extends RecyclerView.ViewHolder {
        private final TextView voiceInformation;
        private final TextView time;
        private final LinearLayout senderGroupLayout;
        private final RoundedImageView senderGroupAvatar;
        private final TextView senderGroupName;
        private final ImageView playPause;
        private final SeekBar seekBar;
        private boolean isPause = false;
        private MediaPlayer mediaPlayer;
        private Runnable updateSeekBar;
        private Handler seekBarHandler = new Handler();

        public ReceivedVoiceViewHolder(View itemView) {
            super(itemView);
            voiceInformation = itemView.findViewById(R.id.voice_message_info);
            time = itemView.findViewById(R.id.message_time);
            senderGroupLayout = itemView.findViewById(R.id.layout_sender_group);
            senderGroupAvatar = itemView.findViewById(R.id.group_sender_avatar);
            senderGroupName = itemView.findViewById(R.id.name_sender_group);
            playPause = itemView.findViewById(R.id.play_pause_voice_message);
            seekBar = itemView.findViewById(R.id.received_voice_progress);
        }

        @RequiresApi(api = Build.VERSION_CODES.Q)
        public void bind(EntityMessage oneMessage, int type) {
            if (type == AdapterChatContact.GROUP) {
                senderGroupLayout.setVisibility(View.VISIBLE);
                Picasso.get().load(BASE_PHOTO + oneMessage.getAuthor().getAvatarAuthor()).placeholder(R.color.primary).into(senderGroupAvatar);
                senderGroupName.setText(oneMessage.getAuthor().getPersonalDataAuthor().getName() + "  " + oneMessage.getAuthor().getPersonalDataAuthor().getSurname());
            } else {
                senderGroupLayout.setVisibility(View.GONE);
            }
            itemView.setOnLongClickListener(view -> {
                chatMenu.showMenu(oneMessage, itemView, false);
                return true;
            });
            voiceInformation.setText(oneMessage.getAttachment().getSize() / 1000 + "KB");
            time.setText(normalTime(oneMessage.getCreatedAt()));
            playPause.setOnClickListener(view -> {
                EntityMessage oneVoiceMessage = messages.get(getAdapterPosition());
                Log.e("Play_pause", "play_clicked");
                isPause = !isPause;

                if (mediaPlayer == null) {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    prepareMediaPlayer(oneVoiceMessage);
                }

                if (isPause) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }
                    playPause.setImageResource(R.drawable.ic_play_circle);
                } else {
                    if (mediaPlayer.getDuration() > 0) {
                        if (!mediaPlayer.isPlaying()) {
                            mediaPlayer.start();
                            updateSeekBar.run(); // Start updating seek bar progress
                        }
                        playPause.setImageResource(R.drawable.ic_pause_circle);
                    }
                }
            });
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser && mediaPlayer != null) {
                        mediaPlayer.seekTo(progress);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // This method is not needed for your use case
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // This method is not needed for your use case
                }
            });

        }

        private void prepareMediaPlayer(EntityMessage oneVoiceMessage) {
            String audioUrl = BASE_URL + "/" + oneVoiceMessage.getAttachment().getFileUrl();

            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(audioUrl);
                mediaPlayer.prepareAsync();

                mediaPlayer.setOnPreparedListener(mp -> {
                    int totalDuration = mediaPlayer.getDuration();
                    seekBar.setMax(totalDuration);

                    updateSeekBar = new Runnable() {
                        @Override
                        public void run() {
                            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                                int currentPosition = mediaPlayer.getCurrentPosition();
                                seekBar.setProgress(currentPosition);
                                seekBarHandler.postDelayed(this, 500); // Update every 500ms
                            }
                        }
                    };

                    mediaPlayer.start();
                    playPause.setImageResource(R.drawable.ic_pause_circle);
                    updateSeekBar.run();
                });

                mediaPlayer.setOnCompletionListener(mp1 -> {
                    playPause.setImageResource(R.drawable.ic_play_circle);
                    isPause = true;
                    mediaPlayer.seekTo(0);
                    seekBar.setProgress(0);
                    seekBarHandler.removeCallbacks(updateSeekBar); // Stop updating seek bar progress
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private class SenderFileViewHolder extends RecyclerView.ViewHolder {
        private final TextView fileName;
        private final TextView time;
        private final ImageView status;
        private final TextView fileInformation;
        private final FrameLayout clickerResend;
        private final ImageView fileIcon;

        public SenderFileViewHolder(View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.msg_file_name);
            time = itemView.findViewById(R.id.message_time);
            status = itemView.findViewById(R.id.msg_indicator);
            fileInformation = itemView.findViewById(R.id.msg_file_info);
            clickerResend = itemView.findViewById(R.id.clicker_resend);
            fileIcon = itemView.findViewById(R.id.file_icon);
        }

        @RequiresApi(api = Build.VERSION_CODES.Q)
        public void bind(EntityMessage oneMessage) {
            itemView.setOnLongClickListener(view -> {
                chatMenu.showMenu(oneMessage, itemView, true);
                return true;
            });
            FileFormatUtil.getFileFormatFromUrl(oneMessage.getAttachment().getFileUrl(), fileFormat -> updateIconBasedOnFileFormat(fileFormat));
            Log.e(TAG, "bind: " + oneMessage.getAttachment().getFileName());
            fileName.setText(oneMessage.getAttachment().getFileName());
            fileInformation.setText(String.valueOf((oneMessage.getAttachment().getSize() / 1000)) + "KB");
            time.setText(normalTime(oneMessage.getCreatedAt()));
            clickerResend.setOnClickListener(view -> {
                useCaseChatRoom.showDialog(context, oneMessage);
            });
            if (oneMessage.getStatus() != null && !oneMessage.getStatus().isEmpty()) {
                switch (oneMessage.getStatus()) {
                    case MESSAGE_SENDING:
                        status.setVisibility(View.GONE);
                        break;
                    case MESSAGE_UN_SEND:
                        status.setVisibility(View.VISIBLE);
                        status.setImageResource(R.drawable.ic_error_message);
                        break;
                    case MESSAGE_SENT:
                        status.setVisibility(View.VISIBLE);
                        status.setImageResource(R.drawable.ic_msg_indicator_sent);
                        break;
                    case MESSAGE_DELIVERED:
                        status.setVisibility(View.VISIBLE);
                        status.setImageResource(R.drawable.ic_message_received);
                        break;
                    case MESSAGE_READ:
                        status.setVisibility(View.VISIBLE);
                        status.setImageResource(R.drawable.ic_msg_indicator_read);
                        break;
                }
            }
        }

        private void updateIconBasedOnFileFormat(String fileFormat) {
            switch (fileFormat) {
                case PDF:
                    fileIcon.setImageResource(R.drawable.icon_pdf);
                    break;
                case WORD:
                    fileIcon.setImageResource(R.drawable.ic_docx);
                    break;
                case EXCEL:
                    fileIcon.setImageResource(R.drawable.icon_xls);
                    break;
                case POWER_POINT:
                    fileIcon.setImageResource(R.drawable.icon_ppt);
                    break;
                default:
                    fileIcon.setImageResource(R.drawable.ic_documenyt_file);
            }
        }

    }

    private class ReceivedFileViewHolder extends RecyclerView.ViewHolder {
        private final TextView fileName;
        private final TextView time;
        private final TextView fileInformation;
        private final LinearLayout senderGroupLayout;
        private final RoundedImageView senderGroupAvatar;
        private final TextView senderGroupName;
        private final ImageView fileIcon;


        public ReceivedFileViewHolder(View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.msg_file_name);
            time = itemView.findViewById(R.id.message_time);
            fileInformation = itemView.findViewById(R.id.file_msg_rec_info);
            senderGroupLayout = itemView.findViewById(R.id.layout_sender_group);
            senderGroupAvatar = itemView.findViewById(R.id.group_sender_avatar);
            senderGroupName = itemView.findViewById(R.id.name_sender_group);
            fileIcon = itemView.findViewById(R.id.icon_file);
        }

        @RequiresApi(api = Build.VERSION_CODES.Q)
        public void bind(EntityMessage oneMessage, int type) {
            if (type == AdapterChatContact.GROUP) {
                senderGroupLayout.setVisibility(View.VISIBLE);
                Picasso.get().load(BASE_PHOTO + oneMessage.getAuthor().getAvatarAuthor()).placeholder(R.color.primary).into(senderGroupAvatar);
                senderGroupName.setText(oneMessage.getAuthor().getPersonalDataAuthor().getName() + "  " + oneMessage.getAuthor().getPersonalDataAuthor().getSurname());
            } else {
                senderGroupLayout.setVisibility(View.GONE);
            }
            fileInformation.setText(String.valueOf((oneMessage.getAttachment().getSize() / 1000)) + "KB");
            fileName.setText(oneMessage.getAttachment().getFileName());
            FileFormatUtil.getFileFormatFromUrl(oneMessage.getAttachment().getFileUrl(), fileFormat -> updateIconBasedOnFileFormat(fileFormat));


            itemView.setOnLongClickListener(view -> {
                chatMenu.showMenu(oneMessage, itemView, false);
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

        private void updateIconBasedOnFileFormat(String fileFormat) {
            switch (fileFormat) {
                case PDF:
                    fileIcon.setImageResource(R.drawable.icon_pdf);
                    break;
                case WORD:
                    fileIcon.setImageResource(R.drawable.ic_docx);
                    break;
                case EXCEL:
                    fileIcon.setImageResource(R.drawable.icon_xls);
                    break;
                case POWER_POINT:
                    fileIcon.setImageResource(R.drawable.icon_ppt);
                    break;
                default:
                    fileIcon.setImageResource(R.drawable.ic_documenyt_file);
            }
        }
    }

    private class SendImageViewHolder extends RecyclerView.ViewHolder {
        private final RoundedImageView image;
        private final ImageView status;
        private final TextView imageSentTime;
        private final TextView imageSentSize;
        private final FrameLayout clickerResend;

        public SendImageViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            status = itemView.findViewById(R.id.msg_indicator);
            imageSentTime = itemView.findViewById(R.id.img_sent_time);
            imageSentSize = itemView.findViewById(R.id.img_size);
            clickerResend = itemView.findViewById(R.id.clicker_resend);
        }

        @RequiresApi(api = Build.VERSION_CODES.Q)
        public void bind(EntityMessage oneMessage) {
            itemView.setOnLongClickListener(view -> {
                chatMenu.showMenu(oneMessage, itemView, true);
                return true;
            });
            itemView.setOnClickListener(view -> {
                itemView.setEnabled(false);
                addFragment(mainFragmentManager, R.id.main_content, FragmentPhotoItem.newInstance(oneMessage.getAttachment().getFileUrl()));
                new Handler().postDelayed(() -> itemView.setEnabled(true), 200);
            });
            Picasso.get().load(BASE_URL + "/" + oneMessage.getAttachment().getFileUrl()).placeholder(getPlaceholder(context)).into(image);
            imageSentTime.setText(normalTime(oneMessage.getCreatedAt()));
            if (oneMessage.getAttachment().getSize() != 0) {
                imageSentSize.setText(String.valueOf(oneMessage.getAttachment().getSize() / 1000) + "KB");
            }
            clickerResend.setOnClickListener(view -> {
                useCaseChatRoom.showDialog(context, oneMessage);
            });
            if (oneMessage.getStatus() != null && !oneMessage.getStatus().isEmpty()) {
                switch (oneMessage.getStatus()) {
                    case MESSAGE_SENDING:
                        status.setVisibility(View.GONE);
                        break;
                    case MESSAGE_UN_SEND:
                        status.setVisibility(View.VISIBLE);
                        status.setImageResource(R.drawable.ic_error_message);
                        break;
                    case MESSAGE_SENT:
                        status.setVisibility(View.VISIBLE);
                        status.setImageResource(R.drawable.ic_msg_indicator_sent);
                        break;
                    case MESSAGE_DELIVERED:
                        status.setVisibility(View.VISIBLE);
                        status.setImageResource(R.drawable.ic_message_received);
                        break;
                    case MESSAGE_READ:
                        status.setVisibility(View.VISIBLE);
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
        private final LinearLayout senderGroupLayout;
        private final RoundedImageView senderGroupAvatar;
        private final TextView senderGroupName;

        public ReceivedImageViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            imageSentTime = itemView.findViewById(R.id.img_sent_time);
            imageSentSize = itemView.findViewById(R.id.img_size);
            senderGroupLayout = itemView.findViewById(R.id.layout_sender_group);
            senderGroupAvatar = itemView.findViewById(R.id.group_sender_avatar);
            senderGroupName = itemView.findViewById(R.id.name_sender_group);
        }

        @RequiresApi(api = Build.VERSION_CODES.Q)
        @SuppressLint("SetTextI18n")
        public void bind(EntityMessage oneMessage, int type) {
            if (type == AdapterChatContact.GROUP) {
                senderGroupLayout.setVisibility(View.VISIBLE);
                Picasso.get().load(BASE_PHOTO + oneMessage.getAuthor().getAvatarAuthor()).placeholder(getPlaceholder(context)).into(senderGroupAvatar);
                senderGroupName.setText(oneMessage.getAuthor().getPersonalDataAuthor().getName() + "  " + oneMessage.getAuthor().getPersonalDataAuthor().getSurname());
            } else {
                senderGroupLayout.setVisibility(View.GONE);
            }
            itemView.setOnClickListener(view -> {
                itemView.setEnabled(false);
                addFragment(mainFragmentManager, R.id.main_content, FragmentPhotoItem.newInstance(oneMessage.getAttachment().getFileUrl()));
                new Handler().postDelayed(() -> itemView.setEnabled(true), 200);
            });
            itemView.setOnLongClickListener(view -> {
                chatMenu.showMenu(oneMessage, itemView, false);
                return true;
            });
            Picasso.get().load(BASE_URL + "/" + oneMessage.getAttachment().getFileUrl()).placeholder(getPlaceholder(context)).into(image);
            imageSentTime.setText(normalTime(oneMessage.getCreatedAt()));
            Integer size = oneMessage.getAttachment().getSize();
            if (size != 0) {
                // The size is available and not equal to zero
                imageSentSize.setText(String.valueOf(size / 1000) + "KB");
            }
        }
    }

    private class DateViewHolder extends RecyclerView.ViewHolder {
        private final TextView date;

        public DateViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
        }

        public void bind(EntityMessage oneMessage) {
            date.setText(oneMessage.getText());

        }
    }


}