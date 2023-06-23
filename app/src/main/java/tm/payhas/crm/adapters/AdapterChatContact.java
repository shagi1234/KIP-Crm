package tm.payhas.crm.adapters;

import static tm.payhas.crm.activity.ActivityMain.mainFragmentManager;
import static tm.payhas.crm.api.network.Network.BASE_PHOTO;
import static tm.payhas.crm.api.network.Network.BASE_URL;
import static tm.payhas.crm.helpers.Common.normalTime;
import static tm.payhas.crm.helpers.StaticMethods.hideSoftKeyboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import tm.payhas.crm.R;
import tm.payhas.crm.api.data.dto.DtoUserInfo;
import tm.payhas.crm.dataModels.DataGroup;
import tm.payhas.crm.fragment.FragmentChatRoom;
import tm.payhas.crm.helpers.Common;
import tm.payhas.crm.helpers.StaticMethods;

public class AdapterChatContact extends RecyclerView.Adapter<AdapterChatContact.ViewHolder> {

    private Context context;
    private Activity activity;
    private int type;
    public static final int GROUP = 2;
    public static final int PRIVATE = 1;
    private ArrayList<DtoUserInfo> privateUserList = new ArrayList<>();
    private ArrayList<DataGroup> groups = new ArrayList<>();

    public AdapterChatContact(Context context, int type) {
        this.context = context;
        this.type = type;
    }


    @SuppressLint("NotifyDataSetChanged")
    public void setPrivateUserList(ArrayList<DtoUserInfo> privateUserList) {
        this.privateUserList = privateUserList;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setGroups(ArrayList<DataGroup> groups) {
        this.groups = groups;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_contact, parent, false);
        return new ViewHolder(view);
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (type == PRIVATE) {
            holder.bind();
        } else {
            holder.bindGroup();
        }


    }

    @Override
    public int getItemCount() {
        if (type == PRIVATE) {
            return privateUserList.size();
        } else {
            return groups.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView contactImage;
        private LinearLayout contactChatMain;
        private TextView contactName;
        private TextView contactChat;
        private TextView contactChatTime;
        private TextView contactChatCount;
        private RoundedImageView onlineIndicator;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contactImage = itemView.findViewById(R.id.contact_image);
            contactChatMain = itemView.findViewById(R.id.contact_chat_main);
            contactChat = itemView.findViewById(R.id.contact_chat);
            contactChatTime = itemView.findViewById(R.id.contact_time);
            contactName = itemView.findViewById(R.id.contact_name);
            contactChatCount = itemView.findViewById(R.id.contact_chat_count);
            onlineIndicator = itemView.findViewById(R.id.online_indicator);
        }

        public void bind() {
            setInfo();
            StaticMethods.setBackgroundDrawable(context, contactChatCount, R.color.primary, 0, 50, false, 0);
        }

        private void setInfo() {
            DtoUserInfo privateUser = privateUserList.get(getAdapterPosition());
            if (privateUser.isActive())
                onlineIndicator.setVisibility(View.VISIBLE);
            else onlineIndicator.setVisibility(View.GONE);

            contactName.setText(privateUser.getPersonalData().getName());
            contactChatTime.setText(normalTime(privateUser.getLastActivity()));
            contactChat.setText(privateUser.getMessageRoom().getText());
            if (privateUser.getMessageRoom().getRoom().getCount().getMessages() == 0) {
                contactChatCount.setVisibility(View.GONE);
            } else {
                contactChatCount.setText(String.valueOf(privateUser.getMessageRoom().getRoom().getCount().getMessages()));
            }
            Picasso.get().load(BASE_URL + "/" + privateUser.getAvatar()).placeholder(R.color.primary).into(contactImage);
            contactChatMain.setOnClickListener(view -> {
                DtoUserInfo privateUser1 = privateUserList.get(getAdapterPosition());
                contactChatMain.setEnabled(false);
                hideSoftKeyboard(activity);
                int id = privateUser1.getId();
                int roomId = privateUser1.getMessageRoom().getRoomId();
                String userName = privateUser1.getPersonalData().getName();
                String avatarUrl = privateUser1.getAvatar();
                String lastActivity = privateUser1.getLastActivity();
                boolean isActive = privateUser1.isActive();
                Log.e("Adapter", "setInfo: " + roomId);
                Common.addFragment(mainFragmentManager, R.id.main_content, FragmentChatRoom.newInstance(roomId, id, userName, avatarUrl, lastActivity, isActive, PRIVATE, 0));
                new Handler().postDelayed(() -> contactChatMain.setEnabled(true), 200);
            });
        }

        public void bindGroup() {
            DataGroup group = groups.get(getAdapterPosition());
            setInfoGroup(group);
        }

        private void setInfoGroup(DataGroup group) {
            Picasso.get().load(BASE_PHOTO + group.getAvatar()).placeholder(R.color.primary).into(contactImage);
            contactName.setText(group.getName());
            contactChatTime.setText(normalTime(group.getMessageRoom().getCreatedAt()));
            contactChat.setText(String.valueOf(group.getMessageRoom().getRoom().getCount().getParticipants()) + " participants");
            if (group.getMessageRoom().getRoom().getCount().getMessages() == 0) {
                contactChatCount.setVisibility(View.GONE);
            } else {
                contactChatCount.setText(String.valueOf(group.getMessageRoom().getRoom().getCount().getMessages()));
            }
            contactChatMain.setOnClickListener(view -> {
                DataGroup oneGroup = groups.get(getAdapterPosition());
                contactChatMain.setEnabled(false);
                hideSoftKeyboard(activity);
                int id = oneGroup.getId();
                int roomId = oneGroup.getMessageRoom().getRoomId();
                String userName = oneGroup.getName();
                String avatarUrl = oneGroup.getAvatar();
                String lastActivity = "";
                boolean isActive = false;
                int memberCount = oneGroup.getMessageRoom().getRoom().getCount().getParticipants();
                Log.e("Adapter", "setInfo: " + roomId);
                Common.addFragment(mainFragmentManager, R.id.main_content, FragmentChatRoom.newInstance(roomId, id, userName, avatarUrl, lastActivity, false, GROUP, memberCount));
                new Handler().postDelayed(() -> contactChatMain.setEnabled(true), 200);
            });

        }
    }
}
