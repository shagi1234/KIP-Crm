package tm.payhas.crm.interfaces;

import java.util.ArrayList;

import tm.payhas.crm.api.data.DataImages;

public interface UploadedFilesUrl {

    void onUploadManyFiles(ArrayList<String> dataFiles);

    void deleteSelectedImage(DataImages image);
}
