package tm.payhas.crm.presentation.view.adapters;

import static tm.payhas.crm.data.remote.api.network.Network.BASE_PHOTO;
import static tm.payhas.crm.domain.helpers.Common.addFragment;
import static tm.payhas.crm.domain.helpers.Common.normalDate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.payhas.crm.R;
import tm.payhas.crm.data.remote.api.request.RequestComment;
import tm.payhas.crm.data.remote.api.response.ResponseComment;
import tm.payhas.crm.domain.model.DataComments;
import tm.payhas.crm.domain.model.DataNews;
import tm.payhas.crm.presentation.view.activity.ActivityMain;
import tm.payhas.crm.presentation.view.fragment.FragmentDashboardItem;
import tm.payhas.crm.domain.helpers.Common;
import tm.payhas.crm.domain.helpers.NoItemAnimationRV;
import tm.payhas.crm.domain.interfaces.Comment;
import tm.payhas.crm.data.localdb.preference.AccountPreferences;

public class AdapterNews extends RecyclerView.Adapter<AdapterNews.ViewHolder> {

    private final Context context;
    private ArrayList<DataNews> news = new ArrayList<>();
    private AccountPreferences ac;

    public AdapterNews(Context context) {
        this.context = context;
        ac = new AccountPreferences(context);
    }

    public void setNews(ArrayList<DataNews> news) {
        this.news = news;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dashboard_news, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(news.get(position));
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ViewPager2 viewPagerImages;
        private final RecyclerView rvIndicator;
        private final AdapterNewsIndicator adapterNewsIndicator;
        private final AdapterNewsImages adapterNewsImages;
        private final TextView authorNameSurname;
        private final TextView newsTime;
        private final TextView newsTitle;
        private final TextView newsDetails;
        private final EditText commentInput;
        private final ImageView sendComment;
        private final RoundedImageView myAvatar;
        private final RoundedImageView authorAvatar;
        private final TextView lastCommentUserName;
        private final TextView lastCommentUserDate;
        private final TextView lastCommentUserText;
        private final RoundedImageView lastCommentAvatar;
        private final LinearLayout lastCommentLayout;
        private final LinearLayout clickableLayout;
        private final AdapterDashboardComments adapterComment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            clickableLayout = itemView.findViewById(R.id.clickable_layout);
            rvIndicator = itemView.findViewById(R.id.view_pager_indicator);
            viewPagerImages = itemView.findViewById(R.id.news_images);
            authorNameSurname = itemView.findViewById(R.id.author_name_surname);
            newsTime = itemView.findViewById(R.id.news_time);
            newsTitle = itemView.findViewById(R.id.news_title);
            newsDetails = itemView.findViewById(R.id.news_details);
            sendComment = itemView.findViewById(R.id.send_comment);
            commentInput = itemView.findViewById(R.id.comment_input);
            myAvatar = itemView.findViewById(R.id.comment_my_avatar);
            lastCommentUserName = itemView.findViewById(R.id.last_comment_username);
            lastCommentUserDate = itemView.findViewById(R.id.last_comment_date);
            lastCommentUserText = itemView.findViewById(R.id.last_comment_text);
            lastCommentAvatar = itemView.findViewById(R.id.last_comment_avatar);
            authorAvatar = itemView.findViewById(R.id.news_author_image);
            lastCommentLayout = itemView.findViewById(R.id.last_comment_layout);

            adapterComment = new AdapterDashboardComments(context);

            adapterNewsImages = new AdapterNewsImages(itemView.getContext());
            adapterNewsIndicator = new AdapterNewsIndicator(itemView.getContext());

            viewPagerImages.setAdapter(adapterNewsImages);
            rvIndicator.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            rvIndicator.setAdapter(adapterNewsIndicator);
            rvIndicator.setItemAnimator(new NoItemAnimationRV());

            viewPagerImages.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    adapterNewsIndicator.setCurrentPosition(position);
                }
            });
        }

        public void bind(DataNews oneNews) {
            setListeners(oneNews.getId());
            setInfo(oneNews);
        }

        private void setListeners(int id) {
            clickableLayout.setOnClickListener(view -> {
                clickableLayout.setEnabled(false);
                addFragment(ActivityMain.mainFragmentManager, R.id.main_content, FragmentDashboardItem.newInstance(id));
                new Handler().postDelayed(() -> clickableLayout.setEnabled(true), 200);
            });

            sendComment.setOnClickListener(view -> {
                sendComment(id);
                commentInput.setText("");
            });
        }

        @SuppressLint("SetTextI18n")
        private void setInfo(DataNews oneNews) {
            if (oneNews.getAttachments().size() != 0 || oneNews.getAttachments() != null)
                setImagesList(oneNews.getAttachments());
            else
                viewPagerImages.setVisibility(View.GONE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                newsTitle.setText(Html.fromHtml(oneNews.getTitle(), Html.FROM_HTML_MODE_COMPACT));
                newsDetails.setText(Html.fromHtml(oneNews.getContent(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                newsTitle.setText(Html.fromHtml(oneNews.getTitle()));
                newsDetails.setText(Html.fromHtml(oneNews.getContent()));
            }

            Picasso.get().load(BASE_PHOTO + oneNews.getAuthor().getAvatar()).placeholder(R.color.primary).into(authorAvatar);
            newsTime.setText(normalDate(oneNews.getCreatedAt()));
            authorNameSurname.setText(oneNews.getAuthor().getPersonalData().getName() + "  " + oneNews.getAuthor().getPersonalData().getSurname());
            Picasso.get().load(BASE_PHOTO + ac.getPrefAvatarUrl()).placeholder(R.color.primary).into(myAvatar);
            Picasso.get().load(BASE_PHOTO + oneNews.getAuthor().getAvatar()).placeholder(R.color.primary).into(authorAvatar);
            if (oneNews.getComments() != null && oneNews.getComments().size() > 0) {
                setLastComment(oneNews.getComments().get(0));
            } else {
                lastCommentLayout.setVisibility(View.GONE);
            }
        }

        private void setLastComment(DataComments comments) {
            lastCommentUserText.setText(comments.getText());
            lastCommentUserName.setText(comments.getAuthor().getPersonalData().getName() + "  " + comments.getAuthor().getPersonalData().getSurname());
            Picasso.get().load(BASE_PHOTO + comments.getAuthor().getAvatar()).placeholder(R.color.primary).into(lastCommentAvatar);
            lastCommentUserDate.setText(normalDate(comments.getCreatedAt()));
        }

        private void sendComment(int i) {
            RequestComment requestComment = new RequestComment();
            requestComment.setNewsId(i);
            requestComment.setAuthorId(ac.getAuthorId());
            requestComment.setText(commentInput.getText().toString());
            Call<ResponseComment> call = Common.getApi().addComment(ac.getToken(), requestComment);
            call.enqueue(new Callback<ResponseComment>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(@NonNull Call<ResponseComment> call, @NonNull Response<ResponseComment> response) {
                    if (response.isSuccessful()) {
                        if (adapterComment != null && response.body() != null) {
                            setLastComment(response.body().getData());
                            ((Comment) adapterComment).onNewComment(response.body().getData());
                            adapterComment.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseComment> call, Throwable t) {
                }
            });
        }

        private void setImagesList(ArrayList<String> attachments) {
            adapterNewsImages.setImages(attachments);
            if (attachments.size() > 1) {
                adapterNewsIndicator.setImages(attachments);
            } else {
                rvIndicator.setVisibility(View.GONE);
            }
        }
    }
}
