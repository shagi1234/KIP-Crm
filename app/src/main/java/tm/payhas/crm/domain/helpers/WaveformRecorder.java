package tm.payhas.crm.domain.helpers;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;

import androidx.core.app.ActivityCompat;

public class WaveformRecorder {
    private Context context;
    private static final int SAMPLE_RATE = 44100;
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;

    private AudioRecord audioRecord;
    private boolean isRecording;
    private short[] audioBuffer;
    private OnWaveformUpdateListener waveformUpdateListener;
    private Handler handler;

    public interface OnWaveformUpdateListener {
        void onWaveformUpdate(short[] waveformData);
    }

    public WaveformRecorder(Context context) {
        this.context = context;
        audioBuffer = new short[SAMPLE_RATE];
        handler = new Handler();
    }

    public void setOnWaveformUpdateListener(OnWaveformUpdateListener listener) {
        waveformUpdateListener = listener;
    }

    public void startRecording() {
        int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, CHANNEL_CONFIG,
                AUDIO_FORMAT, bufferSize);

        isRecording = true;
        audioRecord.startRecording();

        new Thread(() -> {
            while (isRecording) {
                int bytesRead = audioRecord.read(audioBuffer, 0, audioBuffer.length);
                if (bytesRead > 0) {
                    final short[] waveformData = new short[bytesRead];
                    System.arraycopy(audioBuffer, 0, waveformData, 0, bytesRead);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (waveformUpdateListener != null) {
                                waveformUpdateListener.onWaveformUpdate(waveformData);
                            }
                        }
                    });
                }
            }
        }).start();
    }

    public void stopRecording() {
        isRecording = false;
        audioRecord.stop();
        audioRecord.release();
    }

    public void clearWaveform() {
        audioBuffer = new short[SAMPLE_RATE];
    }
}
