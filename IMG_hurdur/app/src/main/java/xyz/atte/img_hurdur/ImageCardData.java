package xyz.atte.img_hurdur;

import java.net.URL;

/**
 * Created by Atte on 20.4.2016.
 */
public class ImageCardData {
    //protected int imageResourceId;
    protected URL imageUrl;
    protected String title;
    protected String description;
    protected String imageId;


    public ImageCardData(URL imageUrl, String title, String description, String imageId) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.description = description;
        this.imageId = imageId;
    }

    public ImageCardData() {

    }
}
