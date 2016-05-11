package xyz.atte.img_hurdur;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Fragment that contains comments for a image
 *
 * @author Atte Huhtakangas
 * @author Mikko Tossavainen
 * @version 1.0
 */
public class CommentsFragment extends Fragment {
    private String imageID;
    private String comment = "";
    private EditText commentText;
    private TextView title;

    private ArrayList<CommentData> mCommentsDataList;
    CommentsAdapter adapter;

    private ListView mListView;

    public CommentsFragment() {
    }

    /**
     * Shows the back button and loads the comments
     * <br>
     * {@inheritDoc}
     */
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).showBackButton();
        getCommentsFromServer();
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.comments, container, false);
        Bundle args = getArguments();
        imageID = args.getString("imageID");
        initDataSet();
        return rootView;
    }

    /**
     * Gets the comments from the server
     */
    protected void getCommentsFromServer() {
        new GetCommentsTask().execute();
    }

    /**
     * Posts a comment to the server
     */
    protected void postCommentToServer() {
        comment = commentText.getText().toString();
        new PostCommentTask().execute();
    }


    /**
     * Creates the view
     * {@inheritDoc}
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();

        ((MainActivity) getActivity()).showBackButton();


        super.onViewCreated(view, savedInstanceState);
        mListView = (ListView) view.findViewById(R.id.commentListView);

        adapter = new CommentsAdapter(getActivity(), mCommentsDataList);
        mListView.setAdapter(adapter);

        commentText = (EditText) view.findViewById(R.id.userCommentText);

        title = (TextView) view.findViewById(R.id.commentsImageTitle);
        title.setText(args.getString("title"));


        Button button = (Button) view.findViewById(R.id.postCommentButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postCommentToServer();
            }
        });

    }

    /**
     * Initializes the data set
     */
    private void initDataSet() {
        mCommentsDataList = new ArrayList<>();

    }

    /**
     *
     */
    private class PostCommentTask extends AsyncTask<Void, Void, Void> {
        private String mAuthHeader = "Bearer ";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mAuthHeader = "Bearer " + ((MainActivity) getActivity()).mToken;
        }

        @Override
        protected Void doInBackground(Void... params) {
            URL url = null;

            try {
                url = new URL("http://pulivari.xyz/api/image/" + imageID + "/comment/");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Authorization", mAuthHeader);
                conn.setReadTimeout(10_000);
                conn.setConnectTimeout(15_000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                String query = URLEncoder.encode("text", "UTF-8");
                query += "=" + URLEncoder.encode(comment, "UTF-8");

                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    Log.e("post comments", "doInBackground: failed to get comments");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getCommentsFromServer();
        }
    }

    private class GetCommentsTask extends AsyncTask<Void, Void, List<String>> {

        private String mAuthHeader;
        private String mUrl;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mAuthHeader = "Bearer " + ((MainActivity) getActivity()).mToken;
            this.mUrl = Resources.getSystem().getString(R.string.host_name) + "/api/image/";
        }

        @Override
        protected List<String> doInBackground(Void... params) {
            List<String> mCommentList = new LinkedList<>();
            URL url = null;
            String response = "";

            try {
                url = new URL(mUrl + imageID);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10_000);
                conn.setConnectTimeout(15_000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", mAuthHeader);

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }

                    // Parse the json response
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("comments");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        String text;
                        jsonObject = jsonArray.getJSONObject(i);

                        try {
                            text = jsonObject.getString("text");
                            mCommentList.add(text);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return mCommentList;
        }

        @Override
        protected void onPostExecute(List<String> list) {
            adapter.clear();
            for (int i = 0; i < list.size(); i++) {
                mCommentsDataList.add(new CommentData(list.get(i), imageID, "Anon", null));

            }
            adapter.notifyDataSetChanged();
        }
    }
}
