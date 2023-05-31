package tm.payhas.crm.interfaces;

import java.util.ArrayList;

import tm.payhas.crm.dataModels.DataAttachment;
import tm.payhas.crm.dataModels.DataMessageTarget;
import tm.payhas.crm.dataModels.DataProjectUsers;
import tm.payhas.crm.model.ModelFile;

public interface ChatRoomInterface {
    void multiSelectedArray(ArrayList<ModelFile> selected);

    void selectedUserList(ArrayList<DataProjectUsers> selected);

    void userStatus(boolean isActive);

    void newMessage(DataMessageTarget messageTarget);

    void newImageImageUrl(DataAttachment attachment);

}
