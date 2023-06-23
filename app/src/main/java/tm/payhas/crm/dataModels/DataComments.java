package tm.payhas.crm.dataModels;

import com.google.gson.annotations.SerializedName;

import tm.payhas.crm.api.data.dto.DtoUserInfo;

public class DataComments {
    private int id;
    private String text;
    private String createdAt;
    private int authorId;
    private DtoUserInfo author;
    @SerializedName("_count")
    private Count count;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public DtoUserInfo getAuthor() {
        return author;
    }

    public void setAuthor(DtoUserInfo author) {
        this.author = author;
    }

    public Count getCount() {
        return count;
    }

    public void setCount(Count count) {
        this.count = count;
    }

    public class Count {
        private int viewers;
        private int comments;

        public int getViewers() {
            return viewers;
        }

        public void setViewers(int viewers) {
            this.viewers = viewers;
        }

        public int getComments() {
            return comments;
        }

        public void setComments(int comments) {
            this.comments = comments;
        }
    }

}
