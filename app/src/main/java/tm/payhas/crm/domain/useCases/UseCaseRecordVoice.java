package tm.payhas.crm.domain.useCases;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.File;
import java.io.IOException;

import tm.payhas.crm.data.remote.repository.RepositoryVoice;

public class UseCaseRecordVoice {
    private RepositoryVoice repositoryVoice;
    private MutableLiveData<Boolean> isRecording = new MutableLiveData<>(false);


    public UseCaseRecordVoice(Context context) {
        repositoryVoice = new RepositoryVoice(context);
    }

    public void startRecording() throws IOException {
        repositoryVoice.startRecording();
    }

    public File stopRecording() {
        return repositoryVoice.stopRecording();
    }
}
