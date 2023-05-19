package tm.payhas.crm.api.data.dto.subClassesUserInfo;

import com.google.gson.annotations.SerializedName;

public class DtoPassportData {
    @SerializedName("seriya")
    private String series;
    private String fileUrl;
    private String fileName;
    private int passportNumber;

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
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

    public int getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(int passportNumber) {
        this.passportNumber = passportNumber;
    }
}
