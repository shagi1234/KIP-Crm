package tm.payhas.crm.domain.interfaces;

import java.util.ArrayList;

import tm.payhas.crm.domain.model.DataImages;

public interface UploadedFilesUrl {

    void onUploadManyFiles(ArrayList<String> dataFiles);

    void deleteSelectedImage(DataImages image);
}
