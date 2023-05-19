package tm.payhas.crm.interfaces;


import tm.payhas.crm.dataModels.DataMessage;

public interface MessageCallBack {
    void newMessage(DataMessage dataMessage);

    void newMessagePhoto(DataMessage dataMessage);

    void newMessageVoice(DataMessage dataMessage);

    void newMessageFile(DataMessage dataMessage);

}
