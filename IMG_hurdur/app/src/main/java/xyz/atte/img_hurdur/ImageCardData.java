package xyz.atte.img_hurdur;

/**
 * Created by Atte on 20.4.2016.
 */
public class ImageCardData {
    protected int imageResourceId;
    protected String title;
    protected String description;
    protected String imageId;


    public ImageCardData(int imageResourceId, String title, String description, String imageId) {
        this.imageResourceId = imageResourceId;
        this.title = title;
        this.description = description;
        this.imageId = imageId;
    }

    public ImageCardData() {

    }
}
