package tm.payhas.crm.adapters;

import static tm.payhas.crm.statics.StaticConstants.FILE;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tm.payhas.crm.R;
import tm.payhas.crm.api.data.dto.subClassesUserInfo.DataRoomChat;
import tm.payhas.crm.dataModels.DataMessageTarget;
import tm.payhas.crm.interfaces.NewMessage;

public class AdapterSingleChat extends RecyclerView.Adapter implements NewMessage {
    private ArrayList<DataRoomChat> messages = new ArrayList<>();
    private Context context;
    private Activity activity;

    public AdapterSingleChat(Context context) {
        this.context = context;
    }

    public void setMessages(ArrayList<DataRoomChat> messages) {
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

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public int getItemViewType(int position) {
        DataMessageTarget oneMessage = messages.get(position).getMessages().get(position);

        if (oneMessage.getType().equals(STRING)) {
            return LAYOUT_SEND_MESSAGE;
        } else if (oneMessage.getType().equals(PHOTO)) {
            return LAYOUT_SEND_IMAGE;
        } else if (oneMessage.getType().equals(VOICE)) {
            return LAYOUT_SEND_VOICE_MESSAGE;
        } else if (oneMessage.getType().equals(FILE)) {
            return LAYOUT_SEND_FILE;
        }
        return LAYOUT_SEND_MESSAGE;
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
            default:
                return null;

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DataMessageTarget oneMessage = messages.get(position).getMessages().get(position);
        if (oneMessage.getType().equals(STRING)) {
            ((SenderMessageViewHolder) holder).bind(oneMessage.getText());
        } else if (oneMessage.getType().equals(PHOTO)) {
            ((ReceivedMessageViewHolder) holder).bind();
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public void onNewMessage(DataMessageTarget dataMessageTarget) {
        Toast.makeText(context, dataMessageTarget.getText(), Toast.LENGTH_SHORT).show();
//        ArrayList<DataMessageTarget> oneMessageList = new ArrayList<>();
//        oneMessageList.add(dataMessageTarget);
//        DataRoomChat dataRoomChat = new DataRoomChat();
//        dataRoomChat.setChatData("54455454");
//        dataRoomChat.setMessages(oneMessageList);
//        messages.add(dataRoomChat);
        notifyDataSetChanged();
        Log.e("Adapter", "onNewMessage: " + "added");
    }

    class SenderMessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView msgSent;

        public SenderMessageViewHolder(View itemView) {
            super(itemView);
            msgSent = itemView.findViewById(R.id.message_sent);
        }

        public void bind(String text) {
            msgSent.setText(text);
        }
    }

    private class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView msgReceived;

        public ReceivedMessageViewHolder(View itemView) {
            super(itemView);
            msgReceived = itemView.findViewById(R.id.message_received);
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


        public void bind() {
        }
    }

    private class SenderVoiceViewHolder extends RecyclerView.ViewHolder {
        public SenderVoiceViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class ReceivedVoiceViewHolder extends RecyclerView.ViewHolder {
        public ReceivedVoiceViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class SenderFileViewHolder extends RecyclerView.ViewHolder {
        public SenderFileViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class ReceivedFileViewHolder extends RecyclerView.ViewHolder {
        public ReceivedFileViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class SendImageViewHolder extends RecyclerView.ViewHolder {
        public SendImageViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class ReceivedImageViewHolder extends RecyclerView.ViewHolder {
        public ReceivedImageViewHolder(View itemView) {
            super(itemView);
        }
    }
}
