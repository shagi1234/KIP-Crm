package tm.payhas.crm.api.data.dto.subClassesUserInfo;

public class DtoArmy {
    private boolean exists;
    private String fileName;
    private String fileUrl;
    private int militaryId;

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public int getMilitaryId() {
        return militaryId;
    }

    public void setMilitaryId(int militaryId) {
        this.militaryId = militaryId;
    }
}
