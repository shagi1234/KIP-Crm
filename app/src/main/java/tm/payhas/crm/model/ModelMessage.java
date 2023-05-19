package tm.payhas.crm.model;

import android.graphics.drawable.Drawable;

public class ModelMessage {
    public static final int LAYOUT_SEND_MESSAGE = 1;
    public static final int LAYOUT_RECEIVED_MESSAGE = 2;
    public static final int LAYOUT_SEND_VOICE_MESSAGE = 3;
    public static final int LAYOUT_RECEIVED_VOICE_MESSAGE = 4;
    public static final int LAYOUT_SEND_FILE = 5;
    public static final int LAYOUT_RECEIVED_FILE = 6;
    public static final int LAYOUT_SEND_IMAGE = 7;
    public static final int LAYOUT_RECEIVED_IMAGE = 8;

    private String message;
    private int image;
    private Drawable imageDr;
    private int viewType;

    public ModelMessage(String message, int viewType) {
        this.message = message;
        this.viewType = viewType;
    }
    public ModelMessage(int other, int viewType) {
        this.message = message;
        this.viewType = viewType;
    }


    public ModelMessage(Drawable image, int viewType) {
        this.imageDr = image;
        this.viewType = viewType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
