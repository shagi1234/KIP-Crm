package tm.payhas.crm.domain.useCases;

import android.content.Context;

import tm.payhas.crm.data.localdb.preference.AccountPreferences;
import tm.payhas.crm.data.remote.repository.RepositoryDeleteMessage;

public class UseCaseDeleteMessage {
    private Context context;
    RepositoryDeleteMessage repositoryDeleteMessage = new RepositoryDeleteMessage();

    public UseCaseDeleteMessage(Context context) {
        this.context = context;
    }

    public void deleteMessage(int messageId) {
        repositoryDeleteMessage.removeMessage(AccountPreferences.newInstance(context).getToken(), messageId);
    }


}
