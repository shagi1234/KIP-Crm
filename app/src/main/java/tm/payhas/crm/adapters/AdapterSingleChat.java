package tm.payhas.crm.adapters;

import static tm.payhas.crm.statics.StaticConstants.DATE;
import static tm.payhas.crm.statics.StaticConstants.FILE;
import static tm.payhas.crm.statics.StaticConstants.MESSAGE_DELIVERED;
import static tm.payhas.crm.statics.StaticConstants.MESSAGE_READ;
import static tm.payhas.crm.statics.StaticConstants.MESSAGE_SENT;
import static tm.payhas.crm.statics.StaticConstants.MESSAGE_UN_SEND;
import static tm.payhas.crm.statics.StaticConstants.PHOTO;
import static tm.payhas.crm.statics.StaticConstants.STRING;
import static tm.payhas.crm.statics.StaticConstants.VOICE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import tm.payhas.crm.R;
import tm.payhas.crm.dataModels.DataMessageTarget;
import tm.payhas.crm.interfaces.NewMessage;
import tm.payhas.crm.preference.AccountPreferences;

public class AdapterSingleChat extends RecyclerView.Adapter implements NewMessage {
    private ArrayList<DataMessageTarget> messages = new ArrayList<>();
    private Context context;
    private Activity activity;
    private Integer authorId = 0;


    public AdapterSingleChat(Context context) {
        this.context = context;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
        notifyDataSetChanged();
    }

    public void setMessages(ArrayList<DataMessageTarget> messages) {
        this.messages = messages;
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
                    return LAYOUT_SEND_MESSAGE;
                else
                    return LAYOUT_RECEIVED_MESSAGE;
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
                if (oneMessage.getAuthorId() == authorId)
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
                return new ReceivedImageViewHolder(layout9);
            case LAYOUT_RECEIVED_REPLY:
                View layout10 = LayoutInflater.from(context).inflate(R.layout.item_reply_recieved, parent, false);
                return new ReceivedImageViewHolder(layout10);
            case LAYOUT_DATE:
                View layout11 = LayoutInflater.from(context).inflate(R.layout.item_date, parent, false);
                return new ReceivedImageViewHolder(layout11);
            default:
                return null;

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DataMessageTarget oneMessage = messages.get(position);
        Log.e("NEW_MESSAGE_ADAPTER", "onBindViewHolder: " + oneMessage.getText());

        switch (oneMessage.getType()) {
            case STRING:
                if (oneMessage.getAuthorId() == authorId)
                    ((SenderMessageViewHolder) holder).bind(oneMessage);
                else
                    ((ReceivedMessageViewHolder) holder).bind(oneMessage);
//            case VOICE:
//                if (oneMessage.getAuthorId() == authorId)
//                    ((SenderVoiceViewHolder) holder).bind(oneMessage);
//                else
//                    ((ReceivedVoiceViewHolder) holder).bind(oneMessage);
//            case PHOTO:
//                if (oneMessage.getAuthorId() == authorId)
//                    ((SendImageViewHolder) holder).bind(oneMessage);
//                else
//                    ((ReceivedImageViewHolder) holder).bind(oneMessage);
//            case FILE:
//                if (oneMessage.getAuthorId() == authorId)
//                    ((SenderFileViewHolder) holder).bind(oneMessage);
//                else
//                    ((ReceivedFileViewHolder) holder).bind(oneMessage);
//            case DATE:
//                if (oneMessage.getAuthorId() == authorId)
//                    ((DateViewHolder) holder).bind(oneMessage);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public void onNewMessage(DataMessageTarget dataMessageTarget) {
        Toast.makeText(context, dataMessageTarget.getText(), Toast.LENGTH_SHORT).show();
        messages.add(dataMessageTarget);
        notifyDataSetChanged();
        Log.e("Adapter", "onNewMessage: " + "added");
    }

    private static class SenderMessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView msgSent;
        private final TextView time;
        private final ImageView status;

        public SenderMessageViewHolder(View itemView) {
            super(itemView);
            msgSent = itemView.findViewById(R.id.message_sent);
            time = itemView.findViewById(R.id.message_time);
            status = itemView.findViewById(R.id.msg_indicator);
        }

        public void bind(DataMessageTarget messageTarget) {
            msgSent.setText(messageTarget.getText());
            time.setText(messageTarget.getCreatedAt());
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

    private class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView msgReceived;
        private final TextView time;

        public ReceivedMessageViewHolder(View itemView) {
            super(itemView);
            msgReceived = itemView.findViewById(R.id.message_received);
            time = itemView.findViewById(R.id.message_time);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @SuppressLint("RestrictedApi")
                @Override
                public boolean onLongClick(View view) {
                    MenuBuilder menuBuilder = new MenuBuilder(context);
                    MenuInflater menuInflater = new MenuInflater(context);
                    menuInflater.inflate(R.menu.context_menu, menuBuilder);
                    MenuPopupHelper popupHelper = new MenuPopupHelper(context, menuBuilder, itemView);
                    popupHelper.setForceShowIcon(true);
                    popupHelper.show();
                    return true;
                }
            });
        }


