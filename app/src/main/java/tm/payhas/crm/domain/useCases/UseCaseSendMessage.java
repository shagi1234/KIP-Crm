package tm.payhas.crm.domain.useCases;

import android.content.Context;

import java.io.File;
import java.io.IOException;

import tm.payhas.crm.data.localdb.repository.RepositoryMessage;
import tm.payhas.crm.data.remote.repository.RepositorySendMessage;
import tm.payhas.crm.domain.model.DataAnswer;
import tm.payhas.crm.domain.model.DataFile;

public class UseCaseSendMessage {
    RepositorySendMessage repositorySendMessage;

    public UseCaseSendMessage(Context context, RepositoryMessage repositoryMessage) {
        repositorySendMessage = new RepositorySendMessage(repositoryMessage, context);
    }

    public void sendMessage(int replyId, String text, String messageType, DataAnswer messageToReply, int roomId, int userId) {
        repositorySendMessage.sendMessage(replyId, text, messageType, messageToReply, roomId, userId);
    }

    public void sendFileImageVoice(String messageType, DataFile file, int replyId, String text, DataAnswer messageToReply, int roomId, int userId) throws IOException {
        repositorySendMessage.sendFile(messageType, file, roomId, userId, replyId, messageToReply);
    }

    public void sendFileImageVoice(String messageType, File file, int replyId, String text, DataAnswer messageToReply, int roomId, int userId) throws IOException {
        repositorySendMessage.uploadFile(messageType, file, replyId, text, messageToReply, roomId, userId);
    }

}
