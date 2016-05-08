package xyz.atte.img_hurdur;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageUploadActivity extends AppCompatActivity {

    private static final int MULTIPLE_PERMISSION_CODE = 12;
    private static final int IMAGE_CAPTURE_PERMISSION_REQUEST_ID = 2;
    File photoFile;
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
        getPermissions();
        Log.d(TAG, String.valueOf(mCameraPictureView.getWidth()) + " " + String.valueOf(mCameraPictureView.getHeight()));
        //mTakePictureButton = (Button) findViewById(R.id.mTakePhotoButton);



    }

    public void uploadImage(View v) {
        Log.d(TAG,"upload");
        //Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
        new HttpUpload(this,mCurrentPhotoPath,"test title","this is test description",photoFile).execute();
    }

    private void getPermissions() {

        // Ask external storage write permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA},
                        MULTIPLE_PERMISSION_CODE);
        } else {
            dispatchTakePictureIntent();
        }

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, IMAGE_CAPTURE_PERMISSION_REQUEST_ID);
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
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
        mCameraPictureView.setImageBitmap(bitmap);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    dispatchTakePictureIntent();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
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
