package tm.payhas.crm.adapters;

import static tm.payhas.crm.activity.ActivityMain.mainFragmentManager;
import static tm.payhas.crm.api.network.Network.BASE_PHOTO;
import static tm.payhas.crm.helpers.Common.addFragment;
import static tm.payhas.crm.helpers.Common.normalDate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
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

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.payhas.crm.R;
import tm.payhas.crm.api.request.RequestComment;
import tm.payhas.crm.api.response.ResponseComment;
import tm.payhas.crm.dataModels.DataComments;
import tm.payhas.crm.dataModels.DataNews;
import tm.payhas.crm.fragment.FragmentDashboardItem;
import tm.payhas.crm.helpers.Common;
import tm.payhas.crm.helpers.NoItemAnimationRV;
import tm.payhas.crm.interfaces.Comment;
import tm.payhas.crm.preference.AccountPreferences;

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

        holder.bind();
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

        }

        public void bind() {
            Log.e("AdapterNews", "bind: " + news.size());
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

            DataNews oneNews = news.get(getAdapterPosition());
            setListeners(oneNews.getId());
            setInfo(oneNews);
        }

        private void setListeners(int id) {
            clickableLayout.setOnClickListener(view -> {
                clickableLayout.setEnabled(false);
                addFragment(mainFragmentManager, R.id.main_content, FragmentDashboardItem.newInstance(id));
                new Handler().postDelayed(() -> clickableLayout.setEnabled(true), 200);
            });


        }

        private void setInfo(DataNews oneNews) {
            initListeners(oneNews);
            setImagesList(oneNews.getAttachments());
            newsTitle.setText(oneNews.getTitle());
            newsDetails.setText(oneNews.getContent());
            Picasso.get().load(BASE_PHOTO + oneNews.getAuthor().getAvatar()).placeholder(R.color.primary).into(authorAvatar);
            newsTime.setText(normalDate(oneNews.getCreatedAt()));
            authorNameSurname.setText(oneNews.getAuthor().getPersonalData().getName() + "  " + oneNews.getAuthor().getPersonalData().getSurname());
            Picasso.get().load(BASE_PHOTO + ac.getPrefAvatarUrl()).placeholder(R.color.primary).into(myAvatar);
            Picasso.get().load(BASE_PHOTO + oneNews.getAuthor().getAvatar()).placeholder(R.color.primary).into(authorAvatar);
            if (oneNews.getComments() != null) {
                if (oneNews.getComments().size() != 0) {
                    setLastComment(oneNews.getComments().get(0));
                }
            } else {
                lastCommentLayout.setVisibility(View.GONE);
            }
        }

        private void initListeners(DataNews oneNews) {
            sendComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendComment(oneNews.getId());
                    commentInput.setText("");
                }
            });
        }

        @SuppressLint("SetTextI18n")
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
                        if (adapterComment != null) {
                            assert response.body() != null;
                            setLastComment(response.body().getData());
                            if (response.body().getData() != null) {
                                ((Comment) adapterComment).onNewComment(response.body().getData());
                                adapterComment.notifyDataSetChanged();
                            }

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
