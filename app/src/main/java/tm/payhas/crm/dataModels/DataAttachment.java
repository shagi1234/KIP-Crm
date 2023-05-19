package tm.payhas.crm.dataModels;

public class DataAttachment {

    private String type;
    private String duration;
    private String size;
    private String fileUrl;
    private String fileName;

    public DataAttachment(String type, String duration, String size, String fileUrl, String fileName) {
        this.type = type;
        this.duration = duration;
        this.size = size;
        this.fileUrl = fileUrl;
        this.fileName = fileName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
