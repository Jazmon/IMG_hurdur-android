package xyz.atte.img_hurdur;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;


/**
 * The fragment that displays the image cards
 *
 * @author Atte Huhtakangas
 * @author Mikko Tossavainen
 * @version 1.0
 */
public class BrowseFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<ImageCardData> mImageCardDataList;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String mUrl;

    public BrowseFragment() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUrl = getResources().getString(R.string.host_name);
        initDataSet();
    }

    /**
     * Calls the async task to refresh image cards
     */
    private void refreshItems() {
        // Load items
        new GetImagesTask().execute((Void) null);
        // Load complete
        onItemsLoadComplete();
    }

    /**
     * Stops the swipelayout refresh animation
     */
    private void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        mImageCardDataList.clear();

        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);
    }

    /**
     * Creates the recyclerview / swipe refresh layouts
     * <br>
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_browse, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(false);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyAdapter(mImageCardDataList);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    /**
     * Gets the images from the server
     */
    private void initDataSet() {
        mImageCardDataList = new LinkedList<>();
        new GetImagesTask().execute((Void) null);
    }

    /**
     * Gets the image from the server
     */
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        int index;

        /**
         * Sets the index which image from the cardlist the image is mapped to
         *
         * @param index the index which image from the cardlist the image is mapped to
         */
        public DownloadImageTask(int index) {
            this.index = index;
        }

        /**
         * Constructs a bitmap from the response stream
         * {@inheritDoc}
         */
        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        /**
         * Sets the imagecard image and notifies it has changed
         * {@inheritDoc}
         */
        @Override
        protected void onPostExecute(Bitmap result) {
            mImageCardDataList.get(index).image = result;
            mAdapter.notifyItemChanged(index);
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Gets the images
     */
    private class GetImagesTask extends AsyncTask<Void, Void, List<ImageCardData>> {
        private String mAuthHeader;


        /**
         * Sets the header
         * <br>
         * {@inheritDoc}
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mAuthHeader = "Bearer " + ((MainActivity) getActivity()).mToken;
        }

        /**
         * Gets images from the api server
         * <br>
         * {@inheritDoc}
         *
         * @param params
         * @return
         */
        @Override
        protected List<ImageCardData> doInBackground(Void... params) {
            List<ImageCardData> list = new LinkedList<>();
            URL url = null;
            try {
                url = new URL(mUrl + "/api/image");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            String response = "";

            try {
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10_000);
                conn.setConnectTimeout(15_000);
                conn.setRequestMethod("GET");
                conn.addRequestProperty("Authorization", mAuthHeader);
                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }

                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Bitmap bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ALPHA_8);

                        ImageCardData cardData =
                                new ImageCardData(
                                        jsonObject.getString("uploadPath"),
                                        bitmap,
                                        jsonObject.getString("title"),
                                        jsonObject.getString("description"),
                                        jsonObject.getString("_id"));
                        list.add(cardData);
                    }

                    return list;
                } else {
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return list;
        }


        /**
         * Creates requests for each image object to the server to get the actual image.
         * Notifies the recyclerview's adapter the data has changed
         * <br>
         * {@inheritDoc}
         */
        @Override
        protected void onPostExecute(List<ImageCardData> list) {
            super.onPostExecute(list);

            for (int i = 0; i < list.size(); i++) {
                ImageCardData data = list.get(i);
                mImageCardDataList.add(data);
                new DownloadImageTask(i).execute(mUrl + "/uploads/" + data.path);
            }

            mRecyclerView.removeAllViews();
            mAdapter.notifyItemRangeRemoved(0, mAdapter.getItemCount());

            mAdapter.notifyDataSetChanged();
        }
    }
}
