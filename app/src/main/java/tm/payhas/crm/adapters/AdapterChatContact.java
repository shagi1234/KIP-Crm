package tm.payhas.crm.adapters;

import static tm.payhas.crm.activity.ActivityMain.mainFragmentManager;
import static tm.payhas.crm.helpers.StaticMethods.hideSoftKeyboard;

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

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import tm.payhas.crm.R;
import tm.payhas.crm.api.data.dto.DtoUserInfo;
import tm.payhas.crm.fragment.FragmentChatRoom;
import tm.payhas.crm.helpers.Common;
import tm.payhas.crm.helpers.StaticMethods;

public class AdapterChatContact extends RecyclerView.Adapter<AdapterChatContact.ViewHolder> {

    private Context context;
    private Activity activity;
    private ArrayList<DtoUserInfo> privateUserList = new ArrayList<>();

    public AdapterChatContact(Context context) {
        this.context = context;
    }


    public void setPrivateUserList(ArrayList<DtoUserInfo> privateUserList) {
        this.privateUserList = privateUserList;
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
        holder.bind();

    }

    @Override
    public int getItemCount() {
        return privateUserList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView contactImage;
        private LinearLayout contactChatMain;
        private TextView contactName;
        private TextView contactChat;
        private TextView contactChatTime;
        private TextView contactChatCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contactImage = itemView.findViewById(R.id.contact_image);
            contactChatMain = itemView.findViewById(R.id.contact_chat_main);
            contactChat = itemView.findViewById(R.id.contact_chat);
            contactChatTime = itemView.findViewById(R.id.contact_time);
            contactName = itemView.findViewById(R.id.contact_name);
            contactChatCount = itemView.findViewById(R.id.contact_chat_count);
        }

        public void bind() {
            setInfo();
            StaticMethods.setBackgroundDrawable(context, contactChatCount, R.color.primary, 0, 50, false, 0);

        }

        private void setInfo() {
            DtoUserInfo privateUser = privateUserList.get(getAdapterPosition());
            contactName.setText(privateUser.getPersonalData().getName());
            contactChatTime.setText(privateUser.getLastActivity());
            contactChat.setText(privateUser.getMessageRoom().getText());
            if (privateUser.getMessageRoom().getRoom().getCount().getMessages() == 0) {
                contactChatCount.setVisibility(View.GONE);
            } else {
                contactChatCount.setText(String.valueOf(privateUser.getMessageRoom().getRoom().getCount().getMessages()));
            }
            Glide.with(context).load(privateUser.getAvatar()).placeholder(R.color.primary).into(contactImage);
            contactChatMain.setOnClickListener(view -> {
                DtoUserInfo privateUser1 = privateUserList.get(getAdapterPosition());
                contactChatMain.setEnabled(false);
                hideSoftKeyboard(activity);
                int id = privateUser1.getId();
                int roomId = privateUser1.getMessageRoom().getRoomId();
                Log.e("Adapter", "setInfo: " + roomId);
                Common.addFragment(mainFragmentManager, R.id.main_content, FragmentChatRoom.newInstance(roomId, id));
                new Handler().postDelayed(() -> contactChatMain.setEnabled(true), 200);
            });
        }
    }
}
