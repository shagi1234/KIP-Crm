package tm.payhas.crm.api.data.dto;

import tm.payhas.crm.api.data.dto.subClassesUserInfo.DataMessageRoom;
import tm.payhas.crm.api.data.dto.subClassesUserInfo.DtoArmy;
import tm.payhas.crm.api.data.dto.subClassesUserInfo.DtoCv;
import tm.payhas.crm.api.data.dto.subClassesUserInfo.DtoEntrepreneur;
import tm.payhas.crm.api.data.dto.subClassesUserInfo.DtoGeneralInformation;
import tm.payhas.crm.api.data.dto.subClassesUserInfo.DtoMigrationPermission;
import tm.payhas.crm.api.data.dto.subClassesUserInfo.DtoPassportData;
import tm.payhas.crm.api.data.dto.subClassesUserInfo.DtoPersonalData;
import tm.payhas.crm.api.data.dto.subClassesUserInfo.DtoToken;
import tm.payhas.crm.api.data.dto.subClassesUserInfo.DtoWorkBook;

public class DtoUserInfo {
    private String lastActivity;
    private DtoPersonalData personalData;
    private String avatar;
    private String gender;
    private String nationality;
    private DtoPassportData passportData;
    private int tabelNumber;
    private String registeredAddress;
    private int registeredRegionId;
    private String livingAddress;
    private int livingRegionId;
    private DtoMigrationPermission migrationPermission;
    private DtoWorkBook workBook;
    private DtoGeneralInformation generalInformation;
    private DtoCv cv;
    private DtoArmy army;
    private String email;
    private String mobilePhoneNumber;
    private DtoEntrepreneur entrepreneur;
    private String status;
    private String medicalByPass;
    private String certification;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;
    private DtoToken token;
    private DataMessageRoom messageRoom;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DataMessageRoom getMessageRoom() {
        return messageRoom;
    }

    public void setMessageRoom(DataMessageRoom messageRoom) {
        this.messageRoom = messageRoom;
    }

    public String getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(String lastActivity) {
        this.lastActivity = lastActivity;
    }

    public DtoPersonalData getPersonalData() {
        return personalData;
    }

    public void setPersonalData(DtoPersonalData personalData) {
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

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public DtoPassportData getPassportData() {
        return passportData;
    }

    public void setPassportData(DtoPassportData passportData) {
        this.passportData = passportData;
    }

    public int getTabelNumber() {
        return tabelNumber;
    }

    public void setTabelNumber(int tabelNumber) {
        this.tabelNumber = tabelNumber;
    }

    public String getRegisteredAddress() {
        return registeredAddress;
    }

    public void setRegisteredAddress(String registeredAddress) {
        this.registeredAddress = registeredAddress;
    }

    public int getRegisteredRegionId() {
        return registeredRegionId;
    }

    public void setRegisteredRegionId(int registeredRegionId) {
        this.registeredRegionId = registeredRegionId;
    }

    public String getLivingAddress() {
        return livingAddress;
    }

    public void setLivingAddress(String livingAddress) {
        this.livingAddress = livingAddress;
    }

    public int getLivingRegionId() {
        return livingRegionId;
    }

    public void setLivingRegionId(int livingRegionId) {
        this.livingRegionId = livingRegionId;
    }

    public DtoMigrationPermission getMigrationPermission() {
        return migrationPermission;
    }

    public void setMigrationPermission(DtoMigrationPermission migrationPermission) {
        this.migrationPermission = migrationPermission;
    }

    public DtoWorkBook getWorkBook() {
        return workBook;
    }

    public void setWorkBook(DtoWorkBook workBook) {
        this.workBook = workBook;
    }

    public DtoGeneralInformation getGeneralInformation() {
        return generalInformation;
    }

    public void setGeneralInformation(DtoGeneralInformation generalInformation) {
        this.generalInformation = generalInformation;
    }

    public DtoCv getCv() {
        return cv;
    }

    public void setCv(DtoCv cv) {
        this.cv = cv;
    }

    public DtoArmy getArmy() {
        return army;
    }

    public void setArmy(DtoArmy army) {
        this.army = army;
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

    public DtoEntrepreneur getEntrepreneur() {
        return entrepreneur;
    }

    public void setEntrepreneur(DtoEntrepreneur entrepreneur) {
        this.entrepreneur = entrepreneur;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMedicalByPass() {
        return medicalByPass;
    }

    public void setMedicalByPass(String medicalByPass) {
        this.medicalByPass = medicalByPass;
    }

    public String getCertification() {
        return certification;
    }

    public void setCertification(String certification) {
        this.certification = certification;
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

    public DtoToken getToken() {
        return token;
    }

    public void setToken(DtoToken token) {
        this.token = token;
    }
}
