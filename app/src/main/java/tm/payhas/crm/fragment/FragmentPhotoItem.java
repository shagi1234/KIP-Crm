package tm.payhas.crm.fragment;

import static tm.payhas.crm.api.network.Network.BASE_PHOTO;
import static tm.payhas.crm.helpers.StaticMethods.hideSoftKeyboard;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.squareup.picasso.Picasso;

import tm.payhas.crm.databinding.FragmentPhotoItemBinding;



public class FragmentPhotoItem extends Fragment {
    private FragmentPhotoItemBinding b;
    private String imageUrl;


    public static FragmentPhotoItem newInstance(String imgUrl) {
        FragmentPhotoItem fragment = new FragmentPhotoItem();
        Bundle args = new Bundle();
        args.putString("image_url", imgUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        hideSoftKeyboard(getActivity());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSoftKeyboard(getActivity());
        if (getArguments() != null) {
            imageUrl = getArguments().getString("image_url");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentPhotoItemBinding.inflate(inflater);
        hideSoftKeyboard(getActivity());
        initListeners();
        setImageResource();
        return b.getRoot();
    }

    private void setImageResource() {
        if (getContext() == null) {
            return;
        }
        Picasso.get().load(BASE_PHOTO + imageUrl).placeholder(getPlaceholder()).into(b.imagePhoto);
//        Glide.with(getContext())
//                .load(Network.BASE_URL + "/api/" + imageUrl)
//                .placeholder(getPlaceholder())
//                .into(b.imagePhoto);
    }

    private void initListeners() {
        b.cancel.setOnClickListener(view -> {
            if (getActivity() == null) return;
            getActivity().onBackPressed();
        });
    }

    private Drawable getPlaceholder() {
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(getContext());
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();
        return circularProgressDrawable;
    }
}