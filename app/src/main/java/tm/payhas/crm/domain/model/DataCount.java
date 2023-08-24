package tm.payhas.crm.domain.model;

public class DataCount {
    private int messages = 0;
    private int participants = 0;

    public int getParticipants() {
        return participants;
    }

    public void setParticipants(int participants) {
        this.participants = participants;
    }

    public int getMessages() {
        return messages;
    }

    public void setMessages(int messages) {
        this.messages = messages;
    }
}

