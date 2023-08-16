package tm.payhas.crm.adapters;

import static tm.payhas.crm.api.network.Network.BASE_PHOTO;
import static tm.payhas.crm.helpers.Common.normalDate;
import static tm.payhas.crm.helpers.Common.normalTime;

import android.content.Context;
import android.text.TextUtils;
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
import tm.payhas.crm.api.data.dto.DtoUserInfo;
import tm.payhas.crm.interfaces.OnUserCountChangeListener;
import tm.payhas.crm.preference.AccountPreferences;

public class AdapterCreateGroup extends RecyclerView.Adapter<AdapterCreateGroup.ViewHolder> {

    private Context context;
    private ArrayList<DtoUserInfo> members = new ArrayList<>();
    private ArrayList<DtoUserInfo> selectedMembers = new ArrayList<>();
    private ArrayList<Integer> selectedUserList = new ArrayList<>();
    private OnUserCountChangeListener userCountChangeListener;


    public AdapterCreateGroup(Context context) {
        this.context = context;
    }

    public void setUserCountChangeListener(OnUserCountChangeListener listener) {
        this.userCountChangeListener = listener;
    }

    public ArrayList<DtoUserInfo> getSelectedMembers() {
        return selectedMembers;
    }

    public void setSelectedMembers(ArrayList<DtoUserInfo> selectedMembers) {
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
        DtoUserInfo oneUser = members.get(position);
        holder.bind(oneUser);
    }

    @Override
    public int getItemCount() {
        return members.size();
    }


    public void setMembers(ArrayList<DtoUserInfo> members) {
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

        public void bind(DtoUserInfo oneUser) {
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

        private void setUserInfo(DtoUserInfo oneUser) {
            if (oneUser.getLastActivity() != null)
                lastSeen.setText(String.format("%s %s", normalDate(oneUser.getLastActivity()), normalTime(oneUser.getLastActivity())));
            else lastSeen.setText("");
            String name = oneUser.getPersonalData().getName();
            String surname = oneUser.getPersonalData().getSurname();

            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(surname)) {
                nameSurname.setText(String.format("%s %s", name, surname));
            } else if (!TextUtils.isEmpty(name)) {
                nameSurname.setText(name);
            } else if (!TextUtils.isEmpty(surname)) {
                nameSurname.setText(surname);
            } else {
                nameSurname.setText("");
            }
            Picasso.get().load(BASE_PHOTO + oneUser.getAvatar()).placeholder(R.color.primary).into(avatar);
        }

        public void setChecked(DtoUserInfo oneUser) {
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

        public void setUnChecked(DtoUserInfo oneUser) {
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
