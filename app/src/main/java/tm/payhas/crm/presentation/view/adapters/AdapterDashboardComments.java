package tm.payhas.crm.presentation.view.adapters;

import static tm.payhas.crm.data.remote.api.network.Network.BASE_PHOTO;
import static tm.payhas.crm.domain.helpers.Common.normalDate;

import android.annotation.SuppressLint;
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
import tm.payhas.crm.domain.model.DataComments;
import tm.payhas.crm.domain.interfaces.Comment;

public class AdapterDashboardComments extends RecyclerView.Adapter<AdapterDashboardComments.ViewHolder> implements Comment {

    private final Context context;
    private ArrayList<DataComments> comments = new ArrayList<>();

    public AdapterDashboardComments(Context context) {
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setComments(ArrayList<DataComments> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dashboard_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataComments oneComment = comments.get(position);
        holder.bind(oneComment);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onNewComment(DataComments comment) {
        comments.add(comment);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView commentUsername;
        private final TextView commentDate;
        private final TextView commentText;
        private final RoundedImageView commentAvatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            commentUsername = itemView.findViewById(R.id.comment_username);
            commentDate = itemView.findViewById(R.id.comment_date);
            commentText = itemView.findViewById(R.id.comment_text);
            commentAvatar = itemView.findViewById(R.id.comment_avatar);
        }

        @SuppressLint("SetTextI18n")
        public void bind(DataComments oneComment) {
            commentText.setText(oneComment.getText());
            commentDate.setText(normalDate(oneComment.getCreatedAt()));
            commentUsername.setText(oneComment.getAuthor().getPersonalData().getName() + "  " + oneComment.getAuthor().getPersonalData().getSurname());
            Picasso.get().load(BASE_PHOTO + oneComment.getAuthor().getAvatar()).placeholder(R.color.primary).into(commentAvatar);
        }
    }
}
