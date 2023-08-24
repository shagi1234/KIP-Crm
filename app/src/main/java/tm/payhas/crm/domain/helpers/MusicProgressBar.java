package tm.payhas.crm.domain.helpers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class MusicProgressBar extends View {
    private Paint backgroundPaint;
    private Paint progressPaint;
    private int progress;
    private int currentProgress;
    private int maxProgress;

    public MusicProgressBar(Context context) {
        super(context);
        init();
    }

    public MusicProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MusicProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // Set up background paint
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.GRAY);
        backgroundPaint.setStyle(Paint.Style.FILL);

        // Set up progress paint
        progressPaint = new Paint();
        progressPaint.setColor(Color.BLUE);
        progressPaint.setStyle(Paint.Style.FILL);

        // Set initial progress, current progress, and max progress
        progress = 0;
        currentProgress = 0;
        maxProgress = 100;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        // Draw background
        canvas.drawRect(0, 0, width, height, backgroundPaint);

        // Calculate progress width
        int progressWidth = (int) (width * (progress / 100f));

        // Draw progress
        canvas.drawRect(0, 0, progressWidth, height, progressPaint);

        // Calculate current progress width
        int currentProgressWidth = (int) (width * (currentProgress / (float) maxProgress));

        // Draw current progress
        canvas.drawRect(0, 0, currentProgressWidth, height, progressPaint);
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate(); // Redraw the view
    }

    public void setCurrentProgress(int currentProgress) {
        this.currentProgress = currentProgress;
        invalidate(); // Redraw the view
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
        invalidate(); // Redraw the view
    }
}




