package tm.payhas.crm.api.data.dto.subClassesUserInfo;

import com.google.gson.annotations.SerializedName;

public class DtoToken {
    @SerializedName("acceToken")
    private String token;

    public String getToken() {
        return token;
    }

}
