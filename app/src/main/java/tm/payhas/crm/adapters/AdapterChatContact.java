package tm.payhas.crm.adapters;

import static tm.payhas.crm.activity.ActivityMain.mainFragmentManager;
import static tm.payhas.crm.helpers.StaticMethods.hideSoftKeyboard;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;

import tm.payhas.crm.R;
import tm.payhas.crm.fragment.FragmentChatRoom;
import tm.payhas.crm.helpers.Common;
import tm.payhas.crm.helpers.StaticMethods;

public class AdapterChatContact extends RecyclerView.Adapter<AdapterChatContact.ViewHolder> {

    private Context context;
    private Activity activity;

    public AdapterChatContact(Context context) {
        this.context = context;
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
        return 15;
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
            StaticMethods.setBackgroundDrawable(context, contactChatCount, R.color.primary, 0, 50, false, 0);
            contactChatMain.setOnClickListener(view -> {
                contactChatMain.setEnabled(false);
                hideSoftKeyboard(activity);
                Common.addFragment(mainFragmentManager, R.id.main_content, FragmentChatRoom.newInstance());
                new Handler().postDelayed(() -> contactChatMain.setEnabled(true), 200);
            });
        }
    }
}
