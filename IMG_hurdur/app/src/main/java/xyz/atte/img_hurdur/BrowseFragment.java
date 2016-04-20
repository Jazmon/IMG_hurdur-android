package xyz.atte.img_hurdur;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;



public class    BrowseFragment extends Fragment {

    private static final String TAG = "BrowseFragment";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<ImageCardData> mImageCardDataList;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public BrowseFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDataSet();

    }

    // https://stackoverflow.com/questions/25178329/recyclerview-and-swiperefreshlayout
    private void refreshItems() {
        Log.d(TAG, "refreshItems");
        // Load items
        // ...

        // Load complete
        onItemsLoadComplete();
    }

    private void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...
        Log.d(TAG, "onItemsLoadComplete");

        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_browse, container, false);
        rootView.setTag(TAG);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "onRefresh");
                refreshItems();
            }
        });
        mSwipeRefreshLayout.setEnabled(false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(false);


        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);


        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(mImageCardDataList);
        mRecyclerView.setAdapter(mAdapter);

        /*int pos = 0;
        mImageCardDataList.remove(pos);
        mRecyclerView.removeViewAt(pos);
        mAdapter.notifyItemRemoved(pos);
        mAdapter.notifyItemRangeChanged(pos, mImageCardDataList.size());*/
        //mAdapter.notifyDataSetChanged();
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
