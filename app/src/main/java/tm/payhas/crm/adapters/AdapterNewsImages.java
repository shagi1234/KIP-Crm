package tm.payhas.crm.adapters;

import static tm.payhas.crm.api.network.Network.BASE_PHOTO;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import tm.payhas.crm.R;

public class AdapterNewsImages extends RecyclerView.Adapter<AdapterNewsImages.ViewHolder> {

    private final Context context;
    private ArrayList<String> images = new ArrayList<>();


    public AdapterNewsImages(Context context) {
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setImages(ArrayList<String> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_images, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String oneImage = images.get(position);
        holder.bind(oneImage);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final RoundedImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.news_image_item);
        }

        public void bind(String oneImage) {
            Picasso.get().load(BASE_PHOTO + oneImage).placeholder(R.color.primary).into(image);
        }
    }
}
