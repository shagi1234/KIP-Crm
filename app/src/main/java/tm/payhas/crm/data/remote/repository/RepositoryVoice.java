package tm.payhas.crm.data.remote.repository;

import static tm.payhas.crm.domain.helpers.StaticMethods.getOutputFilePath;

import android.content.Context;
import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;

public class RepositoryVoice {
    private MediaRecorder mediaRecorder;
    private String outputFilePath;
    private Context context;

    public RepositoryVoice(Context context) {
        this.context = context;
    }

    public String startRecording() throws IOException {
        if (mediaRecorder != null) {
            throw new IllegalStateException("Recording is already in progress");
        }

        String filePath = getOutputFilePath(context);
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(filePath);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.prepare();
        mediaRecorder.start();

        outputFilePath = filePath;
        return filePath;
    }

    public File stopRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }

        if (outputFilePath != null) {
            File voiceRecorded = new File(getOutputFilePath(context));
            return voiceRecorded;
        }

        return null;
    }

}