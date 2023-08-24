package tm.payhas.crm.domain.model;

public class DataUploadFileResult {

    private String messageType;
    private DataFile dataFile;

    public DataUploadFileResult(String messageType, DataFile dataFile) {
        this.messageType = messageType;
        this.dataFile = dataFile;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public DataFile getDataFile() {
        return dataFile;
    }

    public void setDataFile(DataFile dataFile) {
        this.dataFile = dataFile;
    }
}

