package tm.payhas.crm.interfaces;

import tm.payhas.crm.dataModels.DataComments;

public interface Comment {
    void onNewComment(DataComments comments);
}
