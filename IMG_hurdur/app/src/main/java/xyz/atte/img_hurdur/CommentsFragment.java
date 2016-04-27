package xyz.atte.img_hurdur;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Atte on 27.4.2016.
 */
public class CommentsFragment extends Fragment {
    private static final String TAG = "CommentsView";

    private List<CommentData> mCommentsDataList;

    public CommentsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDataSet();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.comments, container, false);
        rootView.setTag(TAG);

        //ml
        return rootView;
    }

    private void initDataSet() {
        mCommentsDataList = new LinkedList<>();
        mCommentsDataList.add(new CommentData("Hello max!", "1234", "Foobar", null));
        mCommentsDataList.add(new CommentData("Yolo!", "1244", "Per Kunter", null));
        mCommentsDataList.add(new CommentData("SUKA BLYAT!", "1434", "Peter", null));

    }
}
