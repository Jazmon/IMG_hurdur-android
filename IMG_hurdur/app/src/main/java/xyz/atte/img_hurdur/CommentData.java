package xyz.atte.img_hurdur;

import java.util.List;

/**
 * Created by Atte on 27.4.2016.
 */
public class CommentData {
    protected String comment;
    protected String id;
    protected String userName;

    protected List<CommentData> children;

    public CommentData(String comment, String id, String userName, List<CommentData> children) {
        this.comment = comment;
        this.id = id;
        this.userName = userName;
        this.children = children;
    }

    public CommentData() {

    }
}
