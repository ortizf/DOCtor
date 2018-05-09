package com.example.fedeortiz.doctor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by fede on 3/5/18.
 */


public class PatientDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "patientDB.db";

    private static final String TABLE_PATIENTS = "Patients";
    private static final String COLUMN_FIRSTNAME = "firstname";
    private static final String COLUMN_LASTNAME = "lastname";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_CITY = "city";
    private static final String COLUMN_STATE = "state";
    private static final String COLUMN_ZIPCODE = "zipcode";
    private static final String COLUMN_PHONENUMBER = "phonenumber";
    private static final String COLUMN_COMPLAINT = "complaint";
    private static final String COLUMN_ONSET = "onset";

    public PatientDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PATIENTS_TABLE = "CREATE TABLE " +
                TABLE_PATIENTS + "(" +
                COLUMN_FIRSTNAME + " TEXT, " +
                COLUMN_LASTNAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_ADDRESS + " TEXT, " +
                COLUMN_CITY + " TEXT, " +
                COLUMN_STATE + " TEXT, " +
                COLUMN_ZIPCODE + " TEXT, " +
                COLUMN_PHONENUMBER + " TEXT, " +
                COLUMN_COMPLAINT + " TEXT, " +
                COLUMN_ONSET + " TEXT)";

        db.execSQL(CREATE_PATIENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENTS);

        onCreate(db);
    }

    public void addPatient(Patient patient) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRSTNAME, patient.getmFirstName());
        values.put(COLUMN_LASTNAME, patient.getmLastName());
        values.put(COLUMN_EMAIL, patient.getmEmail());
        values.put(COLUMN_ADDRESS, patient.getmAddress());
        values.put(COLUMN_CITY, patient.getmCity());
        values.put(COLUMN_STATE, patient.getmState());
        values.put(COLUMN_ZIPCODE, patient.getmZipcode());
        values.put(COLUMN_PHONENUMBER, patient.getmPhoneNumber());
        values.put(COLUMN_COMPLAINT, patient.getmChiefComplaint());
        values.put(COLUMN_ONSET, patient.getmOnset());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_PATIENTS, null, values);

        db.close();
    }

    public Patient findPatient(String patientEmail) {
        String sqlQuery = "SELECT * FROM " + TABLE_PATIENTS +
                " WHERE " + COLUMN_EMAIL + " = \"" +
                patientEmail + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor myCursor = db.rawQuery(sqlQuery, null);

        Patient myPatient = null;

        if(myCursor.moveToFirst()) {
            String tmpFirstName = myCursor.getString(0);
            String tmpLastName = myCursor.getString(1);
            String tmpEmail = myCursor.getString(2);
            String tmpAddress = myCursor.getString(3);
            String tmpCity = myCursor.getString(4);
            String tmpState = myCursor.getString(5);
            String tmpZipcode = myCursor.getString(6);
            String tmpNumber = myCursor.getString(7);
            String tmpComplaint = myCursor.getString(8);
            String tmpOnset = myCursor.getString(9);
            myCursor.close();
            myPatient = new Patient(tmpFirstName, tmpLastName, tmpEmail, tmpAddress, tmpCity, tmpState, tmpZipcode, tmpNumber, tmpComplaint, tmpOnset);
        }

        db.close();

        return myPatient;
    }

    public boolean deletePatient(String patientEmail) {
        boolean result = false;

        String sql_query = "SELECT * FROM " + TABLE_PATIENTS +
                " WHERE " + COLUMN_EMAIL + " =\"" +
                patientEmail + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor myCursor = db.rawQuery(sql_query, null);

        if (myCursor.moveToFirst()) {
            String tmpFirstName = myCursor.getString(0);

            db.delete(TABLE_PATIENTS, COLUMN_FIRSTNAME + "= ?",
                    new String[]{String.valueOf(tmpFirstName)});

            myCursor.close();

            result = true;
        }

        db.close();
        return result;
    }

    public void updatePatient(Patient patient, String email) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRSTNAME, patient.getmFirstName());
        values.put(COLUMN_LASTNAME, patient.getmLastName());
        values.put(COLUMN_EMAIL, patient.getmEmail());
        values.put(COLUMN_ADDRESS, patient.getmAddress());
        values.put(COLUMN_CITY, patient.getmCity());
        values.put(COLUMN_STATE, patient.getmState());
        values.put(COLUMN_ZIPCODE, patient.getmZipcode());
        values.put(COLUMN_PHONENUMBER, patient.getmPhoneNumber());
        values.put(COLUMN_COMPLAINT, patient.getmChiefComplaint());
        values.put(COLUMN_ONSET, patient.getmOnset());

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            deletePatient(email);
            addPatient(patient);
            //db.update(TABLE_PATIENTS, values, email, null);
        }
        catch (Exception e) {
            System.out.println("Error updating Patient");
        }

        db.close();
    }
}
