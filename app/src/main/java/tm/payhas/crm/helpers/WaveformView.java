package tm.payhas.crm.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class WaveformView extends View {
    private Paint paint;
    private short[] waveformData;
    private int waveformColor;
    private float strokeWidth;
    private Bitmap waveformBitmap;
    private Canvas waveformCanvas;
    private Path waveformPath;

    public WaveformView(Context context) {
        super(context);
        init();
    }

    public WaveformView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        waveformColor = 0xFF000000; // Default waveform color (black)
        strokeWidth = 2f; // Default stroke width

        // Set paint properties
        paint.setColor(waveformColor);
        paint.setStrokeWidth(strokeWidth);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);

        waveformBitmap = null;
        waveformCanvas = null;
        waveformPath = new Path();
    }

    public void setWaveformData(short[] waveformData) {
        this.waveformData = waveformData;
        createWaveformBitmap();
        invalidate();
    }

    public void setWaveformColor(int color) {
        this.waveformColor = color;
        paint.setColor(waveformColor);
        invalidate();
    }

    public void setStrokeWidth(float width) {
        this.strokeWidth = width;
        paint.setStrokeWidth(strokeWidth);
        createWaveformBitmap();
        invalidate();
    }

    private void createWaveformBitmap() {
        if (waveformData == null || waveformData.length == 0 || getWidth() == 0 || getHeight() == 0) {
            waveformBitmap = null;
            waveformCanvas = null;
            waveformPath.reset(); // Reset the waveform path
            return;
        }

        waveformBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        waveformCanvas = new Canvas(waveformBitmap);

        int width = getWidth();
        int height = getHeight();
        int halfHeight = height / 2;

        float scaleX = (float) width / waveformData.length;

        waveformPath.reset(); // Reset the waveform path

        float previousX = 0;
        float previousY = halfHeight;

        for (int i = 0; i < waveformData.length; i++) {
            float x = i * scaleX;
            float y = halfHeight - waveformData[i] * halfHeight / Short.MAX_VALUE;

            if (i == 0) {
                waveformPath.moveTo(x, y);
            } else {
                waveformPath.lineTo(x, y);
            }

            previousX = x;
            previousY = y;
        }

        waveformCanvas.drawPath(waveformPath, paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (waveformBitmap != null) {
            canvas.drawBitmap(waveformBitmap, 0, 0, null);
        }
    }
}
