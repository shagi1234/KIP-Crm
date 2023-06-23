package tm.payhas.crm.adapters;

import static tm.payhas.crm.activity.ActivityMain.mainFragmentManager;
import static tm.payhas.crm.api.network.Network.BASE_PHOTO;
import static tm.payhas.crm.helpers.Common.addFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import tm.payhas.crm.R;
import tm.payhas.crm.api.data.dto.DtoUserInfo;
import tm.payhas.crm.fragment.FragmentUserInfo;

public class AdapterGroupMember extends RecyclerView.Adapter<AdapterGroupMember.ViewHolder> {

    private Context context;
    private ArrayList<DtoUserInfo> memberList = new ArrayList<>();

    public AdapterGroupMember(Context context) {
        this.context = context;
    }

    public void setMemberList(ArrayList<DtoUserInfo> memberList) {
        this.memberList = memberList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_member, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DtoUserInfo member = memberList.get(position);
        holder.bind(member);

    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final RoundedImageView groupMemberAvatar;
        private final TextView groupMemberName;
        private final FrameLayout clickableLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            groupMemberAvatar = itemView.findViewById(R.id.group_member_avatar);
            groupMemberName = itemView.findViewById(R.id.group_member_name);
            clickableLayout = itemView.findViewById(R.id.clickable_layout);
        }

        @SuppressLint("SetTextI18n")
        public void bind(DtoUserInfo member) {
            groupMemberName.setText(member.getPersonalData().getName() + "  " + member.getPersonalData().getSurname());
            Picasso.get().load(BASE_PHOTO + member.getAvatar()).placeholder(R.color.primary).into(groupMemberAvatar);
            clickableLayout.setOnClickListener(view -> {
                clickableLayout.setEnabled(false);
                addFragment(mainFragmentManager, R.id.main_content, FragmentUserInfo.newInstance(member.getId()));
                new Handler().postDelayed(() -> clickableLayout.setEnabled(true), 200);
            });
        }
    }
}
