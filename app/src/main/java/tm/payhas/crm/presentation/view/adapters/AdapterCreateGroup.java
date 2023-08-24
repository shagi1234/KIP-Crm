package tm.payhas.crm.presentation.view.adapters;

import static tm.payhas.crm.data.remote.api.network.Network.BASE_PHOTO;
import static tm.payhas.crm.domain.helpers.Common.normalDate;
import static tm.payhas.crm.domain.helpers.Common.normalTime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import tm.payhas.crm.R;
import tm.payhas.crm.data.localdb.entity.EntityUserInfo;
import tm.payhas.crm.domain.interfaces.OnUserCountChangeListener;
import tm.payhas.crm.data.localdb.preference.AccountPreferences;

public class AdapterCreateGroup extends RecyclerView.Adapter<AdapterCreateGroup.ViewHolder> {

    private Context context;
    private ArrayList<EntityUserInfo> members = new ArrayList<>();
    private ArrayList<EntityUserInfo> selectedMembers = new ArrayList<>();
    private ArrayList<Integer> selectedUserList = new ArrayList<>();
    private OnUserCountChangeListener userCountChangeListener;


    public AdapterCreateGroup(Context context) {
        this.context = context;
    }

    public void setUserCountChangeListener(OnUserCountChangeListener listener) {
        this.userCountChangeListener = listener;
    }

    public ArrayList<EntityUserInfo> getSelectedMembers() {
        return selectedMembers;
    }

    public void setSelectedMembers(ArrayList<EntityUserInfo> selectedMembers) {
        this.selectedMembers = selectedMembers;
        notifyDataSetChanged();
    }

    public ArrayList<Integer> getSelectedUserList() {
        return selectedUserList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_create_group_member, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getId() == AccountPreferences.newInstance(context).getAuthorId()) {
                members.remove(i);
            }
        }
        EntityUserInfo oneUser = members.get(position);
        holder.bind(oneUser);
    }

    @Override
    public int getItemCount() {
        return members.size();
    }


    public void setMembers(ArrayList<EntityUserInfo> members) {
        this.members = members;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CheckBox checkbox;
        private TextView nameSurname;
        private RoundedImageView avatar;
        private TextView lastSeen;
        private LinearLayout mainClicker;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkbox = itemView.findViewById(R.id.checkbox_member);
            nameSurname = itemView.findViewById(R.id.name_member);
            lastSeen = itemView.findViewById(R.id.last_seen_member);
            avatar = itemView.findViewById(R.id.avatar_member);
            mainClicker = itemView.findViewById(R.id.main_clicker);
        }

        public void bind(EntityUserInfo oneUser) {
            setUserInfo(oneUser);
            if (oneUser.isSelected()) {
                setChecked(oneUser);
            } else {
                setUnChecked(oneUser);
            }
            mainClicker.setOnClickListener(view -> {
                if (oneUser.isSelected()) {
                    setUnChecked(oneUser);
                } else {
                    setChecked(oneUser);
                }
            });
        }

        private void setUserInfo(EntityUserInfo oneUser) {
            if (oneUser.getLastActivity() != null)
                lastSeen.setText(String.format("%s %s", normalDate(oneUser.getLastActivity()), normalTime(oneUser.getLastActivity())));
            nameSurname.setText(String.format("%s %s", oneUser.getPersonalData().getName(), oneUser.getPersonalData().getSurname()));
            Picasso.get().load(BASE_PHOTO + oneUser.getAvatar()).placeholder(R.color.primary).into(avatar);
        }

        public void setChecked(EntityUserInfo oneUser) {
            checkbox.setChecked(true);
            oneUser.setSelected(true);
            if (!selectedMembers.contains(oneUser)) {
                selectedMembers.add(oneUser);
                selectedUserList.add(oneUser.getId());
            }
            if (userCountChangeListener != null) {
                userCountChangeListener.onUserCountChange(String.valueOf(selectedMembers.size()) + "/" + String.valueOf(members.size()));
            }
        }

        public void setUnChecked(EntityUserInfo oneUser) {
            checkbox.setChecked(false);
            oneUser.setSelected(false);
            selectedMembers.remove(oneUser);
            selectedUserList.remove((Integer) oneUser.getId());
            if (userCountChangeListener != null) {
                userCountChangeListener.onUserCountChange(String.valueOf(selectedMembers.size()) + "/" + String.valueOf(members.size()));
            }
        }
    }
}
