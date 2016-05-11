package xyz.atte.img_hurdur;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by MikkoEPIC on 20.4.2016.
 */
public class HttpUpload extends AsyncTask<Void, Void, Void> {

    private final String TAG = this.getClass().getSimpleName();
    protected final String mServerUrl = "http://pulivari.xyz/api/upload/";
    protected String mToken;
    protected Activity context;
    protected File mImg;
    protected String mImgPath;
    protected String mImgTitle;
    protected String mImgDescription;


    public HttpUpload(Activity context,
                      String mToken,
                      String mImgPath,
                      String mImgTitle,
                      String mImgDescription,
                      File mImg) {
        super();
        this.mImg = mImg;
        this.mToken = mToken;
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
    protected Void doInBackground(Void... arg0) {

        String mAuthHeader = "Bearer " + mToken;

        // TODO Auto-generated method stub
        HttpURLConnection conn = null;
        DataOutputStream outputStream = null;
        DataInputStream inputStream = null;

        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        String result = "";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        String[] q = mImg.getPath().split("/");
        int idx = q.length - 1;

        try {
            FileInputStream fileInputStream = new FileInputStream(mImg);

            URL url = new URL(mServerUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.addRequestProperty("Authorization", mAuthHeader);

            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            outputStream = new DataOutputStream(conn.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\"" + q[idx] + "\"" + lineEnd);
            outputStream.writeBytes("Content-Type: " + "image/jpeg" + lineEnd);
            outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);

            outputStream.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            outputStream.writeBytes(lineEnd);
            //outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Upload post data
            // Title
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"" + "title" + "\"" + lineEnd);
            outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(mImgTitle);
            outputStream.writeBytes(lineEnd);

            // description
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"" + "description" + "\"" + lineEnd);
            outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(mImgDescription);
            outputStream.writeBytes(lineEnd);


            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            if (conn.getResponseCode() != 200) {
                Log.w(TAG, "doInBackground: Failed to upload image! response code: " + conn.getResponseCode());
            }

            fileInputStream.close();
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            Log.e("Debug", "error: " + e.getMessage(), e);
        }

        //------------------ read the SERVER RESPONSE
        try {
            inputStream = new DataInputStream(conn.getInputStream());
            String str;
            while ((str = inputStream.readLine()) != null) {
                Log.e("Debug", "Server Response " + str);
                //reponse_data=str;
            }
            inputStream.close();
        } catch (IOException ioex) {
            Log.e("Debug", "error: " + ioex.getMessage(), ioex);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        // End ImageUploadActivity
        context.finish();



    }
}
