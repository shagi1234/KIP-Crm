package tm.payhas.crm.data.remote.api.request;

import tm.payhas.crm.domain.model.DataMessageReceived;

public class RequestReceivedMessage {
    private String event;
    private MessageId data;
    private Integer id = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public MessageId getData() {
        return data;
    }

    public void setData(MessageId data) {
        this.data = data;
    }

    public static class MessageId {
        private int messageId;

        public int getMessageId() {
            return messageId;
        }

        public void setMessageId(int messageId) {
            this.messageId = messageId;
        }
    }
}
