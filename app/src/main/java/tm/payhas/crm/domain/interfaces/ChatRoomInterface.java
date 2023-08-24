package tm.payhas.crm.domain.interfaces;

import java.io.IOException;

import tm.payhas.crm.domain.model.DataFile;
import tm.payhas.crm.data.localdb.entity.EntityMessage;

public interface ChatRoomInterface {

    void newImageImageUrl(DataFile dataFile) throws IOException;

    void onWaveformUpdate(short[] waveformData);

}
