package tm.payhas.crm.domain.helpers;

import android.view.View;
import android.view.animation.OvershootInterpolator;

import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
public class CustomItemAnimator extends DefaultItemAnimator {

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        holder.itemView.setScaleX(0.5f);
        holder.itemView.setScaleY(0.5f);
        holder.itemView.setAlpha(0f);
        ViewCompat.animate(holder.itemView)
                .scaleX(1f)
                .scaleY(1f)
                .alpha(1f)
                .setDuration(getAddDuration())
                .setInterpolator(new OvershootInterpolator()) // You can adjust the interpolator
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {
                        dispatchAddStarting(holder);
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        dispatchAddFinished(holder);
                    }

                    @Override
                    public void onAnimationCancel(View view) {
                        // Handle animation cancellation if needed
                    }
                })
                .start();
        return false;
    }

    // Implement other necessary methods if needed
}
