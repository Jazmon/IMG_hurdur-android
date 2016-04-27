package xyz.atte.img_hurdur;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageUploadActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private final String TAG = getClass().getSimpleName();
    private ImageView mCameraPictureView;
    //private Button mTakePictureButton;
    protected String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);

        Toolbar toolbar = (Toolbar) findViewById(R.id.uploadToolbar);
        setSupportActionBar(toolbar);

        // Show back button on toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Add listener to the back button in toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mCameraPictureView = (ImageView) findViewById(R.id.mCameraPreviewImage);
        Log.d(TAG, String.valueOf(mCameraPictureView.getWidth()) + " " + String.valueOf(mCameraPictureView.getHeight()));
        //mTakePictureButton = (Button) findViewById(R.id.mTakePhotoButton);

        dispatchTakePictureIntent();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        Log.d(TAG, timeStamp);
        String imageFileName = "JPEG_" + timeStamp + "_";
        Log.d(TAG, imageFileName + ".jpg");
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        Log.d(TAG, storageDir + imageFileName + ".jpg");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir   /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.d(TAG, image.getAbsolutePath());
        return image;
    }

    private void setPic() {
        /*
        // Get the dimensions of the View
        int targetW = mCameraPictureView.getWidth();
        int targetH = mCameraPictureView.getHeight();

        Log.d(TAG,String.valueOf(targetW) + " " + String.valueOf(targetH));

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        */

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
        mCameraPictureView.setImageBitmap(bitmap);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        setPic();
        /*
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mCameraPictureView.setImageBitmap(imageBitmap);
        } */


    }
}
