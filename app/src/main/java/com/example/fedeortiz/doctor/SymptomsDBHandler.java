package com.example.fedeortiz.doctor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by fede on 3/6/18.
 */

public class SymptomsDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "symptomDB.db";

    private static final String TABLE_SYMPTOMS = "Symptoms";
    private static final String COLUMN_SYMPTOM = "symptom";
    private static final String COLUMN_EMAIL = "email";

    public SymptomsDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SYMPTOMS_TABLE = "CREATE TABLE " +
                TABLE_SYMPTOMS + "(" +
                COLUMN_SYMPTOM + " TEXT, " +
                COLUMN_EMAIL + " TEXT)";

        db.execSQL(CREATE_SYMPTOMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SYMPTOMS);

        onCreate(db);
    }

    public void addSymptom(Symptoms symptom) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_SYMPTOM, symptom.getmSymptoms());
        values.put(COLUMN_EMAIL, symptom.getmEmail());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_SYMPTOMS, null, values);

        db.close();
    }

    public Symptoms findSymptom(String email) {
        String sqlQuery = "SELECT * FROM " + TABLE_SYMPTOMS +
                " WHERE " + COLUMN_EMAIL + " = \"" +
                email + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor myCursor = db.rawQuery(sqlQuery, null);

        Symptoms mySymptom = null;

        if(myCursor.moveToFirst()) {
            String tmpSymptom = myCursor.getString(0);
            String tmpEmail = myCursor.getString(1);
            myCursor.close();
            mySymptom = new Symptoms(tmpSymptom, tmpEmail);
        }

        db.close();

        return mySymptom;
    }

    public boolean deleteSymptom(String email) {
        boolean result = false;

        String sql_query = "SELECT * FROM " + TABLE_SYMPTOMS +
                " WHERE " + COLUMN_EMAIL + " =\"" +
                email + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor myCursor = db.rawQuery(sql_query, null);

        if (myCursor.moveToFirst()) {
            String tmpEmail = myCursor.getString(1);

            db.delete(TABLE_SYMPTOMS, COLUMN_EMAIL + "= ?",
                    new String[]{String.valueOf(tmpEmail)});

            myCursor.close();

            result = true;
        }

        db.close();
        return result;
    }

    public void updateSymptoms(Symptoms symptom, String email) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_SYMPTOM, symptom.getmSymptoms());
        values.put(COLUMN_EMAIL, symptom.getmEmail());

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            deleteSymptom(email);
            addSymptom(symptom);
            //db.update(TABLE_SYMPTOMS, values, email, null);
        }
        catch (Exception e) {
            System.out.println("Error updating Symptom");
        }

        db.close();
    }
}
