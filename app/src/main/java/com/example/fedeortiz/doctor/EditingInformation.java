package com.example.fedeortiz.doctor;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditingInformation extends AppCompatActivity {

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

    // declare GUI components
    private EditText mFirstNameText;
    private EditText mLastnameText;
    private EditText mEmailText;
    private EditText mAddressText;
    private EditText mCityText;
    private EditText mStateText;
    private EditText mZipcodeText;
    private EditText mPhoneNumberText;
    private EditText mChiefComplaintText;
    private EditText mOnsetText;
    private EditText mSymptomsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editing_information);

        mIntent = getIntent();

        // bundle and bringing in patient email
        mBundle = mIntent.getExtras();
        patientEmail = mBundle.getString("patientEmail");
        Toast.makeText(this, "Email: " + patientEmail, Toast.LENGTH_LONG).show();

        patientDB = new PatientDBHandler(this);
        symptomsDB = new SymptomsDBHandler(this);

        globalPatient = patientDB.findPatient(patientEmail);
        globalSymptoms = symptomsDB.findSymptom(patientEmail);

        // Assign GUI components
        mFirstNameText = (EditText)findViewById(R.id.firstNameText);
        mLastnameText = (EditText)findViewById(R.id.lastNameText);
        mEmailText = (EditText)findViewById(R.id.emailText);
        mAddressText = (EditText)findViewById(R.id.addressText);
        mCityText = (EditText)findViewById(R.id.cityText);
        mStateText = (EditText)findViewById(R.id.stateText);
        mZipcodeText = (EditText)findViewById(R.id.zipcodeText);
        mPhoneNumberText = (EditText)findViewById(R.id.phoneNumberText);
        mChiefComplaintText = (EditText) findViewById(R.id.complaintText);
        mOnsetText = (EditText) findViewById(R.id.onsetText);
        mSymptomsText = (EditText)findViewById(R.id.symptomsText);

        // setting textView GUI elements to their respective Strings
        if (globalPatient != null && globalSymptoms != null) {
            mFirstNameText.setText(globalPatient.getmFirstName().toString());
            mLastnameText.setText(globalPatient.getmLastName().toString());
            mEmailText.setText(globalPatient.getmEmail().toString());
            mAddressText.setText(globalPatient.getmAddress().toString());
            mCityText.setText(globalPatient.getmCity().toString() + ", " + globalPatient.getmState() + "   " + globalPatient.getmZipcode());
            mStateText.setText(globalPatient.getmState().toString());
            mZipcodeText.setText(globalPatient.getmZipcode().toString());
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
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    public void onSaveClick(View view) {
        Intent intent = new Intent(this, Review.class);

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
        String symptoms = mSymptomsText.getText().toString();

        if (firstname.isEmpty() || lastname.isEmpty() || email.isEmpty() || address.isEmpty() || city.isEmpty() || state.isEmpty() || zipcode.isEmpty() || phoneNumber.isEmpty() || complaint.isEmpty() || onset.isEmpty() || symptoms.isEmpty()) {
            Toast.makeText(this, "Enter all information", Toast.LENGTH_LONG).show();
        }
        else {

            Patient patient = new Patient(firstname, lastname, email, address, city, state, zipcode, phoneNumber, complaint, onset);
            Symptoms symptom = new Symptoms(symptoms, email);

            PatientDBHandler handler = new PatientDBHandler(this);
            SymptomsDBHandler sHandler = new SymptomsDBHandler(this);

            handler.updatePatient(patient, patientEmail);
            sHandler.updateSymptoms(symptom, patientEmail);

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
            mSymptomsText.setText("");

            Toast.makeText(this, "Information Updated!", Toast.LENGTH_LONG).show();

            if (USE_FLAG)
                    mIntent.addFlags(mFlag);

            // passing email through bundle
            Bundle edit = new Bundle();
            edit.putString("patientEmail", email);
            intent.putExtras(edit);

            intent.putExtra(KEY_STEP, mStep + 1);

            startActivity(intent);
        }
    }
}
