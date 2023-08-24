package tm.payhas.crm.domain.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DataManyFiles {
    @SerializedName("filenames")
    private ArrayList<String> fileNames;

    public ArrayList<String> getFileNames() {
        return fileNames;
    }

    public void setFileNames(ArrayList<String> fileNames) {
        this.fileNames = fileNames;
    }
}
