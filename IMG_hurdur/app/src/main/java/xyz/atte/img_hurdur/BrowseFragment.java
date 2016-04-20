package xyz.atte.img_hurdur;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;


public class BrowseFragment extends Fragment {
    private static final String TAG = "BrowseFragment";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<ImageCardData> mImageCardDataList;

    public BrowseFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDataSet();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_browse, container, false);
        rootView.setTag(TAG);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);



        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(mImageCardDataList);
        mRecyclerView.setAdapter(mAdapter);

        // Inflate the layout for this fragment
        return rootView;
    }

    private void initDataSet() {
        mImageCardDataList = new LinkedList<>();
        mImageCardDataList.add(new ImageCardData(R.drawable.corgi, "Corgi", "I'm a corgi, woof woof"));
        mImageCardDataList.add(new ImageCardData(R.drawable.corgi, "Corgi", "I'm a corgi, woof woof"));
        mImageCardDataList.add(new ImageCardData(R.drawable.corgi, "Corgi", "I'm a corgi, woof woof"));
        mImageCardDataList.add(new ImageCardData(R.drawable.corgi, "Corgi", "I'm a corgi, woof woof"));
        mImageCardDataList.add(new ImageCardData(R.drawable.corgi, "Corgi", "I'm a corgi, woof woof"));
    }
}
