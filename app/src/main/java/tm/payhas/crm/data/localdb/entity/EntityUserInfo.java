package tm.payhas.crm.data.localdb.entity;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import tm.payhas.crm.domain.model.DataPersonalData;
import tm.payhas.crm.domain.model.DataRoom;
import tm.payhas.crm.domain.model.DataToken;

@Entity(tableName = "table_users")
public class EntityUserInfo {
    @PrimaryKey
    private int id;
    private boolean isSelected = false;
    private String lastActivity;
    private boolean isActive;
    @Embedded
    private DataPersonalData personalData;
    private String avatar;
    private String gender;
    private String email;
    private String mobilePhoneNumber;
    private String status;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;
    @Embedded
    private DataToken token;
    @Embedded
    private DataRoom messageRoom;


    public DataRoom getMessageRoom() {
        return messageRoom;
    }

    public void setMessageRoom(DataRoom messageRoom) {
        this.messageRoom = messageRoom;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(String lastActivity) {
        this.lastActivity = lastActivity;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public DataPersonalData getPersonalData() {
        return personalData;
    }

    public void setPersonalData(DataPersonalData personalData) {
        this.personalData = personalData;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public DataToken getToken() {
        return token;
    }

    public void setToken(DataToken token) {
        this.token = token;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
