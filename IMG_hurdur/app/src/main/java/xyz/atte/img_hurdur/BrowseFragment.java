package xyz.atte.img_hurdur;


import android.app.ProgressDialog;
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
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;


public class BrowseFragment extends Fragment implements View.OnClickListener {
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
        Log.d(TAG, "onCreate");
        initDataSet();
    }

    private void refreshItems() {
        Log.d(TAG, "refreshItems");
        // Load items
        new GetImagesTask().execute((Void) null);
        // Load complete
        onItemsLoadComplete();
    }

    private void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        mImageCardDataList.clear();
        mImageCardDataList.add(new ImageCardData(R.drawable.corgi, "foo", "bar", "foobar123"));
        Log.d(TAG, "onItemsLoadComplete");

        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
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
        //mSwipeRefreshLayout.setEnabled(false);
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
        Log.d(TAG, "initDataSet");
        mImageCardDataList = new LinkedList<>();
        /*mImageCardDataList.add(new ImageCardData(R.drawable.corgi, "Corgi", "I'm a corgi, woof woof", "foobar123"));
        mImageCardDataList.add(new ImageCardData(R.drawable.corgi, "Corgi", "I'm a corgi, woof woof", "foobar123"));
        mImageCardDataList.add(new ImageCardData(R.drawable.corgi, "Corgi", "I'm a corgi, woof woof", "foobar123"));
        mImageCardDataList.add(new ImageCardData(R.drawable.corgi, "Corgi", "I'm a corgi, woof woof", "foobar123"));
        mImageCardDataList.add(new ImageCardData(R.drawable.corgi, "Corgi", "I'm a corgi, woof woof", "foobar123"));*/
        new GetImagesTask().execute((Void) null);
    }

    @Override
    public void onClick(View v) {
    }

    private class GetImagesTask extends AsyncTask<Void, Void, List<ImageCardData>> {
        private static final String TAG = "GetImagesTask";
        private String mAuthHeader;
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            /*progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Loading");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Please wait while weather data is loaded...");
            progressDialog.setCancelable(false);
            progressDialog.show();*/

            mAuthHeader = "Bearer: " + ((MainActivity) getActivity()).mToken;
        }

        private String getQuery(HashMap<String, String> params) throws UnsupportedEncodingException {
            Log.d(TAG, "getQuery");
            StringBuilder result = new StringBuilder();
            boolean first = true;

            for (HashMap.Entry<String, String> entry : params.entrySet()) {
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }

            return result.toString();
        }

        @Override
        protected List<ImageCardData> doInBackground(Void... params) {
            Log.v(TAG, "doInBackground");
            List<ImageCardData> list = new LinkedList<>();
            URL url = null;
            try {
                url = new URL("http://192.168.0.100:8000/api/image");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            //HashMap<String, String> postData = new HashMap<>();
            String response = "";

            try {
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10_000);
                conn.setConnectTimeout(15_000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", mAuthHeader);
                Log.d(TAG, "doInBackground: mAuthHeader:" + mAuthHeader);
                //conn.setDoOutput(true);
                //conn.setDoInput();
                int responseCode = conn.getResponseCode();
                Log.d(TAG, "doInBackground: responseCode: " + responseCode);

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    Log.d(TAG, "doInBackground: respcode was Ok");
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                    Log.d(TAG, "response: " + response);
                    // Parse the json response
                    //JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        ImageCardData cardData =
                                new ImageCardData(
                                        R.drawable.corgi,
                                        jsonObject.getString("title"),
                                        jsonObject.getString("description"),
                                        jsonObject.getString("_id"));
                        list.add(cardData);
                    }

                    /*
                     {
                        "_id": "57179ae4159335583b464be2",
                        "title": "Cool pic",
                        "description": "Foo bar baz",
                        "uploadPath": "asdfasdf.png",
                        "__v": 0,
                        "comments": [],
                        "hashtags": [],
                        "mentions": [],
                        "likes": [],
                        "modified": "2016-04-20T15:06:05.865Z",
                        "created": "2016-04-20T15:06:05.865Z"
                     },
                    * */
                    /*boolean success = jsonObject.getBoolean("success");
                    String token = jsonObject.getString("token");
                    int expiresIn = jsonObject.getInt("expiresIn");*/
                    /*data.putBoolean("success", success);
                    data.putString("token", token);
                    data.putInt("expiresIn", expiresIn);*/
                    //return "";
                    return list;
                } else {
                    Log.d(TAG, "doInBackground: response was not ok");
                    //response = "";

                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<ImageCardData> list) {
            super.onPostExecute(list);
            //smallTextView.setText(weather);
            //bigTextView.setText(location);
            //imageView.setImageBitmap(weatherIcon);
            //progressDialog.dismiss();
            //mImageCardDataList.remove(pos);
            /*int pos = 0;
            mImageCardDataList.remove(pos);
            mRecyclerView.removeViewAt(pos);
            mAdapter.notifyItemRemoved(pos);
            mAdapter.notifyItemRangeChanged(pos, mImageCardDataList.size());*/
            //mAdapter.notifyDataSetChanged();
            for (ImageCardData cardData : list) {
                mImageCardDataList.add(cardData);
            }
            mImageCardDataList.add(new ImageCardData(R.drawable.corgi, "foo", "bar", "bar123"));
            //mAdapter.notifyDataSetChanged();

            mRecyclerView.removeAllViews();
            mAdapter.notifyItemRangeRemoved(0, mAdapter.getItemCount());

            //mAdapter.notifyItemRangeChanged(pos, mImageCardDataList.size());*/
            mAdapter.notifyDataSetChanged();
        }
    }
}
