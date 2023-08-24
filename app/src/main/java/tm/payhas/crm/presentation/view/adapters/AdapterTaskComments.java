package tm.payhas.crm.presentation.view.adapters;

import static tm.payhas.crm.data.remote.api.network.Network.BASE_PHOTO;
import static tm.payhas.crm.domain.helpers.Common.normalDate;

import android.content.Context;
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
import tm.payhas.crm.domain.model.DataTaskComment;

public class AdapterTaskComments extends RecyclerView.Adapter<AdapterTaskComments.ViewHolder> {

    private Context context;
    private ArrayList<DataTaskComment> comments = new ArrayList<>();

    public AdapterTaskComments(Context context) {
        this.context = context;
    }

    public void setComments(ArrayList<DataTaskComment> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }

    public void addComment(DataTaskComment oneComment) {
        comments.add(oneComment);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataTaskComment comment = comments.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView username;
        private RoundedImageView avatar;
        private TextView commentText;
        private TextView commentDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.comment_username);
            avatar = itemView.findViewById(R.id.comment_avatar);
            commentText = itemView.findViewById(R.id.comment_text);
            commentDate = itemView.findViewById(R.id.comment_date);

        }

        public void bind(DataTaskComment comment) {
            username.setText(comment.getUser().getPersonalData().getName() + " " + comment.getUser().getPersonalData().getSurname());
            commentText.setText(comment.getText());
            commentDate.setText(normalDate(comment.getCreatedAt()));
            Picasso.get().load(BASE_PHOTO + comment.getUser().getAvatar()).placeholder(R.color.primary).into(avatar);

        }
    }
}