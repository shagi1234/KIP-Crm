package tm.payhas.crm.model;

import androidx.room.Entity;

@Entity(tableName = "messages")
public class EntityMessageWeb  implements Cloneable {
    private String uuid;
    private  int type;
    private  String senderId;
    private  String roomId;
    private int status;
    private  String date;
    private  String replyMessage;
    private String content;
    private  String fileName;
    private  String singerName;
    private String thumbnail;
    private  int postType;
    private  int accountType;
    private  int size;
    private  int duration;
    private  String localFileUri;
    private  String name;
    private  String avatar;
    private  String colorCode;

    public EntityMessageWeb(
            String uuid,
            int type,
            String senderId,
            String roomId,
            int status,
            String date,
            String replyMessage,
            String content,
            String fileName,
            String singerName,
            String thumbnail,
            int postType,
            int accountType,
            int size,
            int duration,
            String localFileUri,
            String name,
            String avatar,
            String colorCode
    ){
        this.uuid = uuid;
        this.type = type;
        this.senderId = senderId;
        this.roomId = roomId;
        this.status = status;
        this.date = date;
        this.replyMessage = replyMessage;
        this.content = content;
        this.fileName = fileName;
        this.singerName = singerName;
        this.thumbnail = thumbnail;
        this.postType = postType;
        this.accountType = accountType;
        this.size = size;
        this.duration = duration;
        this.localFileUri = localFileUri;
        this.name = name;
        this.avatar = avatar;
        this.colorCode = colorCode;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReplyMessage() {
        return replyMessage;
    }

    public void setReplyMessage(String replyMessage) {
        this.replyMessage = replyMessage;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getPostType() {
        return postType;
    }

    public void setPostType(int postType) {
        this.postType = postType;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getLocalFileUri() {
        return localFileUri;
    }

    public void setLocalFileUri(String localFileUri) {
        this.localFileUri = localFileUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }



}
