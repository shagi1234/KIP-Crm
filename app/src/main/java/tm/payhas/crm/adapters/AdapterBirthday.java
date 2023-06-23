package tm.payhas.crm.adapters;

import static tm.payhas.crm.activity.ActivityMain.mainFragmentManager;
import static tm.payhas.crm.api.network.Network.BASE_PHOTO;
import static tm.payhas.crm.helpers.Common.addFragment;
import static tm.payhas.crm.helpers.Common.normalDate;

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
import tm.payhas.crm.dataModels.DataBirthdays;
import tm.payhas.crm.dataModels.DataNews;
import tm.payhas.crm.fragment.FragmentDashboardItem;

public class AdapterBirthday extends RecyclerView.Adapter<AdapterBirthday.ViewHolder> {

    private final Context context;
    private int type = 0;
    private ArrayList<DataBirthdays> birthdays = new ArrayList<>();
    private ArrayList<DataNews> holidays = new ArrayList<>();

    public AdapterBirthday(Context context) {
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setBirthdays(ArrayList<DataBirthdays> birthdays) {
        this.birthdays = birthdays;
        type = 1;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setHolidays(ArrayList<DataNews> holidays) {
        this.holidays = holidays;
        type = 2;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_birthday, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (type == 1) {
            DataBirthdays birthday = birthdays.get(position);
            holder.bind(birthday);
        } else if (type == 2) {
            DataNews holiday = holidays.get(position);
            holder.bindHolidays(holiday);
        }


    }

    @Override
    public int getItemCount() {
        if (type == 1) {
            return birthdays.size();
        } else {
            return holidays.size();
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final RoundedImageView avatar;
        private final TextView title;
        private final TextView date;
        private final FrameLayout clickablelayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.item_birthday_avatar);
            title = itemView.findViewById(R.id.birthday_info);
            date = itemView.findViewById(R.id.birthday_date);
            clickablelayout = itemView.findViewById(R.id.clickable_layout);
        }

        @SuppressLint("SetTextI18n")
        public void bind(DataBirthdays birthday) {
            Picasso.get().load(BASE_PHOTO + birthday.getUser().getAvatar()).placeholder(R.color.primary).into(avatar);
            title.setText(birthday.getUser().getPersonalData().getName() + " " + birthday.getUser().getPersonalData().getSurname());
            date.setText(normalDate(birthday.getDate()));
        }

        public void bindHolidays(DataNews holiday) {
            Picasso.get().load(BASE_PHOTO + holiday.getAttachments().get(0)).placeholder(R.color.primary).into(avatar);
            title.setText(holiday.getTitle());
            date.setText(normalDate(holiday.getCreatedAt()));
            setListeners(holiday.getId());
        }

        private void setListeners(int id) {
            clickablelayout.setOnClickListener(view -> {
                clickablelayout.setEnabled(false);
                addFragment(mainFragmentManager, R.id.main_content, FragmentDashboardItem.newInstance(id));
                new Handler().postDelayed(() -> clickablelayout.setEnabled(true), 200);
            });
        }
    }
}
