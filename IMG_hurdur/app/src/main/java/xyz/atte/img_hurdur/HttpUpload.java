package xyz.atte.img_hurdur;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by MikkoEPIC on 20.4.2016.
 */
public class HttpUpload extends AsyncTask<Void, Void, String> {

    private final String TAG = this.getClass().getSimpleName();
    protected String mServerUrl;
    protected Context context;
    protected Bitmap mImg;
    protected String mImgPath;
    protected String mImgTitle;
    protected String mImgDescription;


    public HttpUpload(Context context, String mImgPath, String mImgTitle, String mImgDescription) {
        super();
        this.context = context;
        this.mImgPath = mImgPath;
        this.mImgTitle = mImgTitle;
        this.mImgDescription = mImgDescription;

    }

    @Override
    protected void onPreExecute() {
        //TODO add some progression display for upload?
    }

    @Override
    protected String doInBackground(Void... voids) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //compress the image to jpg format
        mImg.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        //Encode image to Base64
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

        //generate hashMap to store encodedImage and the name
        HashMap<String, String> detail = new HashMap<>();
        detail.put("title", mImgTitle);
        detail.put("image", encodedImage);
        detail.put("description", mImgDescription);

        try {
            //convert this HashMap to encodedUrl
            String dataToSend = hashMapToUrl(detail);
            //make a Http request and send data
            String response = post(mServerUrl, dataToSend);

            //return the response
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "ERROR  " + e);
            return null;
        }
    }

    private String hashMapToUrl(HashMap<String, String> params) {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    public String post(String serverUrl, String dataToSend) {
        try {
            URL url = new URL(serverUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            //set timeout of 30 seconds
            con.setConnectTimeout(1000 * 30);
            con.setReadTimeout(1000 * 30);
            //Http-method
            con.setRequestMethod("POST");
            con.setDoOutput(true);

            OutputStream os = con.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

            //make request
            writer.write(dataToSend);
            writer.flush();
            writer.close();
            os.close();

            //get the response
            int responseCode = con.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                //read the response
                StringBuilder sb = new StringBuilder();

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String line;

                //loop through the response from the server
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }

                //return the response
                return sb.toString();
            } else {
                Log.e(TAG, "ERROR - Invalid response code from server " + responseCode);
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
