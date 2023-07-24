package tm.payhas.crm.adapters;

import static tm.payhas.crm.api.network.Network.BASE_PHOTO;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import tm.payhas.crm.R;
import tm.payhas.crm.api.data.dto.DtoUserInfo;
import tm.payhas.crm.dataModels.DataProject;

public class AdapterSelectedUsers extends RecyclerView.Adapter<AdapterSelectedUsers.ViewHolder> {

    private Context context;
    private int type;
    private ArrayList<DataProject.UserInTask> userList = new ArrayList<>();
    private ArrayList<DtoUserInfo> selectedList = new ArrayList<>();
    private ArrayList<Integer> userIdList = new ArrayList<>();

    public AdapterSelectedUsers(Context context, int type) {
        this.context = context;
        this.type = type;
    }

    public void setUserList(ArrayList<DataProject.UserInTask> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }

    public void setSelectedList(ArrayList<DtoUserInfo> selectedList) {
        this.selectedList = selectedList;
        notifyDataSetChanged();
    }

    private void createUserIdList() {
        for (int i = 0; i < selectedList.size(); i++) {
            if (!(userIdList.contains(selectedList.get(i).getId()))) {
                userIdList.add(selectedList.get(i).getId());
            }
        }
    }

    public ArrayList<Integer> getSelectedList() {
        createUserIdList();
        return userIdList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_users, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.e("TAG", "Adapter" + userList.size());
        if (type == 1) {
            DataProject.UserInTask oneUser = userList.get(position);
            holder.bind(oneUser);
        } else {
            DtoUserInfo user = selectedList.get(position);
            holder.bindUser(user);
        }

    }

    @Override
    public int getItemCount() {
        if (type == 1) {
            return userList.size();
        } else {
            return selectedList.size();
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView image;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.avatar_user);
            name = itemView.findViewById(R.id.name_user);
        }

        public void bind(DataProject.UserInTask oneUser) {
            name.setText(oneUser.getUser().getPersonalData().getName());
            Picasso.get().load(BASE_PHOTO + oneUser.getUser().getAvatar()).placeholder(R.color.primary).into(image);
        }

        public void bindUser(DtoUserInfo user) {
            name.setText(user.getPersonalData().getName());
            Picasso.get().load(BASE_PHOTO + user.getAvatar()).placeholder(R.color.primary).into(image);

        }


    }
}
