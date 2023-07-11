package tm.payhas.crm.interfaces;

import android.view.SurfaceHolder;

import java.util.ArrayList;

import tm.payhas.crm.dataModels.DataFile;
import tm.payhas.crm.dataModels.DataFolder;
import tm.payhas.crm.dataModels.DataMessageTarget;
import tm.payhas.crm.model.ModelFile;

public interface ChatRoomInterface {


    void onMessageStatus(DataMessageTarget messageTarget);

    void userStatus(boolean isActive);

    void newMessage(DataMessageTarget messageTarget);

    void newImageImageUrl(DataFile dataFile);

    void deleteMessage(DataMessageTarget messageTarget);

    void newReplyMessage(DataMessageTarget messageTarget);

    void onMessageReceived(DataMessageTarget messageTarget);

    void onWaveformUpdate(short[] waveformData);
}
