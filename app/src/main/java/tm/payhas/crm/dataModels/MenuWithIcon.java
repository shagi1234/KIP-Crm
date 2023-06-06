package tm.payhas.crm.dataModels;

import android.graphics.drawable.Drawable;

public class MenuWithIcon {

    private Drawable icon;
    private String title;

    public MenuWithIcon(Drawable icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}

