package tm.payhas.crm.domain.model;

import androidx.room.Embedded;

import com.google.gson.annotations.SerializedName;

public class DataAuthor {
    @SerializedName("id")
    private int idAuthor;
    @SerializedName("isSelected")
    private boolean isSelectedAuthor = false;
    @SerializedName("lastActivity")
    private String lastActivityAuthor;
    @SerializedName("isActive")
    private boolean isActiveAuthor;
    @Embedded
    @SerializedName("personalData")
    private DataPersonalData personalDataAuthor;
    @SerializedName("avatar")
    private String avatarAuthor;
    @SerializedName("email")
    private String emailAuthor;
    @SerializedName("mobilePhoneNumber")
    private String mobilePhoneNumberAuthor;
    @SerializedName("status")
    private String statusAuthor;
    @SerializedName("createdAt")
    private String createdAtAuthor;
    @SerializedName("updatedAt")
    private String updatedAtAuthor;
    @SerializedName("deletedAt")
    private String deletedAtAuthor;
    @Embedded
    private DataRoom messageRoom;

    public int getIdAuthor() {
        return idAuthor;
    }

    public void setIdAuthor(int idAuthor) {
        this.idAuthor = idAuthor;
    }

    public boolean isSelectedAuthor() {
        return isSelectedAuthor;
    }

    public void setSelectedAuthor(boolean selectedAuthor) {
        isSelectedAuthor = selectedAuthor;
    }

    public String getLastActivityAuthor() {
        return lastActivityAuthor;
    }

    public void setLastActivityAuthor(String lastActivityAuthor) {
        this.lastActivityAuthor = lastActivityAuthor;
    }

    public boolean isActiveAuthor() {
        return isActiveAuthor;
    }

    public void setActiveAuthor(boolean activeAuthor) {
        isActiveAuthor = activeAuthor;
    }

    public DataPersonalData getPersonalDataAuthor() {
        return personalDataAuthor;
    }

    public void setPersonalDataAuthor(DataPersonalData personalDataAuthor) {
        this.personalDataAuthor = personalDataAuthor;
    }

    public String getAvatarAuthor() {
        return avatarAuthor;
    }

    public void setAvatarAuthor(String avatarAuthor) {
        this.avatarAuthor = avatarAuthor;
    }

    public String getEmailAuthor() {
        return emailAuthor;
    }

    public void setEmailAuthor(String emailAuthor) {
        this.emailAuthor = emailAuthor;
    }

    public String getMobilePhoneNumberAuthor() {
        return mobilePhoneNumberAuthor;
    }

    public void setMobilePhoneNumberAuthor(String mobilePhoneNumberAuthor) {
        this.mobilePhoneNumberAuthor = mobilePhoneNumberAuthor;
    }

    public String getStatusAuthor() {
        return statusAuthor;
    }

    public void setStatusAuthor(String statusAuthor) {
        this.statusAuthor = statusAuthor;
    }

    public String getCreatedAtAuthor() {
        return createdAtAuthor;
    }

    public void setCreatedAtAuthor(String createdAtAuthor) {
        this.createdAtAuthor = createdAtAuthor;
    }

    public String getUpdatedAtAuthor() {
        return updatedAtAuthor;
    }

    public void setUpdatedAtAuthor(String updatedAtAuthor) {
        this.updatedAtAuthor = updatedAtAuthor;
    }

    public String getDeletedAtAuthor() {
        return deletedAtAuthor;
    }

    public void setDeletedAtAuthor(String deletedAtAuthor) {
        this.deletedAtAuthor = deletedAtAuthor;
    }

    public DataRoom getMessageRoom() {
        return messageRoom;
    }

    public void setMessageRoom(DataRoom messageRoom) {
        this.messageRoom = messageRoom;
    }
}
