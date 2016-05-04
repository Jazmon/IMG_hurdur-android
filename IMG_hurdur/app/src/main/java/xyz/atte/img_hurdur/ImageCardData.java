package xyz.atte.img_hurdur;

import android.graphics.Bitmap;

import java.net.URL;

/**
 * Created by Atte on 20.4.2016.
 */
public class ImageCardData {
    protected Bitmap image;
    protected String path;
    protected String title;
    protected String description;
    protected String imageId;


    public ImageCardData(String path, Bitmap image, String title, String description, String imageId) {
        this.path = path;
        this.image = image;
        this.title = title;
        this.description = description;
        this.imageId = imageId;
    }

    public ImageCardData() {

    }
}
