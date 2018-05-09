package com.example.fedeortiz.doctor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class symptomsPage extends AppCompatActivity {

    //textview where the log will be displayed
    //private TextView mLogTextView;
    //int we'll use to keep track of which step we are on for display
    private int mStep;
    //Key to yse to pass and retrieve which step we are on
    private String KEY_STEP = "stepcount";

    private static final boolean USE_FLAG = true;
    private static final int mFlag = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;

    // intent for bundle
    private Intent mIntent;

    // bundle
    private Bundle mBundle;

    //declare GUI components
    private EditText mSymptoms;
    private String mEmail;

    // for camera
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView mImageView;
    String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptoms_page);

        mIntent = getIntent();

        // bundle and bringing in patient email
        mBundle = mIntent.getExtras();
        mEmail = mBundle.getString("patientEmail");
        //Toast.makeText(this, "Email: " + mEmail, Toast.LENGTH_LONG).show();

        //Assign GUI Components
        mSymptoms = (EditText)findViewById(R.id.symptomsText);
        mImageView = (ImageView)findViewById(R.id.imageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_log_in_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent mIntent = new Intent(this, LogInPage.class);
        switch (item.getItemId()) {
            case R.id.user:
                if (USE_FLAG)
                    mIntent.addFlags(mFlag);

                mIntent.putExtra(KEY_STEP, mStep + 1);

                startActivity(mIntent);
                //Toast.makeText(this, "User works", Toast.LENGTH_LONG).show();
                return true;
            case R.id.map:
                if (USE_FLAG)
                    mIntent.addFlags(mFlag);

                mIntent.putExtra(KEY_STEP, mStep + 1);

                startActivity(mIntent);
                //Toast.makeText(this, "Map works", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Bundle myData = getIntent().getExtras();
        if (myData == null) {
            mStep = 0;
        }
        else
            mStep = myData.getInt(KEY_STEP);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode  == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
        }
    }

    public void onSymptomsToReviewButtonClick(View v) {
        Intent mIntent = new Intent(this, Review.class);

        String patientSymptoms = mSymptoms.getText().toString();
        String email = mEmail;

        Bundle emailBundle = new Bundle();

        //Toast.makeText(this, email, Toast.LENGTH_LONG).show();

        if (patientSymptoms.isEmpty()) {
            Toast.makeText(this, "Enter all information", Toast.LENGTH_LONG).show();
        }
        else {

            Symptoms symptom = new Symptoms(patientSymptoms, email);

            SymptomsDBHandler handler = new SymptomsDBHandler(this);

            handler.addSymptom(symptom);

            mSymptoms.setText("");

            Toast.makeText(this, "Your Symptoms have been added!", Toast.LENGTH_LONG).show();

            if (USE_FLAG)
                mIntent.addFlags(mFlag);

            emailBundle.putString("patientEmail", email);
            mIntent.putExtras(emailBundle);

            mIntent.putExtra(KEY_STEP, mStep + 1);

            startActivity(mIntent);
        }
    }

    public void onCameraClick(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private File createImageFile() throws IOException {
        // create file name
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timestamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        //save a file
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                System.out.println("Error getting image");
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
}
