package tm.payhas.crm.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tm.payhas.crm.R;
import tm.payhas.crm.model.ModelMessage;

public class AdapterSingleChat extends RecyclerView.Adapter {
    private ArrayList<ModelMessage> messages = new ArrayList<>();
    private Context context;
    private Activity activity;

    public AdapterSingleChat(Context context) {
        this.context = context;
    }

    public void setMessages(ArrayList<ModelMessage> messages) {
        this.messages = messages;
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
        switch (messages.get(position).getViewType()) {
            case 1:
                return LAYOUT_SEND_MESSAGE;
            case 2:
                return LAYOUT_RECEIVED_MESSAGE;
            case 3:
                return LAYOUT_SEND_VOICE_MESSAGE;
            case 4:
                return LAYOUT_RECEIVED_VOICE_MESSAGE;
            case 5:
                return LAYOUT_SEND_FILE;
            case 6:
                return LAYOUT_RECEIVED_FILE;
            case 7:
                return LAYOUT_SEND_IMAGE;
            case 8:
                return LAYOUT_RECEIVED_IMAGE;
            case 9:
                return LAYOUT_SEND_REPLY;
            case 10:
                return LAYOUT_RECEIVED_REPLY;
            default:
                return -1;

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
            default:
                return null;

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (messages.get(position).getViewType()) {
            case LAYOUT_SEND_MESSAGE:
                ((SenderMessageViewHolder) holder).bind();
                break;
            case LAYOUT_RECEIVED_MESSAGE:
                ModelMessage receivedMsg = messages.get(position);
                ((ReceivedMessageViewHolder) holder).msgReceived.setText(receivedMsg.getMessage());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class SenderMessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView msgSent;

        public SenderMessageViewHolder(View itemView) {
            super(itemView);
            msgSent = itemView.findViewById(R.id.message_sent);
        }

        public void bind() {
            ModelMessage oneMessage = messages.get(getAdapterPosition());
            msgSent.setText(oneMessage.getMessage());

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @SuppressLint("RestrictedApi")
                @Override
                public boolean onLongClick(View view) {
                    @SuppressLint("RestrictedApi")
                    MenuBuilder menuBuilder = new MenuBuilder(context);
                    MenuInflater menuInflater = new MenuInflater(context);
                    menuInflater.inflate(R.menu.context_menu, menuBuilder);
                    MenuPopupHelper popupHelper = new MenuPopupHelper(context, menuBuilder, itemView);
                    popupHelper.setForceShowIcon(true);
                    popupHelper.show(300, 0);
                    return true;
                }
            });
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
