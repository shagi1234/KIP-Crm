package tm.payhas.crm.domain.helpers;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

public class NoItemAnimationRV extends DefaultItemAnimator {
    @Override
    public boolean animateChange(@NonNull RecyclerView.ViewHolder oldHolder, @NonNull RecyclerView.ViewHolder newHolder, @NonNull ItemHolderInfo preInfo, @NonNull ItemHolderInfo postInfo) {
        dispatchChangeFinished(oldHolder, true);
        dispatchChangeFinished(newHolder, false);
        return false;
    }
}
