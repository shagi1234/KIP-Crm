package tm.payhas.crm.dataModels;

import com.google.gson.annotations.SerializedName;

public class MediaLocal {
    private int id;
    @SerializedName("image_path")
    private String path;
    private int type;

    public MediaLocal(int id, String path, int type) {
        this.id = id;
        this.path = path;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
