package com.example.patienttracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "PatientTracker.db";
    private static final int DATABASE_VERSION = 2; // Incremented version

    // Patient table
    private static final String TABLE_PATIENTS = "patients";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_AGE = "age";
    private static final String COLUMN_GENDER = "gender";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_EMAIL = "email";

    // Medical records table
    private static final String TABLE_MEDICAL_RECORDS = "medical_records";
    private static final String COLUMN_RECORD_ID = "record_id";
    private static final String COLUMN_PATIENT_ID = "patient_id";
    private static final String COLUMN_VISIT_DATE = "visit_date";
    private static final String COLUMN_DISEASE = "disease";
    private static final String COLUMN_SYMPTOMS = "symptoms";
    private static final String COLUMN_MEDICATION = "medication";
    private static final String COLUMN_NOTES = "notes";
    private static final String COLUMN_ARRIVAL_DATE = "arrival_date";

    // Users table
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_ROLE = "role";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createPatientTable = "CREATE TABLE " + TABLE_PATIENTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT NOT NULL,"
                + COLUMN_AGE + " INTEGER,"
                + COLUMN_GENDER + " TEXT,"
                + COLUMN_PHONE + " TEXT,"
                + COLUMN_EMAIL + " TEXT"
                + ")";

        String createMedicalRecordsTable = "CREATE TABLE " + TABLE_MEDICAL_RECORDS + "("
                + COLUMN_RECORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_PATIENT_ID + " INTEGER,"
                + COLUMN_VISIT_DATE + " TEXT NOT NULL,"
                + COLUMN_DISEASE + " TEXT NOT NULL,"
                + COLUMN_SYMPTOMS + " TEXT,"
                + COLUMN_MEDICATION + " TEXT,"
                + COLUMN_NOTES + " TEXT,"
                + COLUMN_ARRIVAL_DATE + " TEXT,"
                + "FOREIGN KEY(" + COLUMN_PATIENT_ID + ") REFERENCES " + TABLE_PATIENTS + "(" + COLUMN_ID + ") ON DELETE CASCADE"
                + ")";

        String createUserTable = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERNAME + " TEXT UNIQUE NOT NULL,"
                + COLUMN_PASSWORD + " TEXT NOT NULL,"
                + COLUMN_ROLE + " TEXT NOT NULL"
                + ")";

        db.execSQL(createPatientTable);
        db.execSQL(createMedicalRecordsTable);
        db.execSQL(createUserTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            String createUserTable = "CREATE TABLE " + TABLE_USERS + "("
                    + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_USERNAME + " TEXT UNIQUE NOT NULL,"
                    + COLUMN_PASSWORD + " TEXT NOT NULL,"
                    + COLUMN_ROLE + " TEXT NOT NULL"
                    + ")";
            db.execSQL(createUserTable);
        }
    }

    // User CRUD operations
    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, user.getUsername());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_ROLE, user.getRole().name());

        long id = db.insert(TABLE_USERS, null, values);
        db.close();
        return id;
    }

    public User getUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_USER_ID, COLUMN_USERNAME, COLUMN_PASSWORD, COLUMN_ROLE},
                COLUMN_USERNAME + "=?",
                new String[]{username},
                null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            User user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)),
                    Role.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE)))
            );
            cursor.close();
            db.close();
            return user;
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return null;
    }

    // Patient CRUD operations
    public long addPatient(Patient patient) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, patient.getName());
        values.put(COLUMN_AGE, patient.getAge());
        values.put(COLUMN_GENDER, patient.getGender());
        values.put(COLUMN_PHONE, patient.getPhone());
        values.put(COLUMN_EMAIL, patient.getEmail());

        long id = db.insert(TABLE_PATIENTS, null, values);
        db.close();
        return id;
    }

    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PATIENTS + " ORDER BY " + COLUMN_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                Patient patient = new Patient();
                patient.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                patient.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
                patient.setAge(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AGE)));
                patient.setGender(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENDER)));
                patient.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)));
                patient.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)));
                patients.add(patient);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return patients;
    }

    // Medical Record CRUD operations
    public long addMedicalRecord(MedicalRecord record) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PATIENT_ID, record.getPatientId());
        values.put(COLUMN_VISIT_DATE, record.getVisitDate());
        values.put(COLUMN_DISEASE, record.getDisease());
        values.put(COLUMN_SYMPTOMS, record.getSymptoms());
        values.put(COLUMN_MEDICATION, record.getMedication());
        values.put(COLUMN_NOTES, record.getNotes());
        values.put(COLUMN_ARRIVAL_DATE, record.getArrivalDate());

        long id = db.insert(TABLE_MEDICAL_RECORDS, null, values);
        db.close();
        return id;
    }

    public List<MedicalRecord> getAllMedicalRecords() {
        List<MedicalRecord> records = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MEDICAL_RECORDS +
                " ORDER BY " + COLUMN_VISIT_DATE + " DESC", null);

        if (cursor.moveToFirst()) {
            do {
                MedicalRecord record = new MedicalRecord();
                record.setRecordId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RECORD_ID)));
                record.setPatientId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PATIENT_ID)));
                record.setVisitDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VISIT_DATE)));
                record.setDisease(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DISEASE)));
                record.setSymptoms(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SYMPTOMS)));
                record.setMedication(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEDICATION)));
                record.setNotes(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTES)));
                record.setArrivalDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ARRIVAL_DATE)));
                records.add(record);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return records;
    }

    public List<MedicalRecord> getMedicalRecordsByFilter(String patientName, String disease, String lastVisit, String arrivalDate) {
        List<MedicalRecord> records = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        StringBuilder query = new StringBuilder("SELECT mr.*, p."+COLUMN_NAME+" FROM " + TABLE_MEDICAL_RECORDS + " mr JOIN "+TABLE_PATIENTS+" p ON mr."+COLUMN_PATIENT_ID+" = p."+COLUMN_ID+" WHERE 1=1");
        List<String> params = new ArrayList<>();

        if (patientName != null && !patientName.isEmpty()) {
            query.append(" AND p.").append(COLUMN_NAME).append(" LIKE ?");
            params.add("%" + patientName + "%");
        }

        if (disease != null && !disease.isEmpty()) {
            query.append(" AND mr.").append(COLUMN_DISEASE).append(" LIKE ?");
            params.add("%" + disease + "%");
        }

        if (lastVisit != null && !lastVisit.isEmpty()) {
            query.append(" AND mr.").append(COLUMN_VISIT_DATE).append(" = ?");
            params.add(lastVisit);
        }

        if (arrivalDate != null && !arrivalDate.isEmpty()) {
            query.append(" AND mr.").append(COLUMN_ARRIVAL_DATE).append(" = ?");
            params.add(arrivalDate);
        }

        query.append(" ORDER BY mr.").append(COLUMN_VISIT_DATE).append(" DESC");

        Cursor cursor = db.rawQuery(query.toString(), params.toArray(new String[0]));

        if (cursor.moveToFirst()) {
            do {
                MedicalRecord record = new MedicalRecord();
                record.setRecordId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RECORD_ID)));
                record.setPatientId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PATIENT_ID)));
                record.setVisitDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VISIT_DATE)));
                record.setDisease(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DISEASE)));
                record.setSymptoms(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SYMPTOMS)));
                record.setMedication(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEDICATION)));
                record.setNotes(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTES)));
                record.setArrivalDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ARRIVAL_DATE)));
                records.add(record);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return records;
    }
}
