package xyz.atte.img_hurdur;

import android.media.Image;

/**
 * Created by Atte on 20.4.2016.
 */
public class ImageCardData {
    protected int imageResourceId;
    protected String title;
    protected String description;


    public ImageCardData(int imageResourceId, String title, String description) {
        this.imageResourceId = imageResourceId;
        this.title = title;
        this.description = description;
    }

    public ImageCardData() {

    }
}
