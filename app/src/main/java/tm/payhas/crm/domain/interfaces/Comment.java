package tm.payhas.crm.domain.interfaces;

import tm.payhas.crm.domain.model.DataComments;

public interface Comment {
    void onNewComment(DataComments comments);
}
