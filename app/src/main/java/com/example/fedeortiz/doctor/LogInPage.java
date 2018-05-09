package com.example.fedeortiz.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LogInPage extends AppCompatActivity {

    //textview where the log will be displayed
    //private TextView mLogTextView;
    //int we'll use to keep track of which step we are on for display
    private int mStep;
    //Key to yse to pass and retrieve which step we are on
    private String KEY_STEP = "stepcount";

    private static final boolean USE_FLAG = true;
    private static final int mFlag = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;

    private Bundle mBundle;

    // declare GUI components
    private EditText mFirstNameText;
    private EditText mLastnameText;
    private EditText mEmailText;
    private EditText mAddressText;
    private EditText mCityText;
    private EditText mStateText;
    private EditText mZipcodeText;
    private EditText mPhoneNumberText;
    private TextInputEditText mChiefComplaintText;
    private TextInputEditText mOnsetText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //mLogTextView = (TextView) findViewById(R.id.testTextView);

        // Assign GUI components
        mFirstNameText = (EditText)findViewById(R.id.firstNameText);
        mLastnameText = (EditText)findViewById(R.id.lastNameText);
        mEmailText = (EditText)findViewById(R.id.emailText);
        mAddressText = (EditText)findViewById(R.id.addressText);
        mCityText = (EditText)findViewById(R.id.cityText);
        mStateText = (EditText)findViewById(R.id.stateText);
        mZipcodeText = (EditText)findViewById(R.id.zipcodeText);
        mPhoneNumberText = (EditText)findViewById(R.id.phoneNumberText);
        mChiefComplaintText = (TextInputEditText)findViewById(R.id.complaintText);
        mOnsetText = (TextInputEditText)findViewById(R.id.onsetText);

        mBundle = new Bundle();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_log_in_page, menu);
        //return true;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        // Handle item selection
        Intent mIntent;
        switch (item.getItemId()) {
            case R.id.user:
                if (USE_FLAG)
                    mIntent = new Intent(this, LogInPage.class);
                mIntent.addFlags(mFlag);

                mIntent.putExtra(KEY_STEP, mStep + 1);

                startActivity(mIntent);
                //Toast.makeText(this, "User works", Toast.LENGTH_LONG).show();
                return true;
            case R.id.map:
                if (USE_FLAG)
                    mIntent = new Intent(this, Map2.class);
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

    public void onLogInToSymptomsButtonClick(View v) {
        Intent mIntent = new Intent(this, symptomsPage.class);

        String firstname = mFirstNameText.getText().toString();
        String lastname = mLastnameText.getText().toString();
        String email = mEmailText.getText().toString();
        String address = mAddressText.getText().toString();
        String city = mCityText.getText().toString();
        String state = mStateText.getText().toString();
        String zipcode = mZipcodeText.getText().toString();
        String phoneNumber = mPhoneNumberText.getText().toString();
        String complaint = mChiefComplaintText.getText().toString();
        String onset = mOnsetText.getText().toString();

        if (firstname.isEmpty() || lastname.isEmpty() || email.isEmpty() || address.isEmpty() || city.isEmpty() || state.isEmpty() || zipcode.isEmpty() || phoneNumber.isEmpty() || complaint.isEmpty() || onset.isEmpty()) {
            Toast.makeText(this, "Enter all information", Toast.LENGTH_LONG).show();
        }
        else {

            Patient patient = new Patient(firstname, lastname, email, address, city, state, zipcode, phoneNumber, complaint, onset);

            PatientDBHandler handler = new PatientDBHandler(this);

            if (handler.findPatient(email) == null) {

                handler.addPatient(patient);

                mBundle.putString("patientEmail", email);

                mFirstNameText.setText("");
                mLastnameText.setText("");
                mEmailText.setText("");
                mAddressText.setText("");
                mCityText.setText("");
                mStateText.setText("");
                mZipcodeText.setText("");
                mPhoneNumberText.setText("");
                mChiefComplaintText.setText("");
                mOnsetText.setText("");

                Toast.makeText(this, "Welcome to DOCtor " + firstname + "!", Toast.LENGTH_LONG).show();


                if (USE_FLAG)
                    mIntent.addFlags(mFlag);

                mIntent.putExtra(KEY_STEP, mStep + 1);
                mIntent.putExtras(mBundle);

                startActivity(mIntent);
            }
            else
                Toast.makeText(this, "That email already exists!", Toast.LENGTH_LONG).show();
        }
    }
}
