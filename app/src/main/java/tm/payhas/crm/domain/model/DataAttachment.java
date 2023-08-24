package tm.payhas.crm.domain.model;

import androidx.room.Embedded;

import com.google.gson.annotations.SerializedName;

public class DataAttachment {
    @SerializedName("type")
    @Embedded(prefix = "prefix_")
    private String attachmentType;
    private String duration;
    private Integer size = null;
    private String fileUrl;
    private String fileName;


    public String getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }

    public Integer getSize() {
        return getSafeInt(size);
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
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

    private int getSafeInt(Integer value) {
        return value != null ? value : 0;
    }

}
