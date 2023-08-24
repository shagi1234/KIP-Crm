package tm.payhas.crm.presentation.view.adapters;

import static tm.payhas.crm.data.remote.api.network.Network.BASE_PHOTO;
import static tm.payhas.crm.domain.helpers.Common.addFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import tm.payhas.crm.R;
import tm.payhas.crm.domain.model.DataImages;
import tm.payhas.crm.presentation.view.activity.ActivityMain;
import tm.payhas.crm.presentation.view.fragment.FragmentAddNews;
import tm.payhas.crm.presentation.view.fragment.FragmentOpenGallery;
import tm.payhas.crm.domain.helpers.SelectedMedia;
import tm.payhas.crm.domain.interfaces.UploadedFilesUrl;

public class AdapterAddImage extends RecyclerView.Adapter<AdapterAddImage.ViewHolder> {

    private Context context;
    private ArrayList<DataImages> images = new ArrayList<>();

    public AdapterAddImage(Context context) {
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setImages(ArrayList<DataImages> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    public ArrayList<DataImages> getImages() {
        return images;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataImages image = images.get(position);
        holder.bind(image);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final RoundedImageView imageLoaded;
        private final ImageView deleteIcon;
        private final FrameLayout clicker;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageLoaded = itemView.findViewById(R.id.image);
            deleteIcon = itemView.findViewById(R.id.delete);
            clicker = itemView.findViewById(R.id.clicker);
        }

        public void bind(DataImages image) {
            if (image.isSelector()) {
                deleteIcon.setVisibility(View.GONE);
                imageLoaded.setImageResource(R.drawable.ic_add_image);
                clicker.setOnClickListener(view -> {
                    clicker.setEnabled(false);
                    SelectedMedia.getArrayList().clear();
                    addFragment(ActivityMain.mainFragmentManager, R.id.main_content, FragmentOpenGallery.newInstance(5, 0, FragmentOpenGallery.MANY, 0));
                    new Handler().postDelayed(() -> clicker.setEnabled(true), 200);
                });
            } else {
                Picasso.get().load(BASE_PHOTO + image.getImageUrl()).placeholder(R.color.primary).into(imageLoaded);
                deleteIcon.setOnClickListener(view -> {
                    DataImages imageSelected = images.get(getAdapterPosition());
                    Fragment addNews = ActivityMain.mainFragmentManager.findFragmentByTag(FragmentAddNews.class.getSimpleName());
                    if (addNews instanceof UploadedFilesUrl) {
                        ((UploadedFilesUrl) addNews).deleteSelectedImage(imageSelected);
                    }
                    images.remove(imageSelected);
                    notifyItemRemoved(getAdapterPosition());
                });
            }
        }
    }
}
