package xyz.atte.img_hurdur;

import android.graphics.Bitmap;

/**
 * Model for image card's data
 *
 * @author Atte Huhtakangas
 * @author Mikko Tossavainen
 * @version 1.0
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
