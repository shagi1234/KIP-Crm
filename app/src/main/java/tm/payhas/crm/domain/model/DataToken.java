package tm.payhas.crm.domain.model;

import com.google.gson.annotations.SerializedName;

public class DataToken {
    @SerializedName("acceToken")
    private String token;

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

}
