package tm.payhas.crm.helpers;

import android.view.SurfaceHolder;

public class SurfaceViewCallBack implements SurfaceHolder.Callback {
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // Initialize the drawing code here (e.g., start drawing the waveform)
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Respond to changes in the surface (if needed)
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Clean up resources or stop drawing (if needed)
    }
}

