package tm.payhas.crm.domain.model;

import androidx.room.Embedded;

import com.google.gson.annotations.SerializedName;

public class DtoRoom {
    @SerializedName("_count")
    @Embedded
    private DataCount count;

    public DataCount getCount() {
        return count;
    }

    public void setCount(DataCount count) {
        this.count = count;
    }

}
