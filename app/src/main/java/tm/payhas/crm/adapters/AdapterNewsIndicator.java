package tm.payhas.crm.adapters;

import static tm.payhas.crm.api.network.Network.BASE_PHOTO;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import tm.payhas.crm.R;

public class AdapterNewsIndicator extends RecyclerView.Adapter<AdapterNewsIndicator.ViewHolder> {

    private Context context;
    private int currentPosition;
    private ArrayList<String> images = new ArrayList<>();

    public void setCurrentPosition(int currentPosition) {
        notifyItemChanged(this.currentPosition);
        this.currentPosition = currentPosition;
        notifyItemChanged(currentPosition);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setImages(ArrayList<String> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    public AdapterNewsIndicator(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_indicator, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        FrameLayout layer;
        RoundedImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layer = itemView.findViewById(R.id.layer);
            image = itemView.findViewById(R.id.news_images_indicator);
        }

        public void bind() {
            String oneImage = images.get(getAdapterPosition());
            Picasso.get().load(BASE_PHOTO + oneImage).placeholder(R.color.primary).into(image);
            if (currentPosition == getAdapterPosition()) {
                layer.setVisibility(View.GONE);
            } else {
                layer.setVisibility(View.VISIBLE);
            }
        }
    }


}