        public void bind(DataMessageTarget messageTarget) {
            msgReceived.setText(messageTarget.getText());
            time.setText(messageTarget.getCreatedAt());
        }
    }

    private static class SenderVoiceViewHolder extends RecyclerView.ViewHolder {
        private final TextView voiceInformation;
        private final ImageView status;
        private final TextView time;

        public SenderVoiceViewHolder(View itemView) {
            super(itemView);
            voiceInformation = itemView.findViewById(R.id.voice_message_info);
            status = itemView.findViewById(R.id.msg_indicator);
            time = itemView.findViewById(R.id.message_time);
        }

        public void bind(DataMessageTarget oneMessage) {
            voiceInformation.setText(oneMessage.getAttachment().getDuration() + "," + oneMessage.getAttachment().getSize());
            time.setText(oneMessage.getCreatedAt());
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

    private class ReceivedVoiceViewHolder extends RecyclerView.ViewHolder {
        private final TextView voiceInformation;
        private final TextView time;

        public ReceivedVoiceViewHolder(View itemView) {
            super(itemView);
            voiceInformation = itemView.findViewById(R.id.voice_message_info);
            time = itemView.findViewById(R.id.message_time);
        }

        public void bind(DataMessageTarget oneMessage) {
            voiceInformation.setText(oneMessage.getAttachment().getDuration() + "," + oneMessage.getAttachment().getSize());
            time.setText(oneMessage.getCreatedAt());
        }
    }

    private class SenderFileViewHolder extends RecyclerView.ViewHolder {
        private final TextView fileName;
        private final TextView time;
        private final ImageView status;
        private final TextView fileInformation;

        public SenderFileViewHolder(View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.msg_file_name);
            time = itemView.findViewById(R.id.message_time);
            status = itemView.findViewById(R.id.msg_indicator);
            fileInformation = itemView.findViewById(R.id.msg_file_info);
        }

        public void bind(DataMessageTarget oneMessage) {
            fileName.setText(oneMessage.getAttachment().getFileName());
            fileInformation.setText(oneMessage.getAttachment().getDuration() + "," + oneMessage.getAttachment().getSize());
            time.setText(oneMessage.getCreatedAt());
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

    private class ReceivedFileViewHolder extends RecyclerView.ViewHolder {
        private final TextView fileName;
        private final TextView time;
        private final TextView fileInformation;

        public ReceivedFileViewHolder(View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.msg_file_name);
            time = itemView.findViewById(R.id.message_time);
            fileInformation = itemView.findViewById(R.id.msg_file_info);
        }

        public void bind(DataMessageTarget oneMessage) {
            fileName.setText(oneMessage.getAttachment().getFileName());
            fileInformation.setText(oneMessage.getAttachment().getDuration() + "," + oneMessage.getAttachment().getSize());
            time.setText(oneMessage.getCreatedAt());
        }
    }

    private class SendImageViewHolder extends RecyclerView.ViewHolder {
        private final ImageView image;
        private final ImageView status;
        private final TextView imageSentTime;
        private final TextView imageSentSize;

        public SendImageViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            status = itemView.findViewById(R.id.msg_indicator);
            imageSentTime = itemView.findViewById(R.id.img_sent_time);
            imageSentSize = itemView.findViewById(R.id.img_size);
        }

        public void bind(DataMessageTarget oneMessage) {
            Glide.with(context).load(oneMessage.getAttachment().getFileUrl()).placeholder(R.color.primary).into(image);
            imageSentTime.setText(oneMessage.getCreatedAt());
            imageSentSize.setText(oneMessage.getAttachment().getSize());
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

    private class ReceivedImageViewHolder extends RecyclerView.ViewHolder {
        private final ImageView image;
        private final TextView imageSentTime;
        private final TextView imageSentSize;

        public ReceivedImageViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            imageSentTime = itemView.findViewById(R.id.img_sent_time);
            imageSentSize = itemView.findViewById(R.id.img_size);
        }

        public void bind(DataMessageTarget oneMessage) {
            Glide.with(context).load(oneMessage.getAttachment().getFileUrl()).placeholder(R.color.primary).into(image);
            imageSentTime.setText(oneMessage.getCreatedAt());
            imageSentSize.setText(oneMessage.getAttachment().getSize());
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
