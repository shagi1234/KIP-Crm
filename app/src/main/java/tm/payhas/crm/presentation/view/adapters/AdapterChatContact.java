package tm.payhas.crm.presentation.view.adapters;

import static tm.payhas.crm.data.remote.api.network.Network.BASE_PHOTO;
import static tm.payhas.crm.data.remote.api.network.Network.BASE_URL;
import static tm.payhas.crm.domain.helpers.Common.normalTime;
import static tm.payhas.crm.domain.helpers.StaticMethods.hideSoftKeyboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
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
import java.util.List;

import tm.payhas.crm.R;
import tm.payhas.crm.data.localdb.entity.EntityUserInfo;
import tm.payhas.crm.data.localdb.entity.EntityGroup;
import tm.payhas.crm.presentation.view.activity.ActivityMain;
import tm.payhas.crm.presentation.view.fragment.FragmentChatRoom;
import tm.payhas.crm.domain.helpers.Common;

public class AdapterChatContact extends RecyclerView.Adapter<AdapterChatContact.ViewHolder> {
    private Activity activity;
    private final int type;
    public static final int GROUP = 2;
    public static final int PRIVATE = 1;
    private List<EntityUserInfo> privateUserList = new ArrayList<>();
    private List<EntityGroup> groups = new ArrayList<>();

    public AdapterChatContact(int type) {
        this.type = type;
    }


    @SuppressLint("NotifyDataSetChanged")
    public void setPrivateUserList(List<EntityUserInfo> privateUserList) {
        this.privateUserList = privateUserList;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setGroups(List<EntityGroup> groups) {
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
            EntityUserInfo userInfo = privateUserList.get(position);
            holder.bind(userInfo, position);
        } else {
            EntityGroup entityGroup = groups.get(position);
            holder.bindGroup(entityGroup, position);
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
        private final RoundedImageView contactImage;
        private final LinearLayout contactChatMain;
        private final TextView contactName;
        private final TextView contactChat;
        private final TextView contactChatTime;
        private final TextView contactChatCount;
        private final RoundedImageView onlineIndicator;

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

        public void bind(EntityUserInfo userInfo, int position) {
            setInfo(userInfo);
            setListeners(position);
        }

        private void setListeners(int position) {
            contactChatMain.setOnClickListener(view -> {
                EntityUserInfo privateUser1 = privateUserList.get(position);
                contactChatMain.setEnabled(false);
                hideSoftKeyboard(activity);
                int id = privateUser1.getId();
                int roomId = privateUser1.getMessageRoom().getRoomIdRoom();
                Common.addFragment(ActivityMain.mainFragmentManager, R.id.main_content, FragmentChatRoom.newInstance(roomId, id, PRIVATE));
                new Handler().postDelayed(() -> contactChatMain.setEnabled(true), 200);
            });
        }

        @SuppressLint("SetTextI18n")
        private void setInfo(EntityUserInfo privateUser) {
            if (privateUser.isActive()) onlineIndicator.setVisibility(View.VISIBLE);
            else onlineIndicator.setVisibility(View.GONE);
            contactName.setText(privateUser.getPersonalData().getName() + " " + privateUser.getPersonalData().getLastName());
            if (privateUser.getMessageRoom().getCreatedAtRoom() != null)
                contactChatCount.setText(normalTime(privateUser.getMessageRoom().getCreatedAtRoom()));
            else contactChatTime.setText(normalTime(privateUser.getLastActivity()));


            contactChat.setText(privateUser.getMessageRoom().getTextRoom());
            if (privateUser.getMessageRoom().getRoom().getCount().getMessages() != 0) {
                contactChatCount.setVisibility(View.VISIBLE);
                contactChatCount.setText(String.valueOf(privateUser.getMessageRoom().getRoom().getCount().getMessages()));
            } else {
                contactChatCount.setVisibility(View.GONE); // Hide if count is 0
            }

            Picasso.get().load(BASE_URL + "/" + privateUser.getAvatar()).placeholder(R.color.primary).into(contactImage);

        }

        public void bindGroup(EntityGroup entityGroup, int position) {
            setInfoGroup(entityGroup);
            setListenersGroup(position);
        }

        private void setListenersGroup(int position) {
            contactChatMain.setOnClickListener(view -> {
                EntityGroup oneGroup = groups.get(position);
                contactChatMain.setEnabled(false);
                hideSoftKeyboard(activity);
                int id = oneGroup.getId();
                int roomId = oneGroup.getMessageRoom().getRoomIdRoom();
                Common.addFragment(ActivityMain.mainFragmentManager, R.id.main_content, FragmentChatRoom.newInstance(roomId, id, GROUP));
                new Handler().postDelayed(() -> contactChatMain.setEnabled(true), 200);
            });
        }

        @SuppressLint("SetTextI18n")
        private void setInfoGroup(EntityGroup group) {
            Picasso.get().load(BASE_PHOTO + group.getAvatar()).placeholder(R.color.primary).into(contactImage);
            contactName.setText(group.getName());
            contactChatTime.setText(normalTime(group.getMessageRoom().getCreatedAtRoom()));
            contactChat.setText(String.valueOf(group.getMessageRoom().getRoom().getCount().getParticipants()) + " participants");
            if (group.getMessageRoom().getRoom().getCount().getMessages() == 0) {
                contactChatCount.setVisibility(View.GONE);
            } else {
                contactChatCount.setVisibility(View.VISIBLE);
                contactChatCount.setText(String.valueOf(group.getMessageRoom().getRoom().getCount().getMessages()));
            }
        }
    }
}
