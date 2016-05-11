package xyz.atte.img_hurdur;

import java.util.List;

/**
 * The model for comment data
 *
 * @author Atte Huhtakangas
 * @author Mikko Tossavainen
 * @version 1.0
 */
public class CommentData {
    protected String comment;
    protected String id;
    protected String userName;

    /**
     * Child comments (not used)
     */
    protected List<CommentData> children;

    public CommentData(String comment, String id, String userName, List<CommentData> children) {
        this.comment = comment;
        this.id = id;
        this.userName = userName;
        this.children = children;
    }

    public CommentData() {
    }

    @Override
    public String toString() {
        return userName;
    }
}
