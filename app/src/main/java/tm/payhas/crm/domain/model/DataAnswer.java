package tm.payhas.crm.domain.model;

import androidx.room.Embedded;

import com.google.gson.annotations.SerializedName;

public class DataAnswer {
    @SerializedName("roomId")
    private Integer roomIdAnswer = null;
    @SerializedName("id")
    private Integer idAnswer = null;
    @SerializedName("localId")
    private String localIdAnswer;
    @SerializedName("type")
    private String typeAnswer = "";
    @SerializedName("text")
    private String textAnswer;
    @SerializedName("forwardId")
    private Integer forwardIdAnswer = null;
    @SerializedName("answerId")
    private Integer answerIdAnswer = null;
    @SerializedName("friendId")
    private int friendIdAnswer;
    @SerializedName("status")
    private String statusAnswer;
    @SerializedName("authorId")
    private int authorIdAnswer;
    @Embedded
    @SerializedName("attachment")
    private DataAttachment attachmentAnswer;
    @SerializedName("createdAt")
    private String createdAtAnswer;
    @SerializedName("updatedAt")
    private String updatedAtAnswer;
    @SerializedName("deletedAt")
    private String deletedAtAnswer;
    @Embedded(prefix = "prefix_")
    @SerializedName("author")
    private DataAuthor authorAnswer;

    public DataAnswer(Integer roomIdAnswer, Integer idAnswer, String localIdAnswer, String typeAnswer, String textAnswer, Integer forwardIdAnswer, Integer answerIdAnswer, int friendIdAnswer, String statusAnswer, int authorIdAnswer, DataAttachment attachmentAnswer, String createdAtAnswer, String updatedAtAnswer, String deletedAtAnswer, DataAuthor authorAnswer) {
        this.roomIdAnswer = roomIdAnswer;
        this.idAnswer = idAnswer;
        this.localIdAnswer = localIdAnswer;
        this.typeAnswer = typeAnswer;
        this.textAnswer = textAnswer;
        this.forwardIdAnswer = forwardIdAnswer;
        this.answerIdAnswer = answerIdAnswer;
        this.friendIdAnswer = friendIdAnswer;
        this.statusAnswer = statusAnswer;
        this.authorIdAnswer = authorIdAnswer;
        this.attachmentAnswer = attachmentAnswer;
        this.createdAtAnswer = createdAtAnswer;
        this.updatedAtAnswer = updatedAtAnswer;
        this.deletedAtAnswer = deletedAtAnswer;
        this.authorAnswer = authorAnswer;
    }

    public Integer getRoomIdAnswer() {
        return roomIdAnswer;
    }

    public void setRoomIdAnswer(Integer roomIdAnswer) {
        this.roomIdAnswer = roomIdAnswer;
    }

    public Integer getIdAnswer() {
        return idAnswer;
    }

    public void setIdAnswer(Integer idAnswer) {
        this.idAnswer = idAnswer;
    }

    public String getLocalIdAnswer() {
        return localIdAnswer;
    }

    public void setLocalIdAnswer(String localIdAnswer) {
        this.localIdAnswer = localIdAnswer;
    }

    public String getTypeAnswer() {
        return typeAnswer;
    }

    public void setTypeAnswer(String typeAnswer) {
        this.typeAnswer = typeAnswer;
    }

    public String getTextAnswer() {
        return textAnswer;
    }

    public void setTextAnswer(String textAnswer) {
        this.textAnswer = textAnswer;
    }

    public Integer getForwardIdAnswer() {
        return forwardIdAnswer;
    }

    public void setForwardIdAnswer(Integer forwardIdAnswer) {
        this.forwardIdAnswer = forwardIdAnswer;
    }

    public Integer getAnswerIdAnswer() {
        return answerIdAnswer;
    }

    public void setAnswerIdAnswer(Integer answerIdAnswer) {
        this.answerIdAnswer = answerIdAnswer;
    }

    public int getFriendIdAnswer() {
        return friendIdAnswer;
    }

    public void setFriendIdAnswer(int friendIdAnswer) {
        this.friendIdAnswer = friendIdAnswer;
    }

    public String getStatusAnswer() {
        return statusAnswer;
    }

    public void setStatusAnswer(String statusAnswer) {
        this.statusAnswer = statusAnswer;
    }

    public int getAuthorIdAnswer() {
        return authorIdAnswer;
    }

    public void setAuthorIdAnswer(int authorIdAnswer) {
        this.authorIdAnswer = authorIdAnswer;
    }

    public DataAttachment getAttachmentAnswer() {
        return attachmentAnswer;
    }

    public void setAttachmentAnswer(DataAttachment attachmentAnswer) {
        this.attachmentAnswer = attachmentAnswer;
    }

    public String getCreatedAtAnswer() {
        return createdAtAnswer;
    }

    public void setCreatedAtAnswer(String createdAtAnswer) {
        this.createdAtAnswer = createdAtAnswer;
    }

    public String getUpdatedAtAnswer() {
        return updatedAtAnswer;
    }

    public void setUpdatedAtAnswer(String updatedAtAnswer) {
        this.updatedAtAnswer = updatedAtAnswer;
    }

    public String getDeletedAtAnswer() {
        return deletedAtAnswer;
    }

    public void setDeletedAtAnswer(String deletedAtAnswer) {
        this.deletedAtAnswer = deletedAtAnswer;
    }

    public DataAuthor getAuthorAnswer() {
        return authorAnswer;
    }

    public void setAuthorAnswer(DataAuthor authorAnswer) {
        this.authorAnswer = authorAnswer;
    }
}
