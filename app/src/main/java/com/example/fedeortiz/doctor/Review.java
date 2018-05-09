package com.example.fedeortiz.doctor;

import android.content.Intent;
import android.icu.text.SymbolTable;
import android.net.Uri;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Review extends AppCompatActivity {

    //textview where the log will be displayed
    //private TextView mLogTextView;
    //int we'll use to keep track of which step we are on for display
    private int mStep;
    //Key to yse to pass and retrieve which step we are on
    private String KEY_STEP = "stepcount";

    private static final boolean USE_FLAG = true;
    private static final int mFlag = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;

    private PatientDBHandler patientDB;
    private SymptomsDBHandler symptomsDB;
    private Patient globalPatient;
    private Symptoms globalSymptoms;

    private Intent mIntent;
    private Bundle mBundle;
    private String patientEmail;

    private TextView mSymptomsText;

    private TextView mFirstNameText;
    private TextView mLastnameText;
    private TextView mEmailText;
    private TextView mAddressText;
    private TextView mCityText;
    private TextView mPhoneNumberText;
    private TextView mChiefComplaintText;
    private TextView mOnsetText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        mIntent = getIntent();

        mBundle = mIntent.getExtras();
        patientEmail = mBundle.getString("patientEmail");
        //Toast.makeText(this, "Email: " + patientEmail, Toast.LENGTH_LONG).show();

        patientDB = new PatientDBHandler(this);
        symptomsDB = new SymptomsDBHandler(this);

        globalPatient = patientDB.findPatient(patientEmail);
        globalSymptoms = symptomsDB.findSymptom(patientEmail);

        // initalizing GUI elements
        mFirstNameText = (TextView)findViewById(R.id.firstNameText2);
        mLastnameText = (TextView)findViewById(R.id.lastNameText2);
        mEmailText = (TextView)findViewById(R.id.emailText2);
        mAddressText = (TextView)findViewById(R.id.addressText2);
        mCityText = (TextView)findViewById(R.id.cityText2);
        mPhoneNumberText = (TextView)findViewById(R.id.phoneNumberText2);
        mChiefComplaintText = (TextView)findViewById(R.id.complaintText2);
        mOnsetText = (TextView)findViewById(R.id.onsetText2);
        mSymptomsText = (TextView)findViewById(R.id.symptomsText);

        // setting textView GUI elements to their respective Strings
        if (globalPatient != null && globalSymptoms != null) {
            mFirstNameText.setText(globalPatient.getmFirstName().toString());
            mLastnameText.setText(globalPatient.getmLastName().toString());
            mEmailText.setText(globalPatient.getmEmail().toString());
            mAddressText.setText(globalPatient.getmAddress().toString());
            mCityText.setText(globalPatient.getmCity().toString() + ", " + globalPatient.getmState() + "   " + globalPatient.getmZipcode());
            mPhoneNumberText.setText(globalPatient.getmPhoneNumber().toString());
            mChiefComplaintText.setText(globalPatient.getmChiefComplaint().toString());
            mOnsetText.setText(globalPatient.getmOnset().toString());
            mSymptomsText.setText(globalSymptoms.getmSymptoms().toString());
        }
        else
            mFirstNameText.setText("Patient not found");
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

        //mLogTextView.setText(mLogTextView.getText() + " " + mStep);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    // code to send email and pass all information to gmail
    public void onReviewToLogINButtonClick(View view) {
        Intent intent = new Intent(this, LogInPage.class);
        // whenever we learn how to send emails and stuff this will do that
        // and then go to the landing/log in page
        String emailAddress = patientEmail;
        PatientDBHandler handler = new PatientDBHandler(this);
        SymptomsDBHandler sHandler = new SymptomsDBHandler(this);

        Patient patient = handler.findPatient(emailAddress);
        Symptoms symptom = sHandler.findSymptom(emailAddress);

        String msgBody = "First name: " + patient.getmFirstName().toString() + "\n" +
                "Last name: " + patient.getmLastName().toString() + "\n" +
                "Email: " + patient.getmEmail().toString() + "\n" +
                "Address: " + patient.getmAddress().toString() + "\n" +
                "City: " + patient.getmCity().toString() + ", State: " + patient.getmState().toString() + ", Zipcode: " + patient.getmZipcode().toString() + "\n" +
                "Phone Number: " + patient.getmPhoneNumber().toString() + "\n" +
                "Chief Complaint: " + patient.getmChiefComplaint().toString() + "\n" +
                "Onset: " + patient.getmOnset().toString()  + "\n" +
                "Symptoms: " + symptom.getmSymptoms();

        if (!mEmailText.getText().toString().isEmpty()) {

            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.setType("text/plain");

            emailIntent.putExtra(Intent.EXTRA_EMAIL, emailAddress);
            emailIntent.putExtra(Intent.EXTRA_TEXT, msgBody);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Patient chart: " + patient.getmFirstName().toString() + " " + patient.getmLastName().toString());

            try {
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Problem sending email", Toast.LENGTH_LONG).show();
            }
        }
        else
            Toast.makeText(getApplicationContext(), "Please enter email", Toast.LENGTH_LONG).show();
    }


    public void onUpdateInfoClick(View view) {
        Intent mIntent = new Intent(this, EditingInformation.class);

        if (USE_FLAG)
            mIntent.addFlags(mFlag);

        // passing email through bundle
        Bundle edit = new Bundle();
        edit.putString("patientEmail", patientEmail);
        mIntent.putExtras(edit);

        mIntent.putExtra(KEY_STEP, mStep + 1);

        startActivity(mIntent);

    }
}
