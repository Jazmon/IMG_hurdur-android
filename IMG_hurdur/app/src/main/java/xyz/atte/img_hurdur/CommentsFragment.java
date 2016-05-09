package xyz.atte.img_hurdur;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Atte on 27.4.2016.
 */
public class CommentsFragment extends Fragment {
    private static final String TAG = "CommentsView";

    private ArrayList<CommentData> mCommentsDataList;
    CommentsAdapter adapter;
    private String [] comments;


    private ListView mListView;

    public CommentsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.comments, container, false);
        rootView.setTag(TAG);
        initDataSet();
        return rootView;
    }

    protected void getCommentsFromServer() {
        //TODO
    }

    protected void postCommentToServer() {
        //TODO


        //After posting the comment refresh comments from the server
        getCommentsFromServer();
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ((MainActivity)getActivity()).showBackButton();

        super.onViewCreated(view, savedInstanceState);
        mListView = (ListView) view.findViewById(R.id.commentListView);

        adapter = new CommentsAdapter(getActivity(),mCommentsDataList);
        mListView.setAdapter(adapter);



        Button button = (Button) view.findViewById(R.id.postCommentButton);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                postCommentToServer();
            }
        });
    }

    private void initDataSet() {
        mCommentsDataList = new ArrayList<>();
        mCommentsDataList.add(new CommentData("Test comment", "1234", "Foobar", null));
        mCommentsDataList.add(new CommentData("Testi kommentti hehehheh", "1244", "Per Kunter", null));
        mCommentsDataList.add(new CommentData("Kommentoin tätä kuvaa :)", "1434", "Peter", null));

    }
}
