package tm.payhas.crm.fragment;

import static tm.payhas.crm.api.network.Network.BASE_PHOTO;
import static tm.payhas.crm.helpers.Common.normalDate;
import static tm.payhas.crm.helpers.StaticMethods.setPadding;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.payhas.crm.R;
import tm.payhas.crm.adapters.AdapterDashboardComments;
import tm.payhas.crm.adapters.AdapterNewsImages;
import tm.payhas.crm.adapters.AdapterNewsIndicator;
import tm.payhas.crm.api.RetrofitCallback;
import tm.payhas.crm.api.request.RequestComment;
import tm.payhas.crm.api.response.ResponseComment;
import tm.payhas.crm.api.response.ResponseDashboardItem;
import tm.payhas.crm.dataModels.DataNews;
import tm.payhas.crm.databinding.FragmentDashboardItemBinding;
import tm.payhas.crm.helpers.Common;
import tm.payhas.crm.helpers.NoItemAnimationRV;
import tm.payhas.crm.interfaces.Comment;
import tm.payhas.crm.interfaces.OnInternetStatus;
import tm.payhas.crm.preference.AccountPreferences;

public class FragmentDashboardItem extends Fragment {
    private int newsId;
    private FragmentDashboardItemBinding b;
    private AccountPreferences ac;
    private AdapterNewsIndicator adapterNewsIndicator;
    private AdapterNewsImages adapterNewsImages;
    private AdapterDashboardComments adapterDashboardComments;
    private int page = 1;
    private final int limit = 10;

    public static FragmentDashboardItem newInstance(int i) {
        FragmentDashboardItem fragment = new FragmentDashboardItem();
        Bundle args = new Bundle();
        args.putInt("newsId", i);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(() -> setPadding(b.main,
                0,
                50,
                0,
                0), 100);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            newsId = getArguments().getInt("newsId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentDashboardItemBinding.inflate(inflater);
        ac = new AccountPreferences(getContext());
        initListeners();
        getNewsInfo();
        setRecycler();
        setImagesAdapter();
        return b.getRoot();
    }

    private void initListeners() {
        b.sendComment.setOnClickListener(view -> {
            sendComment(newsId);
            b.commentInput.setText("");
        });
        b.swiper.setOnRefreshListener(() -> {
            b.swiper.setRefreshing(true);
            getNewsInfo();
        });
    }

    private void setImagesAdapter() {
        adapterNewsImages = new AdapterNewsImages(getContext());
        adapterNewsIndicator = new AdapterNewsIndicator(getContext());
        b.newsImages.setAdapter(adapterNewsImages);
        b.indicator.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        b.indicator.setAdapter(adapterNewsIndicator);
        b.indicator.setItemAnimator(new NoItemAnimationRV());

        b.newsImages.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                adapterNewsIndicator.setCurrentPosition(position);
            }
        });
    }

    private void setImagesList(ArrayList<String> attachments) {
        adapterNewsImages.setImages(attachments);
        if (attachments.size() > 1) {
            adapterNewsIndicator.setImages(attachments);
        } else {
            b.indicator.setVisibility(View.GONE);
        }

    }

    private void setRecycler() {
        adapterDashboardComments = new AdapterDashboardComments(getContext());
        b.rvComments.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));
        b.rvComments.setAdapter(adapterDashboardComments);
    }


    private void getNewsInfo() {
        Call<ResponseDashboardItem> getDashboardInfo = Common.getApi().getDashboardItem(ac.getToken(), newsId, page, limit);
        getDashboardInfo.enqueue(new RetrofitCallback<ResponseDashboardItem>() {
            @Override
            public void onResponse(ResponseDashboardItem response) {
                if (response.isSuccess()) {
                    b.swiper.setRefreshing(false);
                    OnInternetStatus internetStatusListener = new OnInternetStatus() {
                    };
                    internetStatusListener.setConnected(b.progressBar.getRoot(), b.noInternet.getRoot(), b.main);
                    setInfo(response.getData().getNews());
                }
            }


            private void setInfo(DataNews oneNews) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    b.newsTitle.setText(Html.fromHtml(oneNews.getTitle(), Html.FROM_HTML_MODE_COMPACT));
                    b.newsDetails.setText(Html.fromHtml(oneNews.getContent(), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    b.newsTitle.setText(Html.fromHtml(oneNews.getTitle()));
                    b.newsDetails.setText(Html.fromHtml(oneNews.getContent()));
                }
                Picasso.get().load(BASE_PHOTO + ac.getPrefAvatarUrl()).placeholder(R.color.primary).into(b.commentMyAvatar);
                Picasso.get().load(BASE_PHOTO + oneNews.getAuthor().getAvatar()).placeholder(R.color.primary).into(b.newsAuthorImage);
                b.newsTime.setText(normalDate(oneNews.getCreatedAt()));
                b.authorNameSurname.setText(oneNews.getAuthor().getPersonalData().getName() + "  " + oneNews.getAuthor().getPersonalData().getSurname());
                Picasso.get().load(BASE_PHOTO + ac.getPrefAvatarUrl()).placeholder(R.color.primary).into(b.commentMyAvatar);
                Picasso.get().load(BASE_PHOTO + oneNews.getAuthor().getAvatar()).placeholder(R.color.primary).into(b.commentMyAvatar);
                setImagesList(oneNews.getAttachments());
                if (oneNews.getComments() != null) {
                    adapterDashboardComments.setComments(oneNews.getComments());
                }
            }


            @Override
            public void onFailure(Throwable t) {
                b.swiper.setRefreshing(false);
                OnInternetStatus internetStatusListener = new OnInternetStatus() {
                };
                internetStatusListener.setNoInternet(b.progressBar.getRoot(), b.noInternet.getRoot(), b.main);
            }
        });
    }

    private void sendComment(int i) {
        RequestComment requestComment = new RequestComment();
        requestComment.setNewsId(i);
        requestComment.setAuthorId(ac.getAuthorId());
        requestComment.setText(b.commentInput.getText().toString());
        Call<ResponseComment> call = Common.getApi().addComment(ac.getToken(), requestComment);
        call.enqueue(new Callback<ResponseComment>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<ResponseComment> call, @NonNull Response<ResponseComment> response) {
                if (response.isSuccessful()) {
                    if (adapterDashboardComments != null) {
                        assert response.body() != null;
                        if (response.body().getData() != null) {
                            ((Comment) adapterDashboardComments).onNewComment(response.body().getData());
                            adapterDashboardComments.notifyDataSetChanged();
                        }

                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseComment> call, Throwable t) {

            }
        });
    }

}