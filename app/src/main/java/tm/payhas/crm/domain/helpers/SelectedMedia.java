package tm.payhas.crm.domain.helpers;

import java.util.ArrayList;

import tm.payhas.crm.domain.model.MediaLocal;

public class SelectedMedia {
    private static ArrayList<MediaLocal> mediaLocals;

    public static ArrayList<MediaLocal> getArrayList() {
        if (mediaLocals == null) {
            mediaLocals=new ArrayList<>();
        }
        return mediaLocals;
    }
}
