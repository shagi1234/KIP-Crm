package tm.payhas.crm.helpers;

import java.util.ArrayList;

import tm.payhas.crm.dataModels.MediaLocal;

public class SelectedMedia {
    private static ArrayList<MediaLocal> mediaLocals;

    public static ArrayList<MediaLocal> getArrayList() {
        if (mediaLocals == null) {
            mediaLocals=new ArrayList<>();
        }
        return mediaLocals;
    }
}